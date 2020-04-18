package Domain.TradingSystem;

public class ProductInStore {

    private ProductInfo productInfo;
    private int amount;
    private Store store;

    public ProductInStore(int productId, int amount, Store store) {
        this.productInfo = new ProductInfo(productId);
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
        return "Product ID: " + productInfo.getId() + ", amount: " + amount;
    }

    public double getPrice(User user) {
        return store.getProductPrice(user, getId(), 1);
    }
}
