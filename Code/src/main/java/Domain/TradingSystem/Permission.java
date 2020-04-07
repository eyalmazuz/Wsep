package Domain.TradingSystem;

import javafx.util.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Permission {

    Subscriber user;
    Subscriber grantor;
    String type; //Owner\Manager
    Store store;

    public Permission(Subscriber user, Subscriber grantor, String type, Store store){
        this.grantor = grantor;
        this.user = user;

    }


    public Store getStore() {
        return store;
    }

    public String getType() {
        return type;
    }

    public boolean isOwner(int storeId) {
        return((storeId == store.getId()) && type.equals("Owner"));
    }
}
