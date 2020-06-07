package Domain.TradingSystem;

import DTOs.ActionResultDTO;
import DTOs.DoubleActionResultDTO;
import DTOs.ResultCode;
import DataAccess.DAOManager;

import java.util.List;
import java.util.Map;

public class User {

    private System system = System.getInstance();


    private String paymentDetails;
    private UserState state;
    private ShoppingCart shoppingCart;
    public static int idCounter = 0;
    private int id;
    private String country = "Unknown";

    public User() {
        this.id = Math.max(DAOManager.getMaxSubscriberId() + 1, idCounter);
        idCounter = id + 1;
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

        if (nState instanceof Subscriber) shoppingCart.setSubscriberId(((Subscriber) nState).getId());

        this.state = nState;
        state.setUser(this);
        state.setCountry(country);

        if (nState instanceof Subscriber) DAOManager.createOrUpdateSubscriber((Subscriber) state);
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

    public ActionResultDTO addProductToCart(Store store, ProductInfo product, int amount) {
        if (shoppingCart == null) return new ActionResultDTO(ResultCode.ERROR_CART_MODIFICATION, "Invalid shopping cart.");
        return shoppingCart.addProduct(store, product, amount);
    }

    public ActionResultDTO editCartProductAmount(Store store, ProductInfo product, int newAmount) {
        if (shoppingCart == null) return new ActionResultDTO(ResultCode.ERROR_CART_MODIFICATION, "Invalid shopping cart.");
        return shoppingCart.editProduct(store, product, newAmount);
    }

    public ActionResultDTO removeProductFromCart(Store store, ProductInfo product) {
        if (shoppingCart == null) return new ActionResultDTO(ResultCode.ERROR_CART_MODIFICATION, "Invalid shopping cart.");
        return shoppingCart.removeProductFromCart(store, product);
    }

    public ActionResultDTO removeAllProductsFromCart() {
        if (shoppingCart == null) return new ActionResultDTO(ResultCode.ERROR_CART_MODIFICATION, "Invalid shopping cart.");
        shoppingCart.removeAllProducts();
        return new ActionResultDTO(ResultCode.SUCCESS, "Shopping cart cleared.");
    }

//Usecase 3.1
    public boolean logout() {
        shoppingCart.setSubscriberId(-1);
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

    public String getHistory () {
        return state.getPurchaseHistory();
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

    public DoubleActionResultDTO getShoppingCartPrice() {
        return shoppingCart.getPrice();
    }

    public void saveCurrentCartAsPurchase() {
        Map<Store, PurchaseDetails> storePurchaseDetailsMap = shoppingCart.saveAndGetStorePurchaseDetails();
        state.addPurchase(storePurchaseDetailsMap);
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

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public Map<Store, List<PurchaseDetails>> getStorePurchaseLists() {
        return state.getStorePurchaseLists();
    }
}

