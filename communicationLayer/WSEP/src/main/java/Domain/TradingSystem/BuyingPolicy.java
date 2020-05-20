package Domain.TradingSystem;

import DTOs.ActionResultDTO;
import DTOs.IntActionResultDto;
import DTOs.ResultCode;

import javax.swing.*;
import java.util.*;

public class BuyingPolicy {

    // This class defines a purchase policy for a store

    private String details;

    public BuyingPolicy(String details) {
        this.details = details;
    }
    private Map<Integer, BuyingType> buyingTypes = new HashMap<>();


    Map<String, BuyingType> buyingTypeDictionary = new HashMap<>();

    public ActionResultDTO isAllowed(User user, ShoppingBasket basket) {
        if (details.equals("No one is allowed")) return new ActionResultDTO(ResultCode.ERROR_PURCHASE, "No one is allowed to buy at this store.");
        String buyingPolicyErrors = "";
        boolean error = false;
        for (BuyingType type : buyingTypes.values()) {
            ActionResultDTO buyingConstraintResult = type.canBuy(user, basket);
            if (buyingConstraintResult.getResultCode() != ResultCode.SUCCESS) {
                error = true;
                buyingPolicyErrors += buyingConstraintResult.getDetails() + "\n";
            }
        }
        if (error) return new ActionResultDTO(ResultCode.ERROR_PURCHASE, buyingPolicyErrors);
        return new ActionResultDTO(ResultCode.SUCCESS, null);
    }

    @Override
    public String toString() {
        return "";
    }

    public int addBuyingType(BuyingType type) {
        int id = 0;
        if (!buyingTypes.isEmpty()) id = Collections.max(buyingTypes.keySet()) + 1;
        buyingTypes.put(id, type);
        return id;
    }

    public Map<Integer, BuyingType> getBuyingTypes() {
        return buyingTypes;
    }

    public void clearBuyingTypes() {
        buyingTypes.clear();
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void removeBuyingType(int buyingTypeID) {
        buyingTypes.remove(buyingTypeID);
    }

    public IntActionResultDto addAdvancedBuyingType(List<Integer> buyingTypeIDs, String logicalOperationStr) {
        synchronized (buyingTypes) {
            List<BuyingType> relevantBuyingTypes = new ArrayList<>();
            for (Integer typeID : buyingTypeIDs) {
                if (buyingTypeIDs.get(typeID) == null) return new IntActionResultDto(ResultCode.ERROR_STORE_BUYING_POLICY_CHANGE, "There is no buying type ID" + typeID, -1);
                relevantBuyingTypes.add(buyingTypes.get(typeID));
                buyingTypeIDs.remove(typeID);
            }
            AdvancedBuying.LogicalOperation logicalOperation = AdvancedBuying.LogicalOperation.AND;
            if (logicalOperationStr.toLowerCase().equals("or")) logicalOperation = AdvancedBuying.LogicalOperation.OR;
            else if (logicalOperationStr.toLowerCase().equals("xor")) logicalOperation = AdvancedBuying.LogicalOperation.XOR;
            else if (logicalOperationStr.toLowerCase().equals("implies")) logicalOperation = AdvancedBuying.LogicalOperation.IMPLIES;
            BuyingType advanced = new AdvancedBuying.LogicalBuying(relevantBuyingTypes, logicalOperation);
            return new IntActionResultDto(ResultCode.SUCCESS, null, addBuyingType(advanced));
        }
    }
}
