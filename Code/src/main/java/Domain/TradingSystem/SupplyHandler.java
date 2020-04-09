package Domain.TradingSystem;

import java.util.Map;

public class SupplyHandler {
    private final String config;

    public SupplyHandler(String config) {
        this.config = config;
    }

    public void requestSupply(User user, Map<Integer, Map<Integer, Integer>> storeProductsIds) {
        // communicate with supply system
    }
}
