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

    public ManagerHandler(int sessionId){
        this.sessionId = sessionId;
    }

    public ActionResultDTO addProductToStore(int storeId, int productId, int amount)  {
        if(s.isSubscriber(sessionId) && s.isManagerWith(sessionId,storeId,"add Product")){
            return s.addProductToStore(sessionId,storeId,productId,amount);
        }
        return new ActionResultDTO(ResultCode.ERROR_STORE_PRODUCT_MODIFICATION, "Only managers can add products to stores.");
    }

    public ActionResultDTO editProductToStore(int storeId, int productId, String info)  {
        if(s.isSubscriber(sessionId) && s.isManagerWith(sessionId,storeId,"edit Product")){
            return s.editProductInStore(sessionId,storeId,productId,info);
        }
        return new ActionResultDTO(ResultCode.ERROR_STORE_PRODUCT_MODIFICATION, "Only managers can edit products in stores.");
    }

    public ActionResultDTO deleteProductFromStore(int storeId, int productId) {
        if(s.isSubscriber(sessionId) && s.isManagerWith(sessionId,storeId,"delete Product")){
            return s.deleteProductFromStore(sessionId,storeId,productId);
        }
        return new ActionResultDTO(ResultCode.ERROR_STORE_PRODUCT_MODIFICATION, "Only managers can delete products in stores.");
    }

    public String viewPurchaseHistory(int storeId){
        if( s.isSubscriber(sessionId) && s.isManagerWith(sessionId,storeId,"any") ){
            return s.getStoreHistory(storeId);
        }
        return "";
    }

    //Usecase 4.2
    public ActionResultDTO changeBuyingPolicy(int storeId, String newPolicy) {
        if(s.isSubscriber(sessionId) && s.isManagerWith(sessionId,storeId, "any")){
            return s.changeBuyingPolicy(storeId, newPolicy);
        }
        return new ActionResultDTO(ResultCode.ERROR_STORE_PRODUCT_MODIFICATION, "Only managers can delete products in stores.");
    }

}
