package Service;

import DTOs.ActionResultDTO;
import DTOs.IntActionResultDto;
import DTOs.ResultCode;
import DTOs.StorePurchaseHistoryDTO;
import Domain.TradingSystem.System;

import java.util.List;

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

    public StorePurchaseHistoryDTO viewPurchaseHistory(int storeId){
        if( s.isSubscriber(sessionId) && s.isManagerWith(sessionId,storeId,"any") ){
            return s.getStoreHistory(storeId);
        }
        return new StorePurchaseHistoryDTO(ResultCode.ERROR_STOREHISTORY,"User Is not manager",-1,null);
    }

    //Usecase 4.2
    public ActionResultDTO changeBuyingPolicy(int storeId, String newPolicy) {
        if(s.isSubscriber(sessionId) && s.isManagerWith(sessionId,storeId, "any")){
            return s.changeBuyingPolicy(storeId, newPolicy);
        }
        return new ActionResultDTO(ResultCode.ERROR_STORE_BUYING_POLICY_CHANGE, "Only managers can change buying policies in stores.");
    }

    // editing policies

    // for productId = -1, constraint will be for all products in the basket
    public IntActionResultDto addSimpleBuyingTypeBasketConstraint(int storeId, int productId, String minmax, int amount) {
        if (s.isSubscriber(sessionId) && s.isManagerWith(sessionId, storeId, "any")) {
            if (!minmax.toLowerCase().equals("max") && !minmax.toLowerCase().equals("min")) return new IntActionResultDto(ResultCode.ERROR_STORE_BUYING_POLICY_CHANGE, "The minmax string should be either max or min", -1);
            if (amount < 0) return new IntActionResultDto(ResultCode.ERROR_STORE_BUYING_POLICY_CHANGE, "Amount must be non-negative", -1);

            int buyingTypeID = s.addSimpleBuyingTypeBasketConstraint(storeId, productId, minmax, amount);
            return new IntActionResultDto(ResultCode.SUCCESS, "Added buying type " + buyingTypeID, buyingTypeID);
        }
        return new IntActionResultDto(ResultCode.ERROR_STORE_BUYING_POLICY_CHANGE, "Only managers can change buying policies in stores.", -1);
    }

    public IntActionResultDto addSimpleBuyingTypeUserConstraint(int storeId, String country) {
        if (s.isSubscriber(sessionId) && s.isManagerWith(sessionId, storeId, "any")) {
            if (country.toLowerCase().equals("any")) return new IntActionResultDto(ResultCode.ERROR_STORE_BUYING_POLICY_CHANGE, "No new policy has been added", -1);
            int buyingTypeID = s.addSimpleBuyingTypeUserConstraint(storeId, country);
            return new IntActionResultDto(ResultCode.SUCCESS, "Added buying type " + buyingTypeID, buyingTypeID);
        }
        return new IntActionResultDto(ResultCode.ERROR_STORE_BUYING_POLICY_CHANGE, "Only managers can change buying policies in stores.", -1);
    }

    public IntActionResultDto addSimpleBuyingTypeSystemConstraint(int storeId, int dayOfWeek) {
        if (s.isSubscriber(sessionId) && s.isManagerWith(sessionId, storeId, "any")) {
            if (dayOfWeek < 1 || dayOfWeek > 7) return new IntActionResultDto(ResultCode.ERROR_STORE_BUYING_POLICY_CHANGE, "Invalid day of week", -1);
            int buyingTypeID = s.addSimpleBuyingTypeSystemConstraint(storeId, dayOfWeek);
            return new IntActionResultDto(ResultCode.SUCCESS, "Added buying type " + buyingTypeID, buyingTypeID);
        }
        return new IntActionResultDto(ResultCode.ERROR_STORE_BUYING_POLICY_CHANGE, "Only managers can change buying policies in stores.", -1);
    }

    public ActionResultDTO removeBuyingType(int storeId, int buyingTypeID) {
        if (s.isSubscriber(sessionId) && s.isManagerWith(sessionId, storeId, "any")) {
            s.removeBuyingTypeFromStore(storeId, buyingTypeID);
            return new ActionResultDTO(ResultCode.SUCCESS, "Removed buying type " + buyingTypeID);
        }
        return new ActionResultDTO(ResultCode.ERROR_STORE_BUYING_POLICY_CHANGE, "Only managers can change buying policies in stores.");
    }

    public ActionResultDTO removeAllBuyingTypes(int storeId) {
        if (s.isSubscriber(sessionId) && s.isManagerWith(sessionId, storeId, "any")) {
            s.removeAllBuyingTypes(storeId);
            return new ActionResultDTO(ResultCode.SUCCESS, "Removed all buying types");
        }
        return new ActionResultDTO(ResultCode.ERROR_STORE_BUYING_POLICY_CHANGE, "Only managers can change buying policies in stores.");
    }

    public IntActionResultDto createAdvancedBuyingType(int storeId, List<Integer> buyingTypeIDs, String logicalOperation) {
        if (s.isSubscriber(sessionId) && s.isManagerWith(sessionId, storeId, "any")) {
            if (!logicalOperation.toLowerCase().equals("xor") && !logicalOperation.toLowerCase().equals("and") && !logicalOperation.toLowerCase().equals("or")
            && !logicalOperation.toLowerCase().equals("implies")) return new IntActionResultDto(ResultCode.ERROR_STORE_BUYING_POLICY_CHANGE, "Logical operations are: and, or, xor, implies", -1);
            if (logicalOperation.toLowerCase().equals("implies") && buyingTypeIDs.size() != 2) return new IntActionResultDto(ResultCode.ERROR_STORE_BUYING_POLICY_CHANGE, "Implies requires exactly 2 basic constraints", -1);

            return s.addAdvancedBuyingType(storeId, buyingTypeIDs, logicalOperation);
        }
        return new IntActionResultDto(ResultCode.ERROR_STORE_BUYING_POLICY_CHANGE, "Only managers can change buying policies in stores.", -1);
    }


}
