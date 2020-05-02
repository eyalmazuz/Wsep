package Domain.TradingSystem;

import java.util.HashMap;
import java.util.Map;

public class ShoppingBasket {
    private Store store;

    private Map<Integer, Integer> products = new HashMap<>();

    public ShoppingBasket(Store store) {
        this.store = store;
    }

    // usecases 2.6, 2.7

    public void addProduct(int productId, int amount) {
        products.put(productId, products.getOrDefault(productId, 0) + amount);
    }

    public Store getStore() {
        return store;
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

    // usecase 2.8.1
    public boolean checkBuyingPolicy(User user) {
        return store.checkPurchaseValidity(user, products);
    }

    public double getTotalPrice(User user) {
        return store.getPrice(user, products);
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

    @Override
    public String toString() {
        String output = "";
        for (Integer productId : products.keySet()) {
            int amount = products.get(productId);
            output += "Product ID: " + productId + ", amount: " + amount + "\n";
        }
        return output;
    }

    public boolean checkStoreSupplies() {
        for (Integer productId : products.keySet()) {
            if (store.getProductAmount(productId) < products.get(productId)) return false;
        }
        return true;
    }

    public boolean updateStoreSupplies() {
        for (Integer productId : products.keySet()) {
            if (! store.setProductAmount(productId, store.getProductAmount(productId) - products.get(productId)))
                return false;
        }
        return true;
    }
    public void restoreStoreSupplies() {
        for (Integer productId : products.keySet()) {
            store.setProductAmount(productId, store.getProductAmount(productId) + products.get(productId));
        }
    }
}
