package Service;

import Domain.TradingSystem.System;

public class SubscriberStateHandler {

    private int sessionId;
    private System s = System.getInstance();

    public SubscriberStateHandler(int sessionId){
        this.sessionId = sessionId;
    }

    //Usecase 3.1
    public boolean logout(){
        if(s.isSubscriber(sessionId)){
            s.saveLatestCart(sessionId);
            s.logout(sessionId);
            return true;
        }
        return false;
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
