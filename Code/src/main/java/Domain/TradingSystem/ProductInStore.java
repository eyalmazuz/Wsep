package Domain.TradingSystem;

public class ProductInStore {

    private ProductInfo productInfo;
    private int amount;

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
        return "Product ID: " + productInfo.getId() + ", amount: " + amount;
    }
}
