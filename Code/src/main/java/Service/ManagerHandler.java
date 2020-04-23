package Service;

import Domain.TradingSystem.System;

public class ManagerHandler {
    //Usecase 5.1 Handler
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

    public boolean editProductToStore(int storeId, int productId, String info){
        if(s.isSubscriber(sessionId) && s.isManagerWith(sessionId,storeId,"edit Product")){
            return s.editProductInStore(sessionId,storeId,productId,info);
        }
        return false;
    }

    public boolean deleteProductFromStore(int storeId, int productId){
        if(s.isSubscriber(sessionId) && s.isManagerWith(sessionId,storeId,"delete Product")){
            return s.deleteProductFromStore(sessionId,storeId,productId);
        }
        return false;
    }

    public String viewPurchaseHistory(int storeId){
        if( s.isSubscriber(sessionId) && s.isManagerWith(sessionId,storeId,"any") ){
            return s.getStoreHistory(storeId);
        }
        return "";
    }
}
