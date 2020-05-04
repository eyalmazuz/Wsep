package Service;

import DTOs.ActionResultDTO;
import DTOs.ResultCode;
import Domain.TradingSystem.System;
import Domain.TradingSystem.UserHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OwnerHandler {
    private int sessionId;
    private System s = System.getInstance();

    private ObjectMapper mapper = new ObjectMapper();

    public OwnerHandler(int sessionId){
        this.sessionId = sessionId;
    }

//Usecase 4.1.1
    public String addProductToStore(int storeId, int productId,int amount) throws JsonProcessingException {
        if(s.isSubscriber(sessionId) && s.isOwner(sessionId,storeId)){
            return mapper.writeValueAsString(s.addProductToStore(sessionId,storeId,productId,amount));
        }
        return mapper.writeValueAsString(new ActionResultDTO(ResultCode.ERROR_STORE_PRODUCT_MODIFICATION, "Only owners can use this functionality."));
    }
    //Usecase 4.1.2
    public String editProductToStore(int storeId, int productId, String info) throws JsonProcessingException {
        if(s.isSubscriber(sessionId) && s.isOwner(sessionId,storeId)){
            return mapper.writeValueAsString(s.editProductInStore(sessionId,storeId,productId,info));
        }
        return mapper.writeValueAsString(new ActionResultDTO(ResultCode.ERROR_STORE_PRODUCT_MODIFICATION, "Only owners can use this functionality."));
    }

    //uscase 4.1.3
    public String deleteProductFromStore(int storeId, int productId) throws JsonProcessingException {
        if(s.isSubscriber(sessionId) && s.isOwner(sessionId,storeId)){
            return mapper.writeValueAsString(s.deleteProductFromStore(sessionId,storeId,productId));
        }
        return mapper.writeValueAsString(new ActionResultDTO(ResultCode.ERROR_STORE_PRODUCT_MODIFICATION, "Only owners can use this functionality."));
    }

    //Usecase 4.3
    public String addStoreOwner(int storeId, int subId) throws JsonProcessingException {
        if (!s.isSubscriber(sessionId) || !s.isOwner(sessionId,storeId)) return mapper.writeValueAsString(new ActionResultDTO(ResultCode.ERROR_STORE_OWNER_MODIFICATION, "Only owners can use this functionality."));
        if (s.subIsOwner(subId, storeId)) return mapper.writeValueAsString(new ActionResultDTO(ResultCode.ERROR_STORE_OWNER_MODIFICATION, "The specified subscriber is already an owner."));
        return mapper.writeValueAsString(s.addStoreOwner(sessionId,storeId,subId));
    }

    //Usecase 4.5
    public String addStoreManager(int storeId, int userId) throws JsonProcessingException {
        if (!s.isSubscriber(sessionId) || !s.isOwner(sessionId,storeId)) return mapper.writeValueAsString(new ActionResultDTO(ResultCode.ERROR_STORE_OWNER_MODIFICATION, "Only owners can use this functionality."));
        if (s.subIsManager(userId, storeId)) return mapper.writeValueAsString(new ActionResultDTO(ResultCode.ERROR_STORE_OWNER_MODIFICATION, "The specified subscriber is already an owner."));

        return mapper.writeValueAsString(s.addStoreManager(sessionId,storeId,userId));
    }


    /**
     * Use Case 4.6.1 and 4.6.2
     * @param storeId
     * @param subId
     * @return
     */
    public String editManageOptions(int storeId, int subId, String options) throws JsonProcessingException {
        if (!s.isOwner(sessionId, storeId) || !s.isSubscriber(sessionId)) return mapper.writeValueAsString(new ActionResultDTO(ResultCode.ERROR_STORE_MANAGER_MODIFICATION, "Only owners can use this functionality."));
        if (!s.subIsManager(subId, storeId)) return mapper.writeValueAsString(new ActionResultDTO(ResultCode.ERROR_STORE_MANAGER_MODIFICATION, "The specified subscriber is not a manager."));
        return mapper.writeValueAsString(s.setManagerDetalis(sessionId,subId,storeId,options));
    }


    /**
     * Use Case 4.7
     * @param storeId
     * @param userId
     * @return
     */
    public String deleteManager(int storeId, int userId) throws JsonProcessingException {
        if (!s.isOwner(sessionId, storeId) || !s.isSubscriber(sessionId)) return mapper.writeValueAsString(new ActionResultDTO(ResultCode.ERROR_STORE_MANAGER_MODIFICATION, "Only owners can use this functionality."));
        if (!s.subIsManager(userId, storeId)) return mapper.writeValueAsString(new ActionResultDTO(ResultCode.ERROR_STORE_MANAGER_MODIFICATION, "The specified subscriber is not a manager."));
        return mapper.writeValueAsString(s.deleteManager(sessionId,storeId,userId));
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
    public String changeBuyingPolicy(int storeId, String newPolicy) throws JsonProcessingException {
        if(s.isSubscriber(sessionId) && s.isOwner(sessionId,storeId)){
            return mapper.writeValueAsString(s.changeBuyingPolicy(storeId, newPolicy));
        }
        return mapper.writeValueAsString(new ActionResultDTO(ResultCode.ERROR_STORE_BUYING_POLICY_CHANGE, "Only owners can use this functionality."));
    }




}
