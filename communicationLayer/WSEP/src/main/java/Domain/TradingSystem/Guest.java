package Domain.TradingSystem;


import java.util.List;
import java.util.Map;

public class Guest implements UserState {

    private User user;

    private ShoppingCart shoppingCart;

    public Guest(User user) {
        this.user = user;
        shoppingCart = new ShoppingCart(user);
    }

    private String country = "Unknown";

    /**
     *
     *Unused Methods Of subscriber state
     *
     */


    public boolean hasOwnerPermission(int storeId) {
        return false;
    }

    public boolean addPermission(Store store, Subscriber grantor, String type) {
        return false;
    }

    public boolean logout() {
        return false;
    }

    public String getPurchaseHistory() {
        return null;
    }

    @Override
    public Map<Store, List<PurchaseDetails>> getStorePurchaseLists() {
        return null;
    }

    public void setUser(User user) {

    }

    public Store openStore() {
        return null;
    }



    public boolean deleteManager(Store store, Subscriber managerToDelete) {
        return false;
    }

    public boolean editPermission(Subscriber manager, Store store, String details) {
        return false;
    }



    @Override
    public void addPurchase(Map<Store, PurchaseDetails> storePurchaseDetails) {

    }

    public void removeLastHistoryItem(List<Store> stores) {

    }

    @Override
    public boolean isAdmin() {
        return false;
    }

    @Override
    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public Map<Integer, List<Integer>> getStorePurchaseListsPrimitive() {
        return null;
    }

    @Override
    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    @Override
    public void setShoppingCart(ShoppingCart cart) {
        this.shoppingCart = cart;
    }

}
