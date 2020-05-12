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
    private List<BuyingType> buyingConstraints = new ArrayList<>();

    public boolean isAllowed(User user, Map<Integer, Integer> productAmounts) {
        if (details.equals("No one is allowed")) return false;
        return true;
    }

    @Override
    public String toString() {
        return "";

    }

    public boolean canBuy() {
        for (BuyingType constraint: buyingConstraints)
            if (!constraint.canBuy()) return false;
        return true;
    }
}
