package Domain.TradingSystem;

import javafx.util.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Permission {

    User user;
    User grantor;
    Map<Store,String> manages; //<store,Owner\Manager>

    public Permission(User user, User grantor){
        this.grantor = grantor;
        this.user = user;
        manages = new HashMap<Store, String>();
    }

    protected Store hasPermission(int storeId, String type){
        for(Store store:manages.keySet()){
            if (store.getId() == storeId){
                if(manages.get(store).equals(type)){
                    return store;
                }
            }
        }
        return null;
    }

    public boolean hasOwnerPermission() {
        for (Map.Entry<Store, String> manage: manages.entrySet()){
            if (manage.getValue().equals("Owner"))
                return true;

        }
        return false;
    }

    public void addPermission (String type, Store store){
        manages.put(store, type);
    }
}
