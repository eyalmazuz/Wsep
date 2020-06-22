package Domain.TradingSystem;

import DTOs.*;
import DataAccess.DAOManager;
import DataAccess.DatabaseFetchException;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@DatabaseTable(tableName = "stores")
public class Store {

    public static AtomicInteger globalId = new AtomicInteger(0);

    @DatabaseField(id = true)
    private int id;

    @DatabaseField
    private String name;


    @DatabaseField (dataType = DataType.SERIALIZABLE)
    private ConcurrentHashMap<Integer,GrantingAgreement> malshab2granting; //Id->agreement

    @ForeignCollectionField(eager = true)
    private ForeignCollection<ProductInStore> products = null;

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private ArrayList<Integer> managerIds;
    private List<Subscriber> managers;

    @ForeignCollectionField(eager = true)
    private ForeignCollection<PurchaseDetails> purchaseHistory = null;

    @DatabaseField (foreign = true)
    private PurchaseDetails lastAddedPurchaseHistoryItem = null;

    @DatabaseField (foreign = true)
    private BuyingPolicy buyingPolicy;

    @DatabaseField (foreign = true)
    private DiscountPolicy discountPolicy;


    @DatabaseField
    private double rating = -1;

    public Store() {}


    public Store(int dummyArg){
        this.id = globalId.incrementAndGet();
        this.name = "";
        managers = new ArrayList<>();
        managerIds = new ArrayList<>();
        DAOManager.createProductInStoreListForStore(this);
        buyingPolicy = new BuyingPolicy("None");
        DAOManager.buyingPolicyDaoCreateOrUpdate(buyingPolicy);
        discountPolicy = new DiscountPolicy("None");
        DAOManager.discountPolicyDaoCreateOrUpdate(discountPolicy);

        DAOManager.createPurchaseHistoryForStore(this);
        malshab2granting = new ConcurrentHashMap<>();
        //storePurchaseHistory = new StorePurchaseHistory(this);
    }

    public int getId() {
        return id;
    }

    public ActionResultDTO addProduct(ProductInfo info, int amount) {
        if (info == null) return new ActionResultDTO(ResultCode.ERROR_STORE_PRODUCT_MODIFICATION, "Invalid product.");
        if (amount < 1) return new ActionResultDTO(ResultCode.ERROR_STORE_PRODUCT_MODIFICATION, "Amount must be positive.");
        AtomicBoolean found = new AtomicBoolean(false);
        for(ProductInStore p : products){
            if(p.getProductInfoId() == info.getId()){
                p.addAmount(amount);
                found.set(true);
            }
        }

        if(!found.get()){
            ProductInStore newProduct = null;
            try {
                newProduct = new ProductInStore(info, amount, this);
            } catch (Exception e) {
                e.printStackTrace();
                return new ActionResultDTO(ResultCode.ERROR_STORE_PRODUCT_MODIFICATION, "Error while creating ProductInStore");
            }
            products.add(newProduct);
        }
        DAOManager.updateStore(this);

        return new ActionResultDTO(ResultCode.SUCCESS, null);
    }

    public ActionResultDTO editProduct(int productId, String info) {
        if(info!=null) {
            for (ProductInStore product : products) {
                if (product.getProductInfoId() == productId) {
                    synchronized (product) {
                        product.editInfo(info);
                    }
                    return new ActionResultDTO(ResultCode.SUCCESS, null);
                }
            }
        }
        DAOManager.updateStore(this);
        return new ActionResultDTO(ResultCode.ERROR_STORE_PRODUCT_MODIFICATION, "Invalid product.");

    }


    public ActionResultDTO deleteProduct(int productId) {
        for (ProductInStore product: products){
            if (product.getProductInfoId() == productId){
                synchronized (product) {
                    products.remove(product);
                }
                return new ActionResultDTO(ResultCode.SUCCESS, null);
            }
        }
        DAOManager.updateStore(this);
        return new ActionResultDTO(ResultCode.ERROR_STORE_PRODUCT_MODIFICATION, "Invalid product.");
    }

    public List<Subscriber> getOwners() {
        List <Subscriber> owners = new LinkedList<Subscriber>();
        for (Subscriber manager: managers){
            if (manager.hasOwnerPermission(this.getId()))
                owners.add(manager);
        }
        return owners;
    }

    public void addOwner(Subscriber newOwner) {
        if(newOwner != null) {
            managers.add(newOwner);
            managerIds.add(newOwner.getId());
            DAOManager.updateStore(this);
        }
    }

    public List<Subscriber> getManagers() {
        List <Subscriber> managers_ = new LinkedList<Subscriber>();
        for (Subscriber manager: managers){
            if (manager.hasManagerPermission(this.getId()))
                managers_.add(manager);
        }
        return managers_;
    }
    public List<PurchaseDetails> getStorePurchaseHistory(){
        return new ArrayList<>(purchaseHistory);
    }

    public void setBuyingPolicy(BuyingPolicy policy) {
        if(policy!= null)
            this.buyingPolicy = policy;
        DAOManager.updateStore(this);
    }

    public void setDiscountPolicy(DiscountPolicy policy) {
        if (policy != null)
            this.discountPolicy = policy;
        DAOManager.updateStore(this);
    }

    public ActionResultDTO checkPurchaseValidity(User user, ShoppingBasket basket) {
        return buyingPolicy.isAllowed(user, basket);
    }

    public PurchaseDetails savePurchase(User user, HashMap<ProductInfo, Integer> products) {
        double totalPrice = getPrice(user, products).getPrice();

        HashMap<ProductInfo, Integer> productInfoIntegerMap = new HashMap<>();
        // get the ProductInfo -> integer map
        for (ProductInfo product : products.keySet()) {
            productInfoIntegerMap.put(product, products.get(product));
        }
        PurchaseDetails details = addPurchase(user, productInfoIntegerMap, totalPrice);
        return details;
    }

    public PurchaseDetails addPurchase(User user, HashMap<ProductInfo, Integer> products, double price) {

        synchronized (System.purchaseLock) {
            PurchaseDetails details = new PurchaseDetails(user, this, products, price);
            purchaseHistory.add(details);
            lastAddedPurchaseHistoryItem = details;
            DAOManager.createPurchaseDetails(details);
            DAOManager.updateStore(this);
            return details;
        }

    }

    private ProductInfo getProductInfoByProductId(int productId) {
        for (ProductInStore pis : products) {
            if (pis.getProductInfoId() == productId) return pis.getProductInfo();
        }
        return null;
    }

    public void cancelPurchase(PurchaseDetails purchaseDetails) {
        synchronized (System.purchaseLock) {
            purchaseHistory.remove(purchaseDetails);
            DAOManager.updateStore(this);
        }
    }

    public int getProductAmount(Integer productId) {
        for (ProductInStore product : products) {
            if (productId == product.getProductInfoId()) {
                return product.getAmount();
            }
        }
        return 0;
    }

    public boolean setProductAmount(Integer productId, int amount) throws DatabaseFetchException {
        synchronized (System.storesLock) {
            if (amount == 0) products.removeIf(pis -> pis.getProductInfoId() == productId);
            else if (amount > 0) {
                boolean productExists = false;
                for (ProductInStore pis : products) {
                    if (productId == pis.getProductInfoId()) {
                        pis.setAmount(amount);
                        productExists = true;
                        break;
                    }
                }
                if (!productExists) addProduct(System.getInstance().getProductInfoById(productId), amount);
            } else
                return false;

            DAOManager.updateStore(this);
            return true;
        }
    }

    public ProductInStore getProductInStoreById(int id) {
        for (ProductInStore pis: products) {
            if (pis.getProductInfoId() == id)
                return pis;
        }
        return null;
    }

    public String toString() {
        String info =
                "Store ID: " + id +
                "\nBuying policy: " + buyingPolicy.toString() +
                "\nDiscount policy: " + discountPolicy.toString() +
                "\nProducts:\n\n";

        for (ProductInStore product: products) {
            info += product.toString() + "\n";
        }

        return info;
    }

    public List<ProductInStore> getProducts() {
        return new ArrayList<>(products);
    }

    public double getRating() {
        return rating;
    }


    public void setRating(double rating) {
        this.rating = rating;
        DAOManager.updateStore(this);
    }

    public void removeProductAmount(Integer productId, Integer amount) {
        synchronized (System.storesLock) {
            if (amount < 0) return;
            for (ProductInStore product : products) {
                int id = product.getProductInfoId();
                if (productId == id) {
                    int newAmount = product.getAmount() - amount;
                    if (newAmount >= 0) {
                        if (newAmount == 0) {
                            products.remove(product);
                        } else {
                            product.setAmount(newAmount);
                        }
                    }
                }
            }
            DAOManager.updateStore(this);
        }
    }

    public void removeManger(Subscriber managerToDelete) {
        if(managerToDelete!= null && managers.size() > 1){
            for(Subscriber s : managers){
                if (s.getId() == managerToDelete.getId()) {
                    managers.remove(managerToDelete);
                    managerIds.remove((Integer) managerToDelete.getId());
                    break;
                }
            }
            for(Integer malshabId : malshab2granting.keySet()){
                GrantingAgreement agreement = malshab2granting.get(malshabId);
                if(agreement.getGrantorId()==managerToDelete.getId()){
                    malshab2granting.remove(malshabId);
                    continue;
                }
                else{
                    agreement.removeApprove(managerToDelete.getId());
                }
            }
        }
        DAOManager.updateStore(this);
    }

    //for Testing reasons
    public void clean() {
        managers.clear();
        managerIds.clear();
        products.clear();
        DAOManager.updateStore(this);
    }

    public BuyingPolicy getBuyingPolicy() {
        return buyingPolicy;
    }

    public DiscountPolicy getDiscountPolicy() {
        return discountPolicy;
    }

    public List<Subscriber> getAllManagers(){
        return managers;
    }

    public DoubleActionResultDTO getPrice(User user, HashMap<ProductInfo, Integer> products) {
        ShoppingBasket basket = new ShoppingBasket(this);
        basket.setProducts(products);

        return new DoubleActionResultDTO(ResultCode.SUCCESS, "get price", discountPolicy.getBasketDiscountedPrice(user, basket));
    }

    public void removeLastHistoryItem() {
        purchaseHistory.remove(lastAddedPurchaseHistoryItem);
        DAOManager.updateStore(this);
    }

    public void setProductPrice(int id, double price) {
        for (ProductInStore productInStore: products) {
            if (productInStore.getProductInfo().getId() == id) {
                productInStore.setPrice(price);
                return;
            }
        }
        DAOManager.updateStore(this);
    }

    public double getProductPrice(int productId) {
        for (ProductInStore productInStore: products) {
            if (productInStore.getProductInfo().getId() == productId) {
                return productInStore.getPrice();
            }
        }
        return -1;
    }

    public int addSimpleBuyingTypeBasketConstraint(ProductInfo productInfo, String minmax, int amount) {
        if (productInfo == null) {
            if (minmax.equals("max")) return buyingPolicy.addSimpleBuyingType(new BasketBuyingConstraint.MaxProductAmountConstraint(amount));
            else return buyingPolicy.addSimpleBuyingType(new BasketBuyingConstraint.MinProductAmountConstraint(amount));
        }  else {
            if (minmax.equals("max")) return buyingPolicy.addSimpleBuyingType(new BasketBuyingConstraint.MaxAmountForProductConstraint(productInfo, amount));
            else return buyingPolicy.addSimpleBuyingType(new BasketBuyingConstraint.MinAmountForProductConstraint(productInfo, amount));
        }
    }

    public int addSimpleBuyingTypeUserConstraint(String country) {
        return buyingPolicy.addSimpleBuyingType(new UserBuyingConstraint.NotOutsideCountryConstraint(country));
    }

    public int addSimpleBuyingTypeSystemConstraint(int dayOfWeek) {
        return buyingPolicy.addSimpleBuyingType(new SystemBuyingConstraint.NotOnDayConstraint(dayOfWeek));
    }

    public void removeBuyingType(int buyingTypeID) {
        buyingPolicy.removeBuyingType(buyingTypeID);
    }

    public void removeAllBuyingTypes() {
        buyingPolicy.clearBuyingTypes();
    }

    public IntActionResultDto addAdvancedBuyingType(List<Integer> buyingTypeIDs, String logicalOperation) {
        return buyingPolicy.createAdvancedBuyingTypeFromExisting(buyingTypeIDs, logicalOperation);
    }

    public BuyingPolicyActionResultDTO getBuyingPolicyDetails() {
        return buyingPolicy.getDTO();
    }

    public DiscountPolicyActionResultDTO getDiscountPolicyDetails() {
        return discountPolicy.getDTO();
    }

    public boolean hasProduct(ProductInfo productInfo) {
        int productId = productInfo.getId();
        for (ProductInStore pis : products) {
            if (pis.getProductInfoId() == productId) return true;
        }
        return false;
    }


    public int addSimpleProductDiscount(int productId, double salePercentage) {
        return discountPolicy.addSimpleDiscountType(new ProductDiscount.ProductSaleDiscount(productId, salePercentage));
    }

    public int addSimpleCategoryDiscount(String categoryName, double salePercentage) {
        return discountPolicy.addSimpleDiscountType(new ProductDiscount.CategorySaleDiscount(categoryName, salePercentage));
    }

    public IntActionResultDto addAdvancedDiscountType(List<Integer> discountTypeIDs, String logicalOperation) {
        return discountPolicy.createAdvancedDiscountTypeFromExisting(discountTypeIDs, logicalOperation);
    }

    public void removeDiscountType(int discountTypeID) {
        discountPolicy.removeDiscountType(discountTypeID);
    }

    public void removeAllDiscountTypes() {
        discountPolicy.clearDiscountTypes();
    }

    public String getProductInStoreInfo(int id) {
        for (ProductInStore pis : products) {
            if (pis.getProductInfoId() == id) return pis.getInfo();
        }
        return null;
    }

    public boolean equals(Object other) {
        if (other instanceof Store) return ((Store) other).getId() == id;
        return false;
    }

    public int hashCode() {
        return id;
    }

    public List<Integer> getManagerIds() {
        return managerIds;
    }

    public void setManagers(List<Subscriber> managers) {
        this.managers = managers;
    }
    /*
        The function recieves a subscriber and return list of store managers who granted by that subscriber
     */
    public List<Subscriber> getAllGrantedBy(Subscriber subscriber) {
        List<Subscriber> result = new ArrayList<>();
        for(Subscriber manager : getAllManagers()){
           if( manager.isGrantedBy(getId(),subscriber.getId()))
            result.add(manager);
        }
        return result;
    }

    public void setName(String name) {
        this.name = name;
        DAOManager.updateStore(this);
    }

    public String getName() {
        return name;
    }

    public boolean addAgreement(GrantingAgreement agreement) {
        int id =agreement.getMalshabId();
        if(malshab2granting.get(id) == null) {
            malshab2granting.put(agreement.getMalshabId(), agreement);
            DAOManager.updateStore(this);
            return true;
        }
        return false;
    }


    public boolean approveMalshab(int grantorid, int malshabId) {
        GrantingAgreement agreement = malshab2granting.get(malshabId);
        if(agreement!=null){
           boolean success = agreement.approve(grantorid);
           DAOManager.updateStore(this);
           return success;
        }
        return false;

    }


    public int getApprovedAgreementGrantor(int malshabId) {
        GrantingAgreement agreement = malshab2granting.get(malshabId);
        if(agreement!=null && agreement.allAproved()){
            return agreement.getGrantorId();
        }
        return -1;

    }


    public void removeAgreement(int subId) {
        malshab2granting.remove(subId);
        DAOManager.updateStore(this);
    }

    public Collection<GrantingAgreement> getAllGrantingAgreements(){
        return malshab2granting.values();
    }


    public List<GrantingAgreement> getAgreementsOf(int subId) {
        List<GrantingAgreement> agreements = new ArrayList<>();
        for(GrantingAgreement agreement : getAllGrantingAgreements()){
            if(agreement.getGrantorId()==subId || agreement.getOwner2approve().keySet().contains(subId))
                agreements.add(agreement);
        }
        return agreements;
    }

    /**
     * checks if there is grantingAgreement for subId and grantorID is in the agreement.
     */
    public boolean agreementExist(int grantorId, int subId) {
        GrantingAgreement agreement = malshab2granting.get(subId);
        if(agreement!=null){
            return agreement.hasApprove(grantorId);
        }
        return false;
    }

    public int getAgreementGrantor(int malshabId) {
        GrantingAgreement agreement = malshab2granting.get(malshabId);
        if(agreement!=null){
            return agreement.getGrantorId();
        }
        return -1;
    }

    public void handleGrantingAgreement() {
        Collection<GrantingAgreement> agreements = getAllGrantingAgreements();
        for (GrantingAgreement agreement : agreements){
            if (agreement.allAproved()){
                Subscriber grantor = System.getInstance().getSubscriber(agreement.getGrantorId());
                Subscriber newOwner = System.getInstance().getSubscriber(agreement.getMalshabId());
                if (System.getInstance().setStoreOwner(grantor, newOwner, this)) {
                    removeAgreement(agreement.getMalshabId());
                }

            }
        }
    }


}
