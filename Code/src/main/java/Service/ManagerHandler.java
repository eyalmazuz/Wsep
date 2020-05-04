package Service;

import DTOs.ActionResultDTO;
import DTOs.ResultCode;
import Domain.TradingSystem.System;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.xml.transform.Result;

public class ManagerHandler {
    //Usecase 5.1 Handler
    private int sessionId;
    private System s = System.getInstance();

    private ObjectMapper mapper = new ObjectMapper();

    public ManagerHandler(int sessionId){
        this.sessionId = sessionId;
    }

    public String addProductToStore(int storeId, int productId, int amount) throws JsonProcessingException {
        if(s.isSubscriber(sessionId) && s.isManagerWith(sessionId,storeId,"add Product")){
            return mapper.writeValueAsString(s.addProductToStore(sessionId,storeId,productId,amount));
        }
        return mapper.writeValueAsString(new ActionResultDTO(ResultCode.ERROR_STORE_PRODUCT_MODIFICATION, "Only managers can add products to stores."));
    }

    public String editProductToStore(int storeId, int productId, String info) throws JsonProcessingException {
        if(s.isSubscriber(sessionId) && s.isManagerWith(sessionId,storeId,"edit Product")){
            return mapper.writeValueAsString(s.editProductInStore(sessionId,storeId,productId,info));
        }
        return mapper.writeValueAsString(new ActionResultDTO(ResultCode.ERROR_STORE_PRODUCT_MODIFICATION, "Only managers can edit products in stores."));
    }

    public String deleteProductFromStore(int storeId, int productId) throws JsonProcessingException {
        if(s.isSubscriber(sessionId) && s.isManagerWith(sessionId,storeId,"delete Product")){
            return mapper.writeValueAsString(s.deleteProductFromStore(sessionId,storeId,productId));
        }
        return mapper.writeValueAsString(new ActionResultDTO(ResultCode.ERROR_STORE_PRODUCT_MODIFICATION, "Only managers can delete products in stores."));
    }

    public String viewPurchaseHistory(int storeId){
        if( s.isSubscriber(sessionId) && s.isManagerWith(sessionId,storeId,"any") ){
            return s.getStoreHistory(storeId);
        }
        return "";
    }

    //Usecase 4.2
    public String changeBuyingPolicy(int storeId, String newPolicy) throws JsonProcessingException {
        if(s.isSubscriber(sessionId) && s.isManagerWith(sessionId,storeId, "any")){
            return mapper.writeValueAsString(s.changeBuyingPolicy(storeId, newPolicy));
        }
        return mapper.writeValueAsString(new ActionResultDTO(ResultCode.ERROR_STORE_PRODUCT_MODIFICATION, "Only managers can delete products in stores."));
    }

}
