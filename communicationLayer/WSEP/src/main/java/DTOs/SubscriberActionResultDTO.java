package DTOs;

import DTOs.SimpleDTOS.SubscriberDTO;

import java.util.List;

public class SubscriberActionResultDTO extends ActionResultDTO{
    List<SubscriberDTO> subscribers;

    public SubscriberActionResultDTO(ResultCode resultCode, String details, List<SubscriberDTO> subscribers) {
        super(resultCode, details);
        this.subscribers = subscribers;
    }

    public List<SubscriberDTO> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(List<SubscriberDTO> subscribers) {
        this.subscribers = subscribers;
    }
}
