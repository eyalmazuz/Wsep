package Domain.TradingSystem;

public class ProductInStore {

    private ProductInfo productInfo;
    private int amount;
    private String info;
    private Store store;
    private System system = System.getInstance();


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

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void addAmount(int amount) {
        this.amount += amount;
    }

    public String toString() {
        return "Store ID: " + store.getId() + ", product ID: " + productInfo.getId() + ", amount: " + amount + ", info: " + info;
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
