package Domain.TradingSystem;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {

    private ArrayList<ShoppingBasket> shoppingBaskets = new ArrayList<ShoppingBasket>();

    public void addProduct(int storeId, int productId, int amount) {
        getOrCreateBasket(storeId).addProduct(productId, amount);
    }

    public void editProduct(int storeId, int productId, int newAmount) {
        ShoppingBasket basket = getBasket(storeId);
        if (basket == null) {
            //TODO: error, no such basket
        } else {
            basket.editProduct(productId, newAmount);
        }
    }

    public void removeProductFromCart(int storeId, int productId) {
        ShoppingBasket basket = getBasket(storeId);
        if (basket == null) {
            //TODO: error, no such basket
        } else {
            basket.removeProduct(productId);
        }
    }

    public void removeAllProducts() {
        shoppingBaskets.clear();
    }

    private ShoppingBasket getBasket(int storeId) {
        for (ShoppingBasket basket : shoppingBaskets) {
            if (basket.getStoreId() == storeId) {
                return basket;
            }
        }
        return null;
    }

    private ShoppingBasket getOrCreateBasket(int storeId) {
        boolean basketExists = false;
        for (ShoppingBasket basket : shoppingBaskets) {
            if (storeId == basket.getStoreId()) {
                return basket;
            }
        }
        ShoppingBasket newBasket = new ShoppingBasket(storeId);
        shoppingBaskets.add(newBasket);
        return newBasket;
    }
}
