package Domain.TradingSystem;

import javafx.util.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Permission {

    User user;
    User grantor;
    String type; //Owner\Manager
    Store store;

    public Permission(User user, User grantor, String type, Store store){
        this.grantor = grantor;
        this.user = user;

    }


    public Store getStore() {
        return store;
    }

    public String getType() {
        return type;
    }
}
