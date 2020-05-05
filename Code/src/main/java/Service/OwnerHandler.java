package Service;

import DTOs.ActionResultDTO;
import DTOs.ResultCode;
import Domain.TradingSystem.System;


public class OwnerHandler {
    private int sessionId;
    private System s = System.getInstance();

    public OwnerHandler(int sessionId){
        this.sessionId = sessionId;
    }

//Usecase 4.1.1
    public ActionResultDTO addProductToStore(int storeId, int productId,int amount) {
        if(s.isSubscriber(sessionId) && s.isOwner(sessionId,storeId)){
            return s.addProductToStore(sessionId,storeId,productId,amount);
        }
        return new ActionResultDTO(ResultCode.ERROR_STORE_PRODUCT_MODIFICATION, "Only owners can use this functionality.");
    }
    //Usecase 4.1.2
    public ActionResultDTO editProductToStore(int storeId, int productId, String info) {
        if(s.isSubscriber(sessionId) && s.isOwner(sessionId,storeId)){
            return s.editProductInStore(sessionId,storeId,productId,info);
        }
        return new ActionResultDTO(ResultCode.ERROR_STORE_PRODUCT_MODIFICATION, "Only owners can use this functionality.");
    }

    //uscase 4.1.3
    public ActionResultDTO deleteProductFromStore(int storeId, int productId) {
        if(s.isSubscriber(sessionId) && s.isOwner(sessionId,storeId)){
            return s.deleteProductFromStore(sessionId,storeId,productId);
        }
        return new ActionResultDTO(ResultCode.ERROR_STORE_PRODUCT_MODIFICATION, "Only owners can use this functionality.");
    }

    //Usecase 4.3
    public ActionResultDTO addStoreOwner(int storeId, int subId) {
        if (!s.isSubscriber(sessionId) || !s.isOwner(sessionId,storeId)) return new ActionResultDTO(ResultCode.ERROR_STORE_OWNER_MODIFICATION, "Only owners can use this functionality.");
        if (s.subIsOwner(subId, storeId)) return new ActionResultDTO(ResultCode.ERROR_STORE_OWNER_MODIFICATION, "The specified subscriber is already an owner.");
        return s.addStoreOwner(sessionId,storeId,subId);
    }

    //Usecase 4.5
    public ActionResultDTO addStoreManager(int storeId, int userId) {
        if (!s.isSubscriber(sessionId) || !s.isOwner(sessionId,storeId)) return new ActionResultDTO(ResultCode.ERROR_STORE_OWNER_MODIFICATION, "Only owners can use this functionality.");
        if (s.subIsManager(userId, storeId)) return new ActionResultDTO(ResultCode.ERROR_STORE_OWNER_MODIFICATION, "The specified subscriber is already an owner.");

        return s.addStoreManager(sessionId,storeId,userId);
    }


    /**
     * Use Case 4.6.1 and 4.6.2
     * @param storeId
     * @param subId
     * @return
     */
    public ActionResultDTO editManageOptions(int storeId, int subId, String options) {
        if (!s.isOwner(sessionId, storeId) || !s.isSubscriber(sessionId)) return new ActionResultDTO(ResultCode.ERROR_STORE_MANAGER_MODIFICATION, "Only owners can use this functionality.");
        if (!s.subIsManager(subId, storeId)) return new ActionResultDTO(ResultCode.ERROR_STORE_MANAGER_MODIFICATION, "The specified subscriber is not a manager.");
        return s.setManagerDetalis(sessionId,subId,storeId,options);
    }


    /**
     * Use Case 4.7
     * @param storeId
     * @param userId
     * @return
     */
    public ActionResultDTO deleteManager(int storeId, int userId) {
        if (!s.isOwner(sessionId, storeId) || !s.isSubscriber(sessionId)) return new ActionResultDTO(ResultCode.ERROR_STORE_MANAGER_MODIFICATION, "Only owners can use this functionality.");
        if (!s.subIsManager(userId, storeId)) return new ActionResultDTO(ResultCode.ERROR_STORE_MANAGER_MODIFICATION, "The specified subscriber is not a manager.");
        return s.deleteManager(sessionId,storeId,userId);
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

    //Usecase 4.2
    public ActionResultDTO changeBuyingPolicy(int storeId, String newPolicy) {
        if(s.isSubscriber(sessionId) && s.isOwner(sessionId,storeId)){
            return s.changeBuyingPolicy(storeId, newPolicy);
        }
        return new ActionResultDTO(ResultCode.ERROR_STORE_BUYING_POLICY_CHANGE, "Only owners can use this functionality.");
    }




}
