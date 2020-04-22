package Service;

import Domain.TradingSystem.System;

public class OwnerHandler {
    private int sessionId;
    private System s = System.getInstance();

    public OwnerHandler(int sessionId){
        this.sessionId = sessionId;
    }

//Usecase 4.1.1
    public boolean addProductToStore(int storeId, int productId,int ammount){
        if(s.isSubscriber(sessionId) && s.isOwner(sessionId,storeId)){
            return s.addProductToStore(sessionId,storeId,productId,ammount);
        }
        return false;
    }
}
