package Service;

import Domain.TradingSystem.System;

public class AdminStateHandler {

    private int sessionId;
    private System s = System.getInstance();

    public AdminStateHandler(int sessionId){
        this.sessionId = sessionId;
    }

    //uscase 6.4.1
    public String getSubscriberHistory(int subId){
        if(s.isAdmin(sessionId)){
            return s.getUserHistory(subId);
        }
        return null;
    }

    //uscase 6.4.2
    public String getStoreHistory(int storeId){
        if(s.isAdmin(sessionId)){
            return s.getStoreHistory(storeId);
        }
        return null;

    }

    public void addProductInfo(int id, String name, String category){
        if(s.isAdmin(sessionId)){
            s.addProductInfo(id, name, category);
        }
    }

}
