package Domain.TradingSystem;

import Domain.TradingSystem.UserState;

public class Subscriber implements UserState {

    private String username;
    private String password;
    private boolean isAdmin;

    public Subscriber() {

    }

    public Subscriber(String username, String password, boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public boolean addProductToStore(Store store, int productId, int ammount) {
        store.addProduct(productId,ammount);
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

    public String getPassword() { return password; }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin() {
        isAdmin = true;
    }
}
