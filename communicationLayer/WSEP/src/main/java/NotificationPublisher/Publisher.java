package NotificationPublisher;


import com.example.communicationLayer.controllers.NotificationHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Publisher {
    private NotificationHandler handler;
    private Map<Integer, List<Integer>> storeId2ManagersMap;

    public Publisher(){
        handler = new NotificationHandler();
        storeId2ManagersMap = new HashMap<>();
    }

    private void addManager(int storeId, int subId){
        List<Integer> managers = storeId2ManagersMap.computeIfAbsent(storeId, k -> new ArrayList());

        if(!managers.contains(subId)){
            managers.add(subId);
        }

    }

    private void deleteManager(int storeId,Integer subId){
        List<Integer> managers = storeId2ManagersMap.get(storeId);
        if(managers!= null && managers.contains(subId)){
            managers.remove(subId);
        }
    }

    private void notifyStore(int storeId)
    {
        List<Integer> managers = storeId2ManagersMap.get(storeId);
        if(managers!=null){
            for(int subscriber:managers){
                handler.sendStoreUpdate(subscriber,storeId);
            }
        }

    }
}
