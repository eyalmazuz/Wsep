package Domain.TradingSystem;

import DTOs.ActionResultDTO;
import DTOs.ResultCode;

import java.util.HashMap;
import java.util.Map;

public class ShoppingBasket {
    private Store store;

    private Map<ProductInfo, Integer> products = new HashMap<>();

    public ShoppingBasket(Store store) {
        this.store = store;
    }

    // usecases 2.6, 2.7

    public void addProduct(ProductInfo product, int amount) {
        products.put(product, products.getOrDefault(product, 0) + amount);
    }

    public Store getStore() {
        return store;
    }

    public int getStoreId() {
        return store.getId();
    }

    public ActionResultDTO editProduct(ProductInfo product, int newAmount) {
        if (!products.containsKey(product)) return new ActionResultDTO(ResultCode.ERROR_CART_MODIFICATION, "Basket does not have this product.");
        products.put(product, newAmount);
        return new ActionResultDTO(ResultCode.SUCCESS, null);
    }

    public ActionResultDTO removeProduct(ProductInfo product) {
        if (!products.containsKey(product)) return new ActionResultDTO(ResultCode.ERROR_CART_MODIFICATION, "Basket does not have this product.");
        products.remove(product);
        return new ActionResultDTO(ResultCode.SUCCESS, null);
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

    public Map<ProductInfo, Integer> getProducts() {
        return products;
    }

    public void merge(ShoppingBasket otherBasket) {
        for (ProductInfo product : otherBasket.products.keySet()) {
            int amount = otherBasket.getProducts().get(product);
            products.put(product, products.getOrDefault(product, 0) + amount);
        }
    }

    @Override
    public String toString() {
        String output = "";
        for (ProductInfo product : products.keySet()) {
            int amount = products.get(product);
            output += "Product ID: " + product.getId() + ", amount: " + amount + "\n";
        }
        return output;
    }

    public boolean checkStoreSupplies() {
        for (ProductInfo product : products.keySet()) {
            if (store.getProductAmount(product.getId()) < products.get(product)) return false;
        }
        return true;
    }

    public boolean updateStoreSupplies() {
        for (ProductInfo product : products.keySet()) {
            if (! store.setProductAmount(product.getId(), store.getProductAmount(product.getId()) - products.get(product)))
                return false;
        }
        return true;
    }
    public void restoreStoreSupplies() {
        for (ProductInfo product : products.keySet()) {
            store.setProductAmount(product.getId(), store.getProductAmount(product.getId()) + products.get(product));
        }
    }
}
