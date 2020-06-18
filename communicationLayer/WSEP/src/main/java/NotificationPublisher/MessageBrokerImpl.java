package NotificationPublisher;

import org.springframework.messaging.simp.SimpMessageSendingOperations;

import java.util.ArrayList;
import java.util.List;

public class MessageBrokerImpl implements MessageBroker {

    private SimpMessageSendingOperations messageTemplate;

    public MessageBrokerImpl(SimpMessageSendingOperations messageTemplate) {
        this.messageTemplate = messageTemplate;
    }

    @Override
    public List<Integer> sendTo(String url,List<Integer> subscribers, Object message) {
        if(subscribers.isEmpty()) {//broadcast
            System.out.println(String.format("sending stat message to: %s", url));
            messageTemplate.convertAndSend(url, message);
        }
        else
            for (Integer sessionId : subscribers) {
                messageTemplate.convertAndSend(url + sessionId, message);
                System.out.println("Notified User" + sessionId + " in address: " + url + sessionId);
            }
        return new ArrayList<>();
    }
}
