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
    private BuyingType constraint;

    public boolean isAllowed(User user, int storeId) {
        if (details.equals("No one is allowed")) return false;
        if (constraint == null) return true;
        return constraint.canBuy(user, storeId);
    }

    @Override
    public String toString() {
        return "";
    }

    public void setConstraint(BuyingType constraint) {
        this.constraint = constraint;
    }

}
