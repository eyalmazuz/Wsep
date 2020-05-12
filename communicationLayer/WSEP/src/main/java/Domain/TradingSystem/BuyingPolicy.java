package Domain.TradingSystem;

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

    public boolean isAllowed(User user, ShoppingBasket basket) {
        if (details.equals("No one is allowed")) return false;
        for (BuyingType type : buyingTypes) {
            if (!type.canBuy(user, basket)) return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "";
    }

    public void addBuyingType(BuyingType type) {
        buyingTypes.add(type);
    }

    public void clearBuyingTypes() {
        buyingTypes.clear();
    }
}
