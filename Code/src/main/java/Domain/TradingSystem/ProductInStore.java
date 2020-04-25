package Domain.TradingSystem;

public class ProductInStore {

    private ProductInfo productInfo;
    private int amount;
    private String info;
    private Store store;
    private System system = System.getInstance();

    public ProductInStore(int productId, int amount, Store store) throws Exception {
        ProductInfo productInfo = system.getProductInfoById(productId);
        if (productInfo != null)
            this.productInfo = productInfo;
        else throw new Exception("No such product in the system");
        this.amount = amount;
        this.store = store;
    }

    public ProductInStore(ProductInfo productInfo, int amount, Store store) {
        this.productInfo = productInfo;
        this.amount = amount;
        this.store = store;
    }

    public int getId() {
        return productInfo.getId();
    }

    public int getAmount() {
        return amount;
    }

    public void addAmount(int amount) {
        this.amount += amount;
    }

    public String toString() {
        return "Store ID: " + store.getId() + ", product ID: " + productInfo.getId() + ", amount: " + amount + ", info: " + info;
    }

    public double getPrice(User user) {
        return store.getProductPrice(user, getId(), 1);
    }

    public String getInfo (){
        return this.info;
    }
    public void editInfo(String newInfo){
        this.info=newInfo;
    }

    public ProductInfo getProductInfo() {
        return productInfo;
    }
}
