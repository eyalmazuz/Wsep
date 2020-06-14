package NotificationPublisher;

import java.util.List;

public interface MessageBroker {

    public List<Integer> sendTo(String url,List<Integer> subscribers, Object message);
}
