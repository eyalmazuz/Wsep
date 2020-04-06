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

    public boolean hasOwnerPermission() {
        return false;
    }

    public void addPermission(Store store, User user, User grantor, String type) {

    }
}
