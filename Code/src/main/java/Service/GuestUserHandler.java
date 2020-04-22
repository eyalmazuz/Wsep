package Service;

import Domain.TradingSystem.System;
import Domain.TradingSystem.UserHandler;

public class GuestUserHandler {
    System s = System.getInstance();

    public boolean login(int sessionId , String username, String password){
        //check if guest - userHandler
        if(s.isGuest(sessionId)){
            int subId = s.getSubscriber(username,password);
            if(subId != -1){
                s.setState(sessionId,subId);
                s.mergeCartWithSubscriber(sessionId);
                return true;
            }
            return false;
        }
        return false;

    }
}
