package Service;


import DTOs.ActionResultDTO;
import DTOs.IntActionResultDto;
import Domain.TradingSystem.System;
import NotificationPublisher.Publisher;

public class SessionHandler {

    private System system = System.getInstance();

    public SessionHandler(){

    }

    public ActionResultDTO setup(String supplyConfig, String paymentConfig) {
        return system.setup(supplyConfig, paymentConfig);
    }

    public IntActionResultDto startSession() {
        return system.startSession();
    }

    public void setPublisher(Publisher publisher){
        system.setPublisher(publisher);
    }

    public void notoficationAck(int subId, int notificationId) {
        system.removeNotification(subId,notificationId);
    }

    public void pullNotifications(int subId) {
        system.pullNotifications(subId);
    }
}
