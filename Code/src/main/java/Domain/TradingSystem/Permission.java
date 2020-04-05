package Domain.TradingSystem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Permission {

    User user;
    User grantor;
    Map<Store,String> manages;

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
}
