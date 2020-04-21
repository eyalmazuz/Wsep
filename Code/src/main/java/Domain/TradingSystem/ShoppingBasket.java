package Domain.TradingSystem;

import java.util.HashMap;
import java.util.Map;

public class ShoppingBasket {
    private Store store;

    private Map<Integer, Integer> products = new HashMap<>();

    public ShoppingBasket(Store store) {
        this.store = store;
    }

    public void addProduct(int productId, int amount) {
        products.put(productId, products.getOrDefault(productId, 0) + amount);
    }

    public int getStoreId() {
        return store.getId();
    }

    public boolean editProduct(int productId, int newAmount) {
        if (!products.containsKey(productId)) return false;
        products.put(productId, newAmount);
        return true;
    }

    public boolean removeProduct(int productId) {
        if (products.containsKey(productId)) {
            products.remove(productId);
            return true;
        }
        return false;
    }

    public boolean checkBuyingPolicy(User user) {
        boolean allowed = true;
        for (Integer productId : products.keySet()) {
            allowed = allowed && store.checkPurchaseValidity(user, productId);
        }
        return allowed;
    }

    public double getTotalPrice(User user) {
        double totalPrice = 0;
        for (Integer productId : products.keySet()) {
            totalPrice += store.getProductPrice(user, productId, products.get(productId));
        }
        return totalPrice;
    }

    public PurchaseDetails savePurchase(User user) {
        return store.savePurchase(user, products);
    }

    public void cancelPurchase(PurchaseDetails purchaseDetails) {
        store.cancelPurchase(purchaseDetails);
    }

    public Map<Integer, Integer> getProducts() {
        return products;
    }

    public void merge(ShoppingBasket otherBasket) {
        for (Integer productId : otherBasket.products.keySet()) {
            int amount = otherBasket.getProducts().get(productId);
            products.put(productId, products.getOrDefault(productId, 0) + amount);
        }
    }
}
