package Domain.TradingSystem;

import java.util.HashMap;
import java.util.Map;

public class ShoppingBasket {
    private int storeId;

    private Map<Integer, Integer> products = new HashMap<>();

    public ShoppingBasket(int storeId) {
        this.storeId = storeId;
    }

    public void addProduct(int productId, int amount) {
        products.put(productId, products.getOrDefault(productId, 0) + amount);
    }

    public int getStoreId() {
        return storeId;
    }

    public void editProduct(int productId, int newAmount) {
        products.put(productId, newAmount);
    }

    public void removeProduct(int productId) {
        products.remove(productId);
    }
}
