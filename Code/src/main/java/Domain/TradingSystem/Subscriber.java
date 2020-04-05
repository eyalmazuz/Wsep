package Domain.TradingSystem;

import Domain.TradingSystem.UserState;

public class Subscriber implements UserState {

    private String username;
    private String password;
    private boolean isAdmin;

    public boolean addProductToStore(Store store, int productId, int ammount) {
        store.addProduct(productId,ammount);
        return true;
    }

    public boolean deleteProductFromStore(Store store, int productId) {
        return store.deleteProduct(productId);
    }


    public boolean editProductInStore(Store store, int productId, String info) {
        store.editProduct(productId,info);
        return true;
    }

    public void setUserName(String userName) {
        this.username = userName;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin() {
        isAdmin = true;
    }
}
