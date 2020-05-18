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
    public List<Integer> sendTo(List<Integer> subscribers, Object message) {
        for(Integer sessionId : subscribers) {
            messageTemplate.convertAndSend("/storeUpdate/" + sessionId, message);
            System.out.println("Notified User" + sessionId);
        } return new ArrayList<>();
    }
}
