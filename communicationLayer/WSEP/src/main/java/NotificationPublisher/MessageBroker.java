package NotificationPublisher;

import java.util.List;

public interface MessageBroker {

    public List<Integer> sendTo(List<Integer> subscribers, Object message);
}
