package Domain.TradingSystem;

import DTOs.*;
import DataAccess.DAOManager;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@DatabaseTable(tableName = "stores")
public class Store {

    private static int globalId = 0;

    @DatabaseField(id = true)
    private int id;

    @ForeignCollectionField(eager = true)
    private ForeignCollection<ProductInStore> products = null;

    // persist this
    private List<Subscriber> managers;

    @ForeignCollectionField(eager = true)
    private ForeignCollection<PurchaseDetails> purchaseHistory = null;

    private PurchaseDetails lastAddedPurchaseHistoryItem = null;

    @DatabaseField (foreign = true)
    private BuyingPolicy buyingPolicy;

    @DatabaseField (foreign = true)
    private DiscountPolicy discountPolicy;

    @DatabaseField
    private int nextPurchaseId = 0;

    @DatabaseField
    private double rating = -1;



    public Store(){
        this.id = globalId;
        globalId ++;
        managers = new LinkedList<>();
        DAOManager.createProductInStoreListForStore(this);
        buyingPolicy = new BuyingPolicy("None");
        discountPolicy = new DiscountPolicy("None");

        DAOManager.createPurchaseHistoryForStore(this);
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
        if(newOwner != null)
            managers.add(newOwner);
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
        PurchaseDetails details = addPurchase(nextPurchaseId, user, productInfoIntegerMap, totalPrice);
        nextPurchaseId++;
        return details;
    }

    public PurchaseDetails addPurchase(int purchaseId, User user, HashMap<ProductInfo, Integer> products, double price) {
        PurchaseDetails details = new PurchaseDetails(purchaseId, user, this, products, price);
        purchaseHistory.add(details);
        DAOManager.createPurchaseDetails(details);
        DAOManager.updateStore(this);
        lastAddedPurchaseHistoryItem = details;
        return details;
    }

    private ProductInfo getProductInfoByProductId(int productId) {
        for (ProductInStore pis : products) {
            if (pis.getProductInfoId() == productId) return pis.getProductInfo();
        }
        return null;
    }

    public void cancelPurchase(PurchaseDetails purchaseDetails) {
        purchaseHistory.remove(purchaseDetails);
        DAOManager.updateStore(this);
    }

    public int getProductAmount(Integer productId) {
        for (ProductInStore product : products) {
            if (productId == product.getProductInfoId()) {
                return product.getAmount();
            }
        }
        return 0;
    }

    public boolean setProductAmount(Integer productId, int amount) {
        synchronized (products) {
            if (amount == 0) products.removeIf(pis -> pis.getProductInfoId() == productId);
            else if (amount>0) {
                boolean productExists = false;
                for (ProductInStore pis : products) {
                    if (productId == pis.getProductInfoId()) {
                        pis.setAmount(amount);
                        productExists = true;
                        break;
                    }
                }
                if (!productExists) addProduct(System.getInstance().getProductInfoById(productId), amount);
            }
            else
                return false;
        }
        DAOManager.updateStore(this);
        return true;
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
        if (amount < 0) return;
        for (ProductInStore product : products) {
            int id = product.getProductInfoId();
            if (productId == id) {
                int newAmount = product.getAmount() - amount;
                if (newAmount>=0) {
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

    public void removeManger(Subscriber managerToDelete) {
        if(managerToDelete!= null && managers.size() > 1){
            for(Subscriber s : managers){
                if (s.getId() == managerToDelete.getId()) {
                    managers.remove(managerToDelete);
                    break;
                }
            }
        }
        DAOManager.updateStore(this);
    }

    //for Testing reasons
    public void clean() {
        managers.clear();
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
}
