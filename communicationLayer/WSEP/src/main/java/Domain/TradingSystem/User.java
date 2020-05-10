package Domain.TradingSystem;

import DTOs.ActionResultDTO;
import DTOs.ResultCode;

import java.util.List;
import java.util.Map;

public class User {

    private System system = System.getInstance();


    private String paymentDetails;
    private UserState state;
    private ShoppingCart shoppingCart;
    private static int idCounter = 0;
    private int id;

    public User() {
        this.id = idCounter;
        idCounter++;
        this.state = new Guest();
        // FIX for acceptance testing
        this.shoppingCart = new ShoppingCart(this);
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

        if(nState == null){
            throw new NullPointerException();
        }

        this.state = nState;
        state.setUser(this);
    }


    /**
     *
     * Functions For Usecases 2.6, 2.7.*
     *
     */
    public void setShoppingCart(ShoppingCart cart) {
        if(cart!=null) {
            this.shoppingCart = cart;
        }
    }

    public ActionResultDTO addProductToCart(Store store, int productId, int amount) {
        if (shoppingCart == null) return new ActionResultDTO(ResultCode.ERROR_CART_MODIFICATION, "Invalid shopping cart.");
        return shoppingCart.addProduct(store, productId, amount);
    }

    public ActionResultDTO editCartProductAmount(Store store, int productId, int newAmount) {
        if (shoppingCart == null) return new ActionResultDTO(ResultCode.ERROR_CART_MODIFICATION, "Invalid shopping cart.");
        return shoppingCart.editProduct(store, productId, newAmount);
    }

    public ActionResultDTO removeProductFromCart(Store store, int productId) {
        if (shoppingCart == null) return new ActionResultDTO(ResultCode.ERROR_CART_MODIFICATION, "Invalid shopping cart.");
        return shoppingCart.removeProductFromCart(store, productId);
    }

    public ActionResultDTO removeAllProductsFromCart() {
        if (shoppingCart == null) return new ActionResultDTO(ResultCode.ERROR_CART_MODIFICATION, "Invalid shopping cart.");
        shoppingCart.removeAllProducts();
        return new ActionResultDTO(ResultCode.SUCCESS, "Shopping cart cleared.");
    }

//Usecase 3.1
    public boolean logout() {
        return state.logout();
    }


    public Store openStore () {
        return state.openStore();
    }



    public boolean isGuest () {
        return state instanceof Guest;
    }

    public ShoppingCart getShoppingCart () {
        return shoppingCart;
    }

    public UserPurchaseHistory getHistory () {
        return state.getHistory();
    }

/*TODO:Fix test to be on system only

    public boolean addOwner (Store store, Subscriber newOwner){
        return state.addOwner(store, newOwner);

    }


    public boolean addManager (Store store, Subscriber newManager){
        return state.addManager(store, newManager);
    }

    public boolean deleteManager (Store store, Subscriber managerToDelete){
        return state.deleteManager(store, managerToDelete);
    }
*/



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

    public UserPurchaseHistory getUserPurchaseHistory() {
        return state.getUserPurchaseHistory();
    }

    public boolean updateStoreSupplies() {
        return shoppingCart.updateStoreSupplies();
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

    public List<Integer> getStoresInCart() {
        return shoppingCart.getStores();
    }
}

