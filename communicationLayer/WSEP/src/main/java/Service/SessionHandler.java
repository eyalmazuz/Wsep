package Service;


import DTOs.ActionResultDTO;
import DTOs.IntActionResultDto;
import DTOs.PermissionActionResultDTO;
import DTOs.ResultCode;
import Domain.TradingSystem.System;
import NotificationPublisher.Publisher;

public class SessionHandler {

    private System system = System.getInstance();

    public SessionHandler(){

    }

    public ActionResultDTO setup(String supplyConfig, String paymentConfig,String path) {
        return system.setup(supplyConfig, paymentConfig,path);
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

    public PermissionActionResultDTO getPermission(int subId, int storeId) {
       return  system.getPermission(subId,storeId);
    }

    public ActionResultDTO isAdmin(int sessionId) {
        if (system.isAdmin(sessionId)){
            return new ActionResultDTO(ResultCode.SUCCESS,"Is ADMIN!");
        }
        return new ActionResultDTO(ResultCode.ERROR_SESSIONID,"is not admin");
    }
}
