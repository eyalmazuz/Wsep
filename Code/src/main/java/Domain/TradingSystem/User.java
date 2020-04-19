package Domain.TradingSystem;

import java.util.HashMap;
import java.util.Map;

public class User {

    private System system = System.getInstance();

    private Permission permissions;
    private UserState state;
    private ShoppingCart shoppingCart;
    private PurchaseHistory purchaseHistory;

    public User() {
        this.state = new Guest();
    }

    public boolean addProductToStore(int storeId, int productId, int amount) {
        return state.addProductToStore(storeId, productId, amount);

    }

    public boolean editProductInStore(int storeId, int productId, String newInfo) {

        return state.editProductInStore(storeId, productId, newInfo);
    }

    public boolean deleteProductFromStore(int storeId, int productId) {

        return state.deleteProductFromStore(storeId, productId);

    }


    public UserState getState() {
        return state;
    }

    public void setState(UserState nState) {
        this.state = nState;
        state.setUser(this);
    }


    public boolean logout() {
        return state.logout(shoppingCart);
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
        state.addPurchase(storePurchaseDetails);
        boolean supplyAvailable = system.requestSupply(this, shoppingCart.getStoreProductsIds());
        if (supplyAvailable) {
            shoppingCart.removeAllProducts();
            // TODO: message user with success
        }
        else{
            system.cancelPayment(this, shoppingCart.getStoreProductsIds());
            shoppingCart.cancelPurchase(storePurchaseDetails); // remove from store purchase history
            state.removePurchase(storePurchaseDetails); // remove from user purchase history


            // TODO: message user with fail and refund
        }
    }
    public Store openStore () {
        return state.openStore();
    }

    public boolean addOwner (Store store, Subscriber newOwner){
        return state.addOwner(store, newOwner);

    }


    public boolean isGuest () {
        return state instanceof Guest;
    }

    public ShoppingCart getShoppingCart () {
        return shoppingCart;
    }

    public String getHistory () {
        return state.getHistory();
    }

    public boolean addManager (Store store, Subscriber newManager){
        return state.addManager(store, newManager);
    }

    public boolean deleteManager (Store store, Subscriber managerToDelete){
        return state.deleteManager(store, managerToDelete);
    }


    public String getStoreHistory ( int storeId){
        return state.getStoreHistory(storeId);
    }


    public boolean isAdmin() {
    return state.isAdmin();
    }
}

