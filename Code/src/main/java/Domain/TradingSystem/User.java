package Domain.TradingSystem;

public class User {

    private Permission permissions;
    private UserState state;

    public boolean addProductToStore(int storeId, int productId, int amount) {
        Store currStore = permissions.hasPermission(storeId,"Owner");
        if(currStore != null){
            return state.addProductToStore(currStore,productId,amount);
        }
        return false;

    }

    public boolean editProductInStore(int storeId, int productId, String newInfo) {
        Store currStore = permissions.hasPermission(storeId,"Owner");
        if(currStore != null){
            return state.editProductInStore(currStore,productId,newInfo);
        }
        return false;
    }

    public boolean deleteProductFromStore(int storeId, int productId) {
        Store currStore = permissions.hasPermission(storeId,"Owner");
        if(currStore != null){
            return state.deleteProductFromStore(currStore,productId);
        }
        return false;
    }


    public boolean hasOwnerPermission() {
        return permissions.hasOwnerPermission();
    }

    public UserState getState() {
        return state;
    }

    public void addPermission(String type,Store store) {
        permissions.addPermission(type, store);
    }
}
