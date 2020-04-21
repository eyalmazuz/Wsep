package Domain.TradingSystem;

import java.util.Map;

public class SupplyHandler {
    private final String config;

    public SupplyHandler(String config) {
        this.config = config;
    }

    public boolean requestSupply(User user, Map<Integer, Map<Integer, Integer>> storeProductsIds) {
        // communicate with supply system
        if (config.equals("Mock Config")) return true;
        return false;
    }
}
