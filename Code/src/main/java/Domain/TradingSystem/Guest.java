package Domain.TradingSystem;


import java.util.List;
import java.util.Map;

public class Guest implements UserState {

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

    public String getHistory() {
        return "No History";
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
    public UserPurchaseHistory getUserPurchaseHistory() {
        return null;
    }

}
