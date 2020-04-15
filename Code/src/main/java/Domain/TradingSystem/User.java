package Domain.TradingSystem;

import java.util.HashMap;
import java.util.Map;

public class User {

    private System system = System.getInstance();

    private Permission permissions;
    private UserState state;
    private ShoppingCart shoppingCart;
    private PurchaseHistory purchaseHistory;

    public boolean addProductToStore(int storeId, int productId, int ammount) {
        Store currStore = permissions.hasPermission(storeId,"Owner");
        if(currStore != null){
            return state.addProductToStore(currStore,productId,ammount);
        }
        return false;
    }

    public void addProductToCart(Store store, int productId, int amount) {
        shoppingCart.addProduct(store, productId, amount);
    }

    public void editCartProductAmount(Store store, int productId, int newAmount) {
        shoppingCart.editProduct(store, productId, newAmount);
    }

    public void removeProductFromCart(Store store, int productId) {
        shoppingCart.removeProductFromCart(store, productId);
    }

    public void removeAllProductsFromCart() {
        shoppingCart.removeAllProducts();
    }

    public void purchaseCart() {
        shoppingCart.attemptPurchase(this);
    }

    public boolean confirmPrice(double totalPrice) {
        // TODO: query user to confirm the price
        return true;
    }



    public void requestConfirmedPurchase() { // from payment system
        if (!system.makePayment(this, shoppingCart.getStoreProductsIds())) {
            // TODO: message user with an error
        }
        Map<Integer, PurchaseDetails> storePurchaseDetails = shoppingCart.savePurchase(this); // store purchase history
        purchaseHistory.addPurchase(storePurchaseDetails); // user purchase history
        boolean supplyAvailable = system.requestSupply(this, shoppingCart.getStoreProductsIds());
        if (supplyAvailable) {
            shoppingCart.removeAllProducts();

            // TODO: message user with success

        } else {
            system.cancelPayment(this, shoppingCart.getStoreProductsIds());
            shoppingCart.cancelPurchase(storePurchaseDetails); // remove from store purchase history
            purchaseHistory.removePurchase(storePurchaseDetails); // remove from user purchase history

            // TODO: message user with fail and refund
        }
    }

    public boolean isGuest() {
        return state instanceof Guest;
    }
}
