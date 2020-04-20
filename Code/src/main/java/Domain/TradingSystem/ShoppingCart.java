package Domain.TradingSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingCart {

    private User user;
    private ArrayList<ShoppingBasket> shoppingBaskets = new ArrayList<ShoppingBasket>();

    /**
     *
     * Functions For Usecases 2.6, 2.7.*
     *
     */

    public boolean addProduct(Store store, int productId, int amount) {
        if (amount < 1) return false;
        getOrCreateBasket(store).addProduct(productId, amount);
        return true;
    }

    public boolean editProduct(Store store, int productId, int newAmount) {
        if (newAmount < 0) return false;

        ShoppingBasket basket = getBasket(store);
        if (basket == null) {
            //TODO: error, no such basket
            return false;
        } else {
            basket.editProduct(productId, newAmount);
            return true;
        }
    }

    public boolean removeProductFromCart(Store store, int productId) {
        ShoppingBasket basket = getBasket(store);
        if (basket == null) {
            //TODO: error, no such basket
            return false;
        } else {
            return basket.removeProduct(productId);
        }
    }

    public void removeAllProducts() {
        shoppingBaskets.clear();
    }

    public boolean attemptPurchase(User user) {
        double totalPrice = 0;
        for (ShoppingBasket basket : shoppingBaskets) {
            if (!basket.checkBuyingPolicy(user)) {
                // TODO: message the user with an error
                return false;
            }
            double basketPrice = basket.getTotalPrice(user);
            totalPrice += basketPrice;
        }
        if (user.confirmPrice(totalPrice)) {
            return user.requestConfirmedPurchase();
        } else {
            return false;
        }
    }

    public Map<Integer, Map<Integer, Integer>> getStoreProductsIds() {
        Map<Integer, Map<Integer, Integer>> storeProductsIds = new HashMap<>();
        for (ShoppingBasket basket : shoppingBaskets) {
            storeProductsIds.put(basket.getStoreId(), basket.getProducts());
        }
        return storeProductsIds;
    }

    public Map<Integer, PurchaseDetails> savePurchase(User user) {
        Map<Integer, PurchaseDetails> storePurchaseDetails = new HashMap<>();
        for (ShoppingBasket basket : shoppingBaskets) {
            PurchaseDetails details = basket.savePurchase(user);
            storePurchaseDetails.put(basket.getStoreId(), details);
        }
        return storePurchaseDetails;
    }

    public void cancelPurchase(Map<Integer, PurchaseDetails> storePurchaseDetails) {
        for (Integer storeId : storePurchaseDetails.keySet()) {
            for (ShoppingBasket basket : shoppingBaskets) {
                if (basket.getStoreId() == storeId) {
                    basket.cancelPurchase(storePurchaseDetails.get(storeId));
                    break;
                }
            }
        }
    }

    private ShoppingBasket getBasket(Store store) {
        for (ShoppingBasket basket : shoppingBaskets) {
            if (basket.getStoreId() == store.getId()) {
                return basket;
            }
        }
        return null;
    }

    private ShoppingBasket getOrCreateBasket(Store store) {
        for (ShoppingBasket basket : shoppingBaskets) {
            if (store.getId() == basket.getStoreId()) {
                return basket;
            }
        }
        ShoppingBasket newBasket = new ShoppingBasket(store);
        shoppingBaskets.add(newBasket);
        return newBasket;
    }

    public void merge(ShoppingCart other) {
        List<Integer> existingStoreIDs = new ArrayList<>();
        for (ShoppingBasket basket : shoppingBaskets) {
            existingStoreIDs.add(basket.getStoreId());
            for (ShoppingBasket otherBasket : other.shoppingBaskets) {
                if (otherBasket.getStoreId() == basket.getStoreId()) {
                    basket.merge(otherBasket);
                    break;
                }
            }
        }

        for (ShoppingBasket otherBasket : other.shoppingBaskets) {
            if(!existingStoreIDs.contains(otherBasket.getStoreId())) {
                shoppingBaskets.add(otherBasket);
            }
        }

    }


    public ArrayList<ShoppingBasket> getBaskets(){
        return shoppingBaskets;
    }
}
