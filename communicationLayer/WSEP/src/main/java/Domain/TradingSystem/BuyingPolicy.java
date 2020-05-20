package Domain.TradingSystem;

import DTOs.ActionResultDTO;
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
}
