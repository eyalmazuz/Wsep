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
    private MessageBroker messageTamplate;


    public Publisher(MessageBroker messageTemplate){
        this.messageTamplate = messageTemplate;

    }



    public void notify(String path, List<Integer> users,Object message)
    {
        if(users!=null){
            messageTamplate.sendTo(path,users,message);
        }

    }
}
