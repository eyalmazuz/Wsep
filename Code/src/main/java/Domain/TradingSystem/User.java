package Domain.TradingSystem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {

    private System system = System.getInstance();


    private String paymentDetails;
    private Permission permissions;
    private UserState state;
    private ShoppingCart shoppingCart;
    private UserPurchaseHistory userPurchaseHistory;
    private static int idCounter = 0;
    private int id;

    public User() {
        this.id = idCounter;
        idCounter++;
        this.state = new Guest();
        // FIX for acceptance testing
        this.shoppingCart = new ShoppingCart(this);
    }

    /**
     *
     * Functions For Usecases 4.*
     *
     */
    public boolean addProductToStore(Store store, int productId, int amount) {
        return state.addProductToStore(store, productId, amount);

    }

    public boolean editProductInStore(Store store, int productId, String newInfo) {

        return state.editProductInStore(store, productId, newInfo);
    }

    public boolean deleteProductFromStore(Store store, int productId) {

        return state.deleteProductFromStore(store, productId);

    }

    public boolean setPaymentDetails(String details) {
        this.paymentDetails = details;
        return true;
    }

    public String getPaymentDetails() {
        return paymentDetails;
    }

    public UserState getState() {
        return state;
    }

    public void setState(UserState nState) {
        this.state = nState;
        state.setUser(this);
    }


    /**
     *
     * Functions For Usecases 2.6, 2.7.*
     *
     */
    public void setShoppingCart(ShoppingCart cart) {
        this.shoppingCart = cart;
    }

    public boolean addProductToCart(Store store, int productId, int amount) {
        if (shoppingCart == null) return false;
        return shoppingCart.addProduct(store, productId, amount);
    }

    public boolean editCartProductAmount(Store store, int productId, int newAmount) {
        if (shoppingCart == null) return false;
        return shoppingCart.editProduct(store, productId, newAmount);
    }

    public boolean removeProductFromCart(Store store, int productId) {
        if (shoppingCart == null) return false;
        return shoppingCart.removeProductFromCart(store, productId);
    }

    public boolean removeAllProductsFromCart() {
        if (shoppingCart == null) return false;
        shoppingCart.removeAllProducts();
        return true;
    }

//Usecase 3.1
    public boolean logout() {
        return state.logout();
    }

    public void saveLatestCart() {
        if(!isGuest()){
            Subscriber s = (Subscriber)state;
            s.saveCart(shoppingCart);
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




    public boolean isAdmin() {
    return state.isAdmin();
    }

    public int getId(){
        return id;
    }


    public boolean isCartEmpty() {
        return shoppingCart.isEmpty();
    }

    public boolean checkStoreSupplies() {
        return shoppingCart.checkStoreSupplies();
    }

    public double getShoppingCartPrice() {
        return shoppingCart.getPrice();
    }

    public void saveCurrentCartAsPurchase() {
        Map<Store, PurchaseDetails> storePurchaseDetailsMap = shoppingCart.saveAndGetStorePurchaseDetails();
        state.addPurchase(storePurchaseDetailsMap);
    }

    public void updateStoreSupplies() {
        shoppingCart.updateStoreSupplies();
    }

    public Map<Integer, Map<Integer, Integer>> getPrimitiveCartDetails() {
        return shoppingCart.getPrimitiveDetails();
    }

    public void emptyCart() {
        shoppingCart.removeAllProducts();
    }

    public void removeLastHistoryItem(List<Store> stores) {
        state.removeLastHistoryItem(stores);
    }
}

