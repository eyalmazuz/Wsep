package Domain.TradingSystem;

import DTOs.ActionResultDTO;
import DTOs.ResultCode;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BuyingPolicy {

    // This class defines a purchase policy for a store

    private String details;

    public BuyingPolicy(String details) {
        this.details = details;
    }
    private List<BuyingType> buyingTypes = new ArrayList<>();

    public ActionResultDTO isAllowed(User user, ShoppingBasket basket) {
        if (details.equals("No one is allowed")) return new ActionResultDTO(ResultCode.ERROR_PURCHASE, "No one is allowed to buy at this store.");
        String buyingPolicyErrors = "";
        boolean error = false;
        for (BuyingType type : buyingTypes) {
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

    public void addBuyingType(BuyingType type) {
        buyingTypes.add(type);
    }

    public List<BuyingType> getBuyingTypes() {
        return buyingTypes;
    }

    public void clearBuyingTypes() {
        buyingTypes.clear();
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
