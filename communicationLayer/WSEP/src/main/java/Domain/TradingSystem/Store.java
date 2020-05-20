package Domain.TradingSystem;

import DTOs.ActionResultDTO;
import DTOs.DoubleActionResultDTO;
import DTOs.IntActionResultDto;
import DTOs.ResultCode;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;


public class Store {

    private static int globalId = 0;

    private int id;
    private List<ProductInStore> products;
    private List <Subscriber> managers;

    private List<PurchaseDetails> purchaseHistory;

    private BuyingPolicy buyingPolicy;
    private DiscountPolicy discountPolicy;
    private int nextPurchaseId = 0;
    private double rating = -1;



    public Store(){
        this.id = globalId;
        globalId ++;
        // FIX for acceptance tests
        managers = new LinkedList<>();
        products = new LinkedList<>();
        buyingPolicy = new BuyingPolicy("None");
        discountPolicy = new DiscountPolicy();
        purchaseHistory = new ArrayList<>();
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
            if(p.getId() == info.getId()){
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

        return new ActionResultDTO(ResultCode.SUCCESS, null);
    }

    public ActionResultDTO editProduct(int productId, String info) {
        if(info!=null) {
            for (ProductInStore product : products) {
                if (product.getId() == productId) {
                    synchronized (product) {
                        product.editInfo(info);
                    }
                    return new ActionResultDTO(ResultCode.SUCCESS, null);
                }
            }
        }
        return new ActionResultDTO(ResultCode.ERROR_STORE_PRODUCT_MODIFICATION, "Invalid product.");

    }


    public ActionResultDTO deleteProduct(int productId) {
        for (ProductInStore product: products){
            if (product.getId() == productId){
                synchronized (product) {
                    products.remove(product);
                }
                return new ActionResultDTO(ResultCode.SUCCESS, null);
            }
        }
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
        return purchaseHistory;
    }

    public void setBuyingPolicy(BuyingPolicy policy) {
        if(policy!= null)
            this.buyingPolicy = policy;
    }

    public void setDiscountPolicy(DiscountPolicy policy) {
        if (policy != null)
            this.discountPolicy = policy;
    }

    public ActionResultDTO checkPurchaseValidity(User user, ShoppingBasket basket) {
        return buyingPolicy.isAllowed(user, basket);
    }

    public PurchaseDetails savePurchase(User user, Map<ProductInfo, Integer> products) {
        double totalPrice = getPrice(user, products).getPrice();

        Map<ProductInfo, Integer> productInfoIntegerMap = new HashMap<>();
        // get the ProductInfo -> integer map
        for (ProductInfo product : products.keySet()) {
            productInfoIntegerMap.put(product, products.get(product));
        }
        PurchaseDetails details = addPurchase(nextPurchaseId, user, productInfoIntegerMap, totalPrice);
        nextPurchaseId++;
        return details;
    }

    public PurchaseDetails addPurchase(int purchaseId, User user, Map<ProductInfo, Integer> products, double price) {
        PurchaseDetails details = new PurchaseDetails(purchaseId, user, this, products, price);
        purchaseHistory.add(details);
        return details;
    }

    private ProductInfo getProductInfoByProductId(int productId) {
        for (ProductInStore pis : products) {
            if (pis.getId() == productId) return pis.getProductInfo();
        }
        return null;
    }

    public void cancelPurchase(PurchaseDetails purchaseDetails) {
        purchaseHistory.remove(purchaseDetails);
    }

    public int getProductAmount(Integer productId) {
        for (ProductInStore product : products) {
            if (productId == product.getId()) {
                return product.getAmount();
            }
        }
        return 0;
    }

    public boolean setProductAmount(Integer productId, int amount) {
        synchronized (products) {
            if (amount == 0) products.removeIf(pis -> pis.getId() == productId);
            else if (amount>0) {
                boolean productExists = false;
                for (ProductInStore pis : products) {
                    if (productId == pis.getId()) {
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
        return true;
    }

    public ProductInStore getProductInStoreById(int id) {
        for (ProductInStore pis: products) {
            if (pis.getId() == id)
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
        return products;
    }

    public double getRating() {
        return rating;
    }

    public void setProducts(List<ProductInStore> products) {
        this.products = products;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void removeProductAmount(Integer productId, Integer amount) {
        if (amount < 0) return;
        for (ProductInStore product : products) {
            int id = product.getId();
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
    }

    //for Testing reasons
    public void clean() {
        managers.clear();
        products.clear();
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

    public DoubleActionResultDTO getPrice(User user, Map<ProductInfo, Integer> products) {
        ShoppingBasket basket = new ShoppingBasket(this);
        basket.setProducts(products);

        return new DoubleActionResultDTO(ResultCode.SUCCESS, "get price", discountPolicy.getBasketDiscountedPrice(user, basket));
    }

    public void removeLastHistoryItem() {
        purchaseHistory.remove(purchaseHistory.size() - 1);
    }

    public void setProductPrice(int id, int price) {
        for (ProductInStore productInStore: products) {
            if (productInStore.getProductInfo().getId() == id) {
                productInStore.setPrice(price);
                return;
            }
        }
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
            if (minmax.equals("max")) return buyingPolicy.addBuyingType(new BasketBuyingConstraint.MaxProductAmountConstraint(amount));
            else return buyingPolicy.addBuyingType(new BasketBuyingConstraint.MinProductAmountConstraint(amount));
        }  else {
            if (minmax.equals("max")) return buyingPolicy.addBuyingType(new BasketBuyingConstraint.MaxAmountForProductConstraint(productInfo, amount));
            else return buyingPolicy.addBuyingType(new BasketBuyingConstraint.MinAmountForProductConstraint(productInfo, amount));
        }
    }

    public int addSimpleBuyingTypeUserConstraint(String country) {
        return buyingPolicy.addBuyingType(new UserBuyingConstraint.NotOutsideCountryConstraint(country));
    }

    public int addSimpleBuyingTypeSystemConstraint(int dayOfWeek) {
        return buyingPolicy.addBuyingType(new SystemBuyingConstraint.NotOnDayConstraint(dayOfWeek));
    }

    public void removeBuyingType(int buyingTypeID) {
        buyingPolicy.removeBuyingType(buyingTypeID);
    }

    public void removeAllBuyingTypes() {
        buyingPolicy.clearBuyingTypes();
    }

    public IntActionResultDto addAdvancedBuyingType(List<Integer> buyingTypeIDs, String logicalOperation) {
        return buyingPolicy.addAdvancedBuyingType(buyingTypeIDs, logicalOperation);
    }

    public String getBuyingPolicyDetails() {
        return buyingPolicy.toString();
    }
}
