package NotificationPublisher;


import com.example.communicationLayer.controllers.NotificationHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Publisher {

    @Autowired
    private SimpMessageSendingOperations messageTamplate;
    private Map<Integer, List<Integer>> storeId2ManagersMap;

    public Publisher(SimpMessageSendingOperations messageTemplate){
        this.messageTamplate = messageTemplate;
        storeId2ManagersMap = new HashMap<>();
    }

    public void addManager(int storeId, int subId){
        System.out.println(String.format("add to store %d subsriber %d", storeId, subId));
        List<Integer> managers = storeId2ManagersMap.computeIfAbsent(storeId, k -> new ArrayList());

        if(!managers.contains(subId)){
            managers.add(subId);
        }

    }

    public void deleteManager(int storeId,Integer subId){
        List<Integer> managers = storeId2ManagersMap.get(storeId);
        if(managers!= null && managers.contains(subId)){
            managers.remove(subId);
        }
    }

    public void notifyStore(int storeId)
    {
        List<Integer> managers = storeId2ManagersMap.get(storeId);
        if(managers!=null){
            for(int subscriber:managers){
                System.out.println(String.format("Published to: %d ", subscriber));
                if(this.messageTamplate!=null){

                    this.messageTamplate.convertAndSend("/storeUpdate/"+subscriber,"Store "+storeId+" Has been updated");
                }
                else {
                    System.out.println("message Template is null");
                }
            }
        }

    }
}
