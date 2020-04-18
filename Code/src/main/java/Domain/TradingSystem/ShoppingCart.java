package Domain.TradingSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShoppingCart {

    private User user;
    private ArrayList<ShoppingBasket> shoppingBaskets = new ArrayList<ShoppingBasket>();

    public void addProduct(Store store, int productId, int amount) {
        getOrCreateBasket(store).addProduct(productId, amount);
    }

    public void editProduct(Store store, int productId, int newAmount) {
        ShoppingBasket basket = getBasket(store);
        if (basket == null) {
            //TODO: error, no such basket
        } else {
            basket.editProduct(productId, newAmount);
        }
    }

    public void removeProductFromCart(Store store, int productId) {
        ShoppingBasket basket = getBasket(store);
        if (basket == null) {
            //TODO: error, no such basket
        } else {
            basket.removeProduct(productId);
        }
    }

    public void removeAllProducts() {
        shoppingBaskets.clear();
    }

    public void attemptPurchase(User user) {
        double totalPrice = 0;
        for (ShoppingBasket basket : shoppingBaskets) {
            if (!basket.checkBuyingPolicy(user)) {
                // TODO: message the user with an error
                return;
            }
            double basketPrice = basket.getTotalPrice(user);
            totalPrice += basketPrice;
        }
        if (user.confirmPrice(totalPrice)) {
            user.requestConfirmedPurchase();
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
        for (ShoppingBasket basket : shoppingBaskets) {
            for (ShoppingBasket otherBasket : other.shoppingBaskets) {
                if (otherBasket.getStoreId() == basket.getStoreId()) {
                    basket.merge(otherBasket);
                    break;
                }
            }
        }

    }


    /**
     * Clones the shoppingBaskets from cart onto this
     * @param cart
     */
    public void copyCart(ShoppingCart cart) {
        //TODO:Implement this
    }

    /**
     * Removes all products from current basket,activates when logging out.
     */
    public void cleanCart() {
        //TODO:Implemet this
    }

    public List<ShoppingBasket> getBaskets(){
        return shoppingBaskets;
    }
}
