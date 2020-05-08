package Service;

import DTOs.ActionResultDTO;
import DTOs.IntActionResultDto;
import DTOs.ResultCode;
import Domain.TradingSystem.System;

public class SubscriberStateHandler {

    private int sessionId;
    private System s = System.getInstance();

    public SubscriberStateHandler(int sessionId){
        this.sessionId = sessionId;
    }

    //Usecase 3.1
    public ActionResultDTO logout()  {
        if(s.isSubscriber(sessionId)){
            s.logout(sessionId);
            return new ActionResultDTO(ResultCode.SUCCESS, null);
        }
        return new ActionResultDTO(ResultCode.ERROR_LOGOUT, "To log out you must be logged in.");
    }

    //Usecase 3.2
    public IntActionResultDto openStore(){
        if(s.isSubscriber(sessionId)){
            return s.openStore(sessionId);
        }
        return new IntActionResultDto(ResultCode.ERROR_OPENSTORE,"user is not subscriber",-1);
    }

    //usecase 3.7
    public String getHistory(){
        if(s.isSubscriber(sessionId)){
            return s.getHistory(sessionId);
        }
        return null;
    }
}
