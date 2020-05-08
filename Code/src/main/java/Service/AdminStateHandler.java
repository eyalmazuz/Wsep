package Service;

import DTOs.ResultCode;
import DTOs.StorePurchaseHistoryDTO;
import DTOs.UserPurchaseHistoryDTO;
import Domain.TradingSystem.System;

public class AdminStateHandler {

    private int sessionId;
    private System s = System.getInstance();

    public AdminStateHandler(int sessionId){
        this.sessionId = sessionId;
    }

    //uscase 6.4.1
    public UserPurchaseHistoryDTO getSubscriberHistory(int subId){
        if(s.isAdmin(sessionId)){
            return s.getUserHistory(subId);
        }
        return new UserPurchaseHistoryDTO(ResultCode.ERROR_HISTORY,"User is not Admin",null);
    }

    //uscase 6.4.2
    public StorePurchaseHistoryDTO getStoreHistory(int storeId){
        if(s.isAdmin(sessionId)){
            return s.getStoreHistory(storeId);
        }
        return new StorePurchaseHistoryDTO(ResultCode.ERROR_STOREHISTORY,"User Is not admin",-1,null);

    }

    public void addProductInfo(int id, String name, String category){
        if(s.isAdmin(sessionId)){
            s.addProductInfo(id, name, category);
        }
    }

}
