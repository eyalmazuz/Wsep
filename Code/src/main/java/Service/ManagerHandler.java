package Service;

import Domain.TradingSystem.System;

public class ManagerHandler {
    private int sessionId;
    private System s = System.getInstance();

    public ManagerHandler(int sessionId){
        this.sessionId = sessionId;
    }

    public boolean addProductToStore(int storeId, int productId,int ammount){
        if(s.isSubscriber(sessionId) && s.isManagerWith(sessionId,storeId,"add Product")){
            return s.addProductToStore(sessionId,storeId,productId,ammount);
        }
        return false;
    }
}
