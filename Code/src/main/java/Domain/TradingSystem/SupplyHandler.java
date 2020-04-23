package Domain.TradingSystem;

import java.util.Map;

public class SupplyHandler {
    private final String config;

    public SupplyHandler(String config) throws Exception {
        if (config.equals("Error")){
            throw new Exception();
        }
        this.config = config;
    }

    public boolean requestSupply(int user, Map<Integer, Map<Integer, Integer>> storeProductsIds) {
        // communicate with supply system
        if (config.equals("Mock Config")) return true;
        return false;
    }
}
