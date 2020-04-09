package Domain.TradingSystem;



public class Guest implements UserState {


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

    public Store openStore() {
        return null;
    }

    public boolean addOwner(Store store, Subscriber newOwner) {
        return false;
    }
}
