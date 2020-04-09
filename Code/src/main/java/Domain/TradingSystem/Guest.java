package Domain.TradingSystem;



public class Guest implements UserState {





    /**
     *
     *Unused Methods Of subscriber state
     *
     */
    public boolean addProductToStore(int storeId, int productId, int ammount) {
        return false;
    }

    public boolean editProductInStore(int currStore, int productId, String newInfo) {
        return false;
    }

    public boolean deleteProductFromStore(int currStore, int productId) {
        return false;
    }

    public boolean hasOwnerPermission(int storeId) {
        return false;
    }

    public boolean addPermission(Store store, Subscriber grantor, String type) {
        return false;
    }

    public boolean logout(ShoppingCart cart) {
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

    public boolean addOwner(Store store, Subscriber newOwner) {
        return false;
    }

    public boolean addManager(Store store, Subscriber newManager) {
        return false;
    }

    public boolean deleteManager(Store store, Subscriber managerToDelete) {
        return false;
    }

    public boolean editPermission(Subscriber manager, Store store, String details) {
        return false;
    }

    public String getStoreHistory(int storeId) {
        return null;
    }
}
