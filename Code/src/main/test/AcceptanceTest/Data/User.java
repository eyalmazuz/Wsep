package AcceptanceTest.Data;

import java.util.LinkedList;
import java.util.List;

public class User {

    private final String username;
    private final String password;
    private ShoppingCart cart;
    private List<History> purchaseHistory;

    public User(String username, String password){
        this.username = username;
        this.password = password;
        cart = new ShoppingCart();
        purchaseHistory = new LinkedList<History>();
    }

}
