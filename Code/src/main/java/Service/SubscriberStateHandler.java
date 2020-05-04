package Service;

import DTOs.ActionResultDTO;
import DTOs.ResultCode;
import Domain.TradingSystem.System;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
    public int openStore(){
        if(s.isSubscriber(sessionId)){
            return s.openStore(sessionId);
        }
        return -1;
    }

    //usecase 3.7
    public String getHistory(){
        if(s.isSubscriber(sessionId)){
            return s.getHistory(sessionId);
        }
        return null;
    }
}
