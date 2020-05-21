package Domain.TradingSystem;

public class ProductInStore {

    private ProductInfo productInfo;
    private int amount;
    private String info;
    private Store store;
    private double price;


    public ProductInStore(ProductInfo productInfo, int amount, Store store) {
        this.productInfo = productInfo;
        this.amount = amount;
        this.store = store;
        this.price = productInfo.getDefaultPrice();
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

    public Store getStore() {
        return store;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
