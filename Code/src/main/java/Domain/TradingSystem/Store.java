package Domain.TradingSystem;

import DTOs.ActionResultDTO;
import DTOs.ResultCode;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;



public class Store {

    private static int globalId = 0;

    private int id;
    private List<ProductInStore> products;
    private List <Subscriber> managers;
    private StorePurchaseHistory storePurchaseHistory;
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
        storePurchaseHistory = new StorePurchaseHistory(this);
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
    public StorePurchaseHistory getStorePurchaseHistory(){
        return storePurchaseHistory;
    }

    public void setBuyingPolicy(BuyingPolicy policy) {
        if(policy!= null)
            this.buyingPolicy = policy;
    }

    public void setDiscountPolicy(DiscountPolicy policy) {
        if(policy!= null)
            this.discountPolicy = policy;
    }

    public boolean checkPurchaseValidity(User user, Map<Integer, Integer> productAmounts) {
        return buyingPolicy.isAllowed(user, productAmounts);
    }

    public PurchaseDetails savePurchase(User user, Map<Integer, Integer> products) {
        double totalPrice = getPrice(user, products);

        Map<ProductInfo, Integer> productInfoIntegerMap = new HashMap<>();
        // get the ProductInfo -> integer map
        for (Integer productId : products.keySet()) {
            productInfoIntegerMap.put(getProductInfoByProductId(productId), products.get(productId));
        }
        PurchaseDetails details = storePurchaseHistory.addPurchase(nextPurchaseId, user, productInfoIntegerMap, totalPrice);
        nextPurchaseId++;
        return details;
    }

    private ProductInfo getProductInfoByProductId(int productId) {
        for (ProductInStore pis : products) {
            if (pis.getId() == productId) return pis.getProductInfo();
        }
        return null;
    }

    public void cancelPurchase(PurchaseDetails purchaseDetails) {
        storePurchaseHistory.removePurchase(purchaseDetails);
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

    public double getPrice(User user, Map<Integer, Integer> products) {
        return discountPolicy.getProductPrice(user, products);
    }

    public void removeLastHistoryItem() {
        storePurchaseHistory.removeLastItem();
    }
}
