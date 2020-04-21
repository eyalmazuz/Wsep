package Domain.TradingSystem;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;



public class Store {

    private static int globalId = 0;

    private int id;
    private List<ProductInStore> products;
    private List <Subscriber> managers;
    private History history;
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
        buyingPolicy = new BuyingPolicy();
        discountPolicy = new DiscountPolicy();
        history = new History();
    }

    public int getId() {
        return id;
    }

    public boolean addProduct(int productId, int amount) {
        if (productId < 0 || amount < 1) return false;
        AtomicBoolean found = new AtomicBoolean(false);
        for(ProductInStore p : products){
            if(p.getId() == productId){
                p.addAmount(amount);
                found.set(true);
            }
        }

        if(!found.get()){
            ProductInStore newProduct = new ProductInStore(productId,amount, this);
            products.add(newProduct);
        }
        return true;
    }

    public boolean editProduct(int productId, String info) {
        for (ProductInStore product:products){
            if (product.getId() == productId){
                product.editInfo(info);
                return true;
            }
        }
        return false;

    }


    public boolean deleteProduct(int productId) {
        for (ProductInStore product: products){
            if (product.getId() == productId){
                products.remove(product);
                return true;
            }
        }
        return false;
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
    public String getHistory(){
        return history.toString();
    }

    public void setBuyingPolicy(BuyingPolicy policy) {
        this.buyingPolicy = policy;
    }

    public void setDiscountPolicy(DiscountPolicy policy) {
        this.discountPolicy = policy;
    }

    public boolean checkPurchaseValidity(User user, int productId) {
        return buyingPolicy.isAllowed(user, productId);
    }

    public double getProductPrice(User user, int productId, int amount) {
        return discountPolicy.getProductPrice(user, productId, amount);
    }


    public PurchaseDetails savePurchase(User user, Map<Integer, Integer> products) {
        double totalPrice = 0;
        for (Integer productId : products.keySet()) {
            totalPrice += getProductPrice(user, productId, products.get(productId));
        }
        PurchaseDetails details = history.addPurchase(nextPurchaseId, user, products, totalPrice);
        nextPurchaseId++;
        return details;
    }

    public void cancelPurchase(PurchaseDetails purchaseDetails) {
        history.removePurchase(purchaseDetails);
    }

    public int getProductAmount(Integer productId) {
        for (ProductInStore product : products) {
            if (productId == product.getId()) {
                return product.getAmount();
            }
        }
        return 0;
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
}
