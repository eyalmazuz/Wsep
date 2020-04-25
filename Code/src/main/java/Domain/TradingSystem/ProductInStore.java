package Domain.TradingSystem;

public class ProductInStore {

    private ProductInfo productInfo;
    private int amount;
    private String info;
    private Store store;

    public ProductInStore(int productId, int amount, Store store) {
        this.productInfo = new ProductInfo(productId);
        this.amount = amount;
        this.store = store;
    }


    public ProductInStore(int productId, int amount) {

         this.productInfo = new ProductInfo(productId);
         this.amount = amount;
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
}
