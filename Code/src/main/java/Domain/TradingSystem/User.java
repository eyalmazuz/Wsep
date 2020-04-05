package Domain.TradingSystem;

public class User {

    private Permission permissions;
    private UserState state;

    public boolean addProductToStore(int storeId, int productId, int ammount) {
        Store currStore = permissions.hasPermission(storeId,"Owner");
        if(currStore != null){
            return state.addProductToStore(currStore,productId,ammount);
        }
        return false;

    }

}
