package Service;

import Domain.TradingSystem.System;
import Domain.TradingSystem.UserHandler;

public class OwnerHandler {
    private int sessionId;
    private System s = System.getInstance();

    public OwnerHandler(int sessionId){
        this.sessionId = sessionId;
    }

//Usecase 4.1.1
    public boolean addProductToStore(int storeId, int productId,int amount){
        if(s.isSubscriber(sessionId) && s.isOwner(sessionId,storeId)){
            return s.addProductToStore(sessionId,storeId,productId,amount);
        }
        return false;
    }
    //Usecase 4.1.2
    public boolean editProductToStore(int storeId, int productId, String info){
        if(s.isSubscriber(sessionId) && s.isOwner(sessionId,storeId)){
            return s.editProductInStore(sessionId,storeId,productId,info);
        }
        return false;
    }

    //uscase 4.1.3
    public boolean deleteProductFromStore(int storeId, int productId){
        if(s.isSubscriber(sessionId) && s.isOwner(sessionId,storeId)){
            return s.deleteProductFromStore(sessionId,storeId,productId);
        }
        return false;
    }

    //Usecase 4.3
    public boolean addStoreOwner(int storeId, int subId){
        if(s.isSubscriber(sessionId) && s.isOwner(sessionId,storeId) && !s.subIsOwner(subId,storeId)){
            return s.addStoreOwner(sessionId,storeId,subId);
        }
        return false;
    }

    //Usecase 4.5
    public boolean addStoreManager(int storeId, int userId){
        if(s.isSubscriber(sessionId) && s.isOwner(sessionId,storeId) && !s.subIsManager(userId,storeId)){
            return s.addStoreManager(sessionId,storeId,userId);
        }
        return false;
    }


    /**
     * Use Case 4.6.1 and 4.6.2
     * @param storeId
     * @param subId
     * @return
     */
    public boolean editManageOptions(int storeId, int subId, String options){
        if(s.subIsManager(subId,storeId) && s.isSubscriber(sessionId) && s.isOwner(sessionId,storeId)){
            return s.setManagerDetalis(sessionId,subId,storeId,options);
        }
        return false;
    }


    /**
     * Use Case 4.7
     * @param storeId
     * @param userId
     * @return
     */
    public boolean deleteManager(int storeId, int userId){
        if(s.subIsManager(userId,storeId) && s.isSubscriber(sessionId) && s.isOwner(sessionId,storeId)){
            return s.deleteManager(sessionId,storeId,userId);
        }
        return false;
    }



    /**
     * Use Case 4.10
     * @param storeId
     * @return
     */
    public String viewPurchaseHistory(int storeId){
        if( s.isSubscriber(sessionId) && s.isOwner(sessionId,storeId) ){
            return s.getStoreHistory(storeId);
        }
        return "";
    }






}
