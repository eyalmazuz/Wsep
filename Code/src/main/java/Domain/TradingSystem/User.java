package Domain.TradingSystem;

public class User {

    private UserState state;
    private ShoppingCart cart;

    public User(){
        this.state = new Guest();
    }

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

    public void setState(UserState nState){
        this.state = nState;
        state.setUser(this);
    }


    public boolean logout() {
        return state.logout(cart);

    }

    public Store openStore() {
        return state.openStore();
    }

    public boolean addOwner(Store store, Subscriber newOwner) {
        return state.addOwner(store,newOwner);

    }

    public String getHistory() {
        return state.getHistory();
    }

    public boolean addManager(Store store, Subscriber newManager) {
        return state.addManager(store,newManager);
    }

    public boolean deleteManager(Store store, Subscriber managerToDelete) {
        return state.deleteManager(store,managerToDelete);
    }


}
