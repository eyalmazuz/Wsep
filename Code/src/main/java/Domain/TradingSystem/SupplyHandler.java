package Domain.TradingSystem;

import java.util.Map;

public class SupplyHandler {
    private final String config;
    private static System system = System.getInstance();

    public SupplyHandler(String config) throws Exception {
        if (config.equals("Error")){
            throw new Exception("Failed To connect Supply Handler");
        }
        this.config = config;
    }

    public boolean requestSupply(int user, Map<Integer, Map<Integer, Integer>> storeProductsIds) {
        // communicate with supply system
        if (config.equals("Mock Config")) {

            // perform any additional checks

            for (Integer store : storeProductsIds.keySet()) {
                system.removeStoreProductSupplies(store, storeProductsIds.get(store));
            }
            return true;
        }
        return false;
    }
}
