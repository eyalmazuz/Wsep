package Service;

import DTOs.*;
import DataAccess.DatabaseFetchException;
import Domain.TradingSystem.System;

import java.util.List;


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

    public ActionResultDTO addProductToStore(int storeId, int productId , String productName, String productCategory ,int amount, double basePrice) {
        if(s.isSubscriber(sessionId) && s.isOwner(sessionId,storeId)){
            ActionResultDTO addProductInfoResult = s.addProductInfo(productId,productName,productCategory,basePrice);
            if(addProductInfoResult.getResultCode()!=ResultCode.SUCCESS){
                return addProductInfoResult;
            }
            ActionResultDTO result = s.addProductToStore(sessionId,storeId,productId,amount);
            if(result.getResultCode()!=ResultCode.SUCCESS){
                s.removeProduct(productId);
            }
            return result;
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
        //java.lang.System.out.println("asdasd");
        if(s.isSubscriber(sessionId) && s.isOwner(sessionId,storeId)){
            return s.deleteProductFromStore(sessionId,storeId,productId);
        }
        return new ActionResultDTO(ResultCode.ERROR_STORE_PRODUCT_MODIFICATION, "Only owners can use this functionality.");
    }

    public SubscriberActionResultDTO getPossibleManagers(int storeId){
        if (!s.isSubscriber(sessionId) || !s.isOwner(sessionId,storeId)) return new SubscriberActionResultDTO(ResultCode.ERROR_STORE_OWNER_MODIFICATION, "Only owners can use this functionality.",null);
        else
            return s.getAvailableUsersToOwn(storeId);
    }

    //Usecase 4.3
    public ActionResultDTO addStoreOwner(int storeId, int subId) {
        if (!s.isSubscriber(sessionId) || !s.isOwner(sessionId,storeId)) return new ActionResultDTO(ResultCode.ERROR_STORE_OWNER_MODIFICATION, "Only owners can use this functionality.");
        try {
            if (s.subIsOwner(subId, storeId)) return new ActionResultDTO(ResultCode.ERROR_STORE_OWNER_MODIFICATION, "The specified subscriber is already an owner.");
        } catch (DatabaseFetchException e) {
            return new ActionResultDTO(ResultCode.ERROR_DATABASE, "Could not contact database. Please try again later.");
        }
        return s.addStoreOwner(sessionId,storeId,subId);
    }

    //Usecase 4.5
    public ActionResultDTO addStoreManager(int storeId, int userId) {
        if (!s.isSubscriber(sessionId) || !s.isOwner(sessionId,storeId)) return new ActionResultDTO(ResultCode.ERROR_STORE_OWNER_MODIFICATION, "Only owners can use this functionality.");
        try {
            if (s.subIsManager(userId, storeId)) return new ActionResultDTO(ResultCode.ERROR_STORE_OWNER_MODIFICATION, "The specified subscriber is already an owner.");
        } catch (DatabaseFetchException e) {
            return new ActionResultDTO(ResultCode.ERROR_DATABASE, "Could not contact database. Please try again later.");

        }

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
        try {
            if (!s.subIsManager(subId, storeId)) return new ActionResultDTO(ResultCode.ERROR_STORE_MANAGER_MODIFICATION, "The specified subscriber is not a manager.");
        } catch (DatabaseFetchException e) {
            return new ActionResultDTO(ResultCode.ERROR_DATABASE, "Could not contact database. Please try again later.");

        }
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
        try {
            if (!s.subIsManager(userId, storeId)) return new ActionResultDTO(ResultCode.ERROR_STORE_MANAGER_MODIFICATION, "The specified subscriber is not a manager.");
        } catch (DatabaseFetchException e) {
            return new ActionResultDTO(ResultCode.ERROR_DATABASE, "Could not contact database. Please try again later.");

        }
        return s.deleteManager(sessionId,storeId,userId);
    }



    /**
     * Use Case 4.10
     * @param storeId
     * @return
     */
    public StorePurchaseHistoryDTO viewPurchaseHistory(int storeId){
        if( s.isSubscriber(sessionId) && s.isOwner(sessionId,storeId) ){
            return s.getStoreHistory(storeId);
        }
        return new StorePurchaseHistoryDTO(ResultCode.ERROR_STOREHISTORY,"User Is not Owner",-1,null);    }

    //Usecase 4.2
    public ActionResultDTO changeBuyingPolicy(int storeId, String newPolicy) {
        if(s.isSubscriber(sessionId) && s.isOwner(sessionId,storeId)){
            return s.changeBuyingPolicy(storeId, newPolicy);
        }
        return new ActionResultDTO(ResultCode.ERROR_STORE_BUYING_POLICY_CHANGE, "Only owners can use this functionality.");
    }

    // editing policies

    // for productId = -1, constraint will be for all products in the basket
    public IntActionResultDto addSimpleBuyingTypeBasketConstraint(int storeId, int productId, String minmax, int amount) {
        if (s.isSubscriber(sessionId) && s.isOwner(sessionId, storeId)) {
            if (!minmax.toLowerCase().equals("max") && !minmax.toLowerCase().equals("min")) return new IntActionResultDto(ResultCode.ERROR_STORE_BUYING_POLICY_CHANGE, "The minmax string should be either max or min", -1);
            if (amount < 0) return new IntActionResultDto(ResultCode.ERROR_STORE_BUYING_POLICY_CHANGE, "Amount must be non-negative", -1);

            int buyingTypeID = 0;
            try {
                buyingTypeID = s.addSimpleBuyingTypeBasketConstraint(storeId, productId, minmax, amount);
            } catch (DatabaseFetchException e) {
                return new IntActionResultDto(ResultCode.ERROR_DATABASE, "Could not contact database. Please try again later.", -1);
            }
            return new IntActionResultDto(ResultCode.SUCCESS, "Added buying type " + buyingTypeID, buyingTypeID);
        }
        return new IntActionResultDto(ResultCode.ERROR_STORE_BUYING_POLICY_CHANGE, "Only managers can change buying policies in stores.", -1);
    }

    public IntActionResultDto addSimpleBuyingTypeUserConstraint(int storeId, String country) {
        if (s.isSubscriber(sessionId) && s.isOwner(sessionId, storeId)) {
            if (country.toLowerCase().equals("any")) return new IntActionResultDto(ResultCode.ERROR_STORE_BUYING_POLICY_CHANGE, "No new policy has been added", -1);
            int buyingTypeID = 0;
            try {
                buyingTypeID = s.addSimpleBuyingTypeUserConstraint(storeId, country);
            } catch (DatabaseFetchException e) {
                return new IntActionResultDto(ResultCode.ERROR_DATABASE, "Could not contact database. Please try again later.", -1);
            }
            return new IntActionResultDto(ResultCode.SUCCESS, "Added buying type " + buyingTypeID, buyingTypeID);
        }
        return new IntActionResultDto(ResultCode.ERROR_STORE_BUYING_POLICY_CHANGE, "Only managers can change buying policies in stores.", -1);
    }

    public IntActionResultDto addSimpleBuyingTypeSystemConstraint(int storeId, int dayOfWeek) {
        if (s.isSubscriber(sessionId) && s.isOwner(sessionId, storeId)) {
            if (dayOfWeek < 1 || dayOfWeek > 7) return new IntActionResultDto(ResultCode.ERROR_STORE_BUYING_POLICY_CHANGE, "Invalid day of week", -1);
            int buyingTypeID = 0;
            try {
                buyingTypeID = s.addSimpleBuyingTypeSystemConstraint(storeId, dayOfWeek);
            } catch (DatabaseFetchException e) {
                return new IntActionResultDto(ResultCode.ERROR_DATABASE, "Could not contact database. Please try again later.", -1);
            }
            return new IntActionResultDto(ResultCode.SUCCESS, "Added buying type " + buyingTypeID, buyingTypeID);
        }
        return new IntActionResultDto(ResultCode.ERROR_STORE_BUYING_POLICY_CHANGE, "Only managers can change buying policies in stores.", -1);
    }

    public IntActionResultDto removeBuyingType(int storeId, int buyingTypeID) {
        if (s.isSubscriber(sessionId) && s.isOwner(sessionId, storeId)) {
            try {
                s.removeBuyingTypeFromStore(storeId, buyingTypeID);
            } catch (DatabaseFetchException e) {
                return new IntActionResultDto(ResultCode.ERROR_DATABASE, "Could not contact database. Please try again later.", -1);
            }
            return new IntActionResultDto(ResultCode.SUCCESS, "Removed buying type " + buyingTypeID, buyingTypeID);
        }
        return new IntActionResultDto(ResultCode.ERROR_STORE_BUYING_POLICY_CHANGE, "Only managers can change buying policies in stores.", -1);
    }

    public IntActionResultDto removeAllBuyingTypes(int storeId) {
        if (s.isSubscriber(sessionId) && s.isOwner(sessionId, storeId)) {
            try {
                s.removeAllBuyingTypes(storeId);
            } catch (DatabaseFetchException e) {
                return new IntActionResultDto(ResultCode.ERROR_DATABASE, "Could not contact database. Please try again later.", -1);
            }
            return new IntActionResultDto(ResultCode.SUCCESS, "Removed all buying types", 0);
        }
        return new IntActionResultDto(ResultCode.ERROR_STORE_BUYING_POLICY_CHANGE, "Only managers can change buying policies in stores.", -1);
    }

    public IntActionResultDto createAdvancedBuyingType(int storeId, List<Integer> buyingTypeIDs, String logicalOperation) {
        if (s.isSubscriber(sessionId) && s.isOwner(sessionId, storeId)) {
            if (!logicalOperation.toLowerCase().equals("xor") && !logicalOperation.toLowerCase().equals("and") && !logicalOperation.toLowerCase().equals("or")
                    && !logicalOperation.toLowerCase().equals("implies")) return new IntActionResultDto(ResultCode.ERROR_STORE_BUYING_POLICY_CHANGE, "Logical operations are: and, or, xor, implies", -1);
            if (logicalOperation.toLowerCase().equals("implies") && buyingTypeIDs.size() != 2) return new IntActionResultDto(ResultCode.ERROR_STORE_BUYING_POLICY_CHANGE, "Implies requires exactly 2 basic constraints", -1);

            return s.addAdvancedBuyingType(storeId, buyingTypeIDs, logicalOperation);
        }
        return new IntActionResultDto(ResultCode.ERROR_STORE_BUYING_POLICY_CHANGE, "Only managers can change buying policies in stores.", -1);
    }


    public BuyingPolicyActionResultDTO viewBuyingPolicies(int sessionId, int storeId) {
        return s.getBuyingPolicyDetails(storeId);
    }

    public ActionResultDTO changeProductPrice(int storeId, int productId, double price) {
        return s.changeProductPrice(storeId, productId, price);
    }

    // editing discount policies

    public IntActionResultDto addSimpleProductDiscount(int storeId, int productId, double salePercentage) {
        if (s.isSubscriber(sessionId) && s.isOwner(sessionId, storeId)) {
            if (salePercentage < 0) return new IntActionResultDto(ResultCode.ERROR_STORE_DISCOUNT_POLICY_CHANGE, "Sale percentage must be non-negative", -1);
            int discountTypeID = 0;
            try {
                discountTypeID = s.addSimpleProductDiscount(storeId, productId, salePercentage);
            } catch (DatabaseFetchException e) {
                return new IntActionResultDto(ResultCode.ERROR_DATABASE, "Could not contact database. Please try again later.", -1);
            }
            return new IntActionResultDto(ResultCode.SUCCESS, "Added discount type " + discountTypeID, discountTypeID);
        }
        return new IntActionResultDto(ResultCode.ERROR_STORE_DISCOUNT_POLICY_CHANGE, "Only managers can change discount policies in stores.", -1);
    }

    public IntActionResultDto addSimpleCategoryDiscount(int storeId, String categoryName, double salePercentage) {
        if (s.isSubscriber(sessionId) && s.isOwner(sessionId, storeId)) {
            int discountTypeID = 0;
            try {
                discountTypeID = s.addSimpleCategoryDiscount(storeId, categoryName, salePercentage);
            } catch (DatabaseFetchException e) {
                return new IntActionResultDto(ResultCode.ERROR_DATABASE, "Could not contact database. Please try again later.", -1);
            }
            return new IntActionResultDto(ResultCode.SUCCESS, "Added discount type " + discountTypeID, discountTypeID);
        }
        return new IntActionResultDto(ResultCode.ERROR_STORE_BUYING_POLICY_CHANGE, "Only managers can change discount policies in stores.", -1);
    }

    public IntActionResultDto createAdvancedDiscountType(int storeId, List<Integer> discountTypeIDs, String logicalOperation) {
        if (s.isSubscriber(sessionId) && s.isOwner(sessionId, storeId)) {
            if (!logicalOperation.toLowerCase().equals("xor") && !logicalOperation.toLowerCase().equals("and") && !logicalOperation.toLowerCase().equals("or"))
                return new IntActionResultDto(ResultCode.ERROR_STORE_DISCOUNT_POLICY_CHANGE, "Logical operations are: and, or, xor", -1);

            return s.addAdvancedDiscountType(storeId, discountTypeIDs, logicalOperation);
        }
        return new IntActionResultDto(ResultCode.ERROR_STORE_DISCOUNT_POLICY_CHANGE, "Only managers can change discount policies in stores.", -1);
    }

    public ActionResultDTO removeDiscountType(int storeId, int discountTypeId) {
        if (s.isSubscriber(sessionId) && s.isOwner(sessionId, storeId)) {
            try {
                s.removeDiscountTypeFromStore(storeId, discountTypeId);
            } catch (DatabaseFetchException e) {
                return new ActionResultDTO(ResultCode.ERROR_DATABASE, "Could not contact database. Please try again later.");
            }
            return new ActionResultDTO(ResultCode.SUCCESS, "Removed discount type " + discountTypeId);
        }
        return new ActionResultDTO(ResultCode.ERROR_STORE_BUYING_POLICY_CHANGE, "Only managers can change discount policies in stores.");
    }

    public ActionResultDTO removeAllDiscountTypes(int storeId) {
        if (s.isSubscriber(sessionId) && s.isOwner(sessionId, storeId)) {
            try {
                s.removeAllDiscountTypes(storeId);
            } catch (DatabaseFetchException e) {
                return new ActionResultDTO(ResultCode.ERROR_DATABASE, "Could not contact database. Please try again later.");
            }
            return new ActionResultDTO(ResultCode.SUCCESS, "Removed all discount types");
        }
        return new ActionResultDTO(ResultCode.ERROR_STORE_BUYING_POLICY_CHANGE, "Only managers can change discount policies in stores.");
    }

    public ActionResultDTO changeDiscountPolicy(int storeId, String newPolicy) {
        if(s.isSubscriber(sessionId) && s.isOwner(sessionId,storeId)){
            return s.changeDiscountPolicy(storeId, newPolicy);
        }
        return new ActionResultDTO(ResultCode.ERROR_STORE_DISCOUNT_POLICY_CHANGE, "Only owners can use this functionality.");
    }

    public DiscountPolicyActionResultDTO viewDiscountPolicies(int sessionId, int storeId) {
        return s.getDiscountPolicyDetails(storeId);
    }

    public ActionResultDTO deleteOwner(int storeId, int userId) {
        if (!s.isOwner(sessionId, storeId) || !s.isSubscriber(sessionId)) return new ActionResultDTO(ResultCode.ERROR_STORE_MANAGER_MODIFICATION, "Only owners can use this functionality.");
        try {
            if (!s.subIsOwner(userId, storeId)) return new ActionResultDTO(ResultCode.ERROR_STORE_MANAGER_MODIFICATION, "The specified subscriber is not a manager.");
        } catch (DatabaseFetchException e) {
            return new ActionResultDTO(ResultCode.ERROR_DATABASE, "Could not contact database. Please try again later.");

        }

        return s.deleteOwner(sessionId,storeId,userId);
    }

    public ActionResultDTO approveStoreOwner(int storeId, int subId) {
        if (!s.isSubscriber(sessionId) || !s.isOwner(sessionId,storeId)) return new ActionResultDTO(ResultCode.ERROR_STORE_OWNER_MODIFICATION, "Only owners can use this functionality.");
        try {
            if (s.subIsOwner(subId, storeId)) return new ActionResultDTO(ResultCode.ERROR_STORE_OWNER_MODIFICATION, "The specified subscriber is already an owner.");
        } catch (DatabaseFetchException e) {
            return new ActionResultDTO(ResultCode.ERROR_DATABASE, "Could not contact database. Please try again later.");

        }
        return s.approveStoreOwner(sessionId,storeId,subId);
    }

    public GrantingResultDTO getAllGrantings(int subId) {
        return s.getAllGrantings(sessionId,subId);
    }

    public ActionResultDTO declineStoreOwner(int storeId, int subId) {
        if (!s.isSubscriber(sessionId) || !s.isOwner(sessionId,storeId)) return new ActionResultDTO(ResultCode.ERROR_STORE_OWNER_MODIFICATION, "Only owners can use this functionality.");
        try {
            if (s.subIsOwner(subId, storeId)) return new ActionResultDTO(ResultCode.ERROR_STORE_OWNER_MODIFICATION, "The specified subscriber is already an owner.");
        } catch (DatabaseFetchException e) {
            return new ActionResultDTO(ResultCode.ERROR_DATABASE, "Could not contact database. Please try again later.");

        }
        return s.declineStoreOwner(sessionId,storeId,subId);
    }
}
