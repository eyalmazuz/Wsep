package Domain.TradingSystem;

public class User {

    private UserState state;

    public boolean addProductToStore(int storeId, int productId, int amount) {
        return state.addProductToStore(storeId,productId,amount);

    }

    public boolean editProductInStore(int storeId, int productId, String newInfo) {

        return state.editProductInStore(storeId,productId,newInfo);
    }

    public boolean deleteProductFromStore(int storeId, int productId) {

        return state.deleteProductFromStore(storeId,productId);

    }




    public UserState getState() {
        return state;
    }

    public boolean hasOwnerPermission() {
        return state.hasOwnerPermission();
    }

    public void addPermission(Store store, User newOwner, User grantor, String owner) {
        state.addPermission(store, newOwner, grantor, owner);
    }
}
