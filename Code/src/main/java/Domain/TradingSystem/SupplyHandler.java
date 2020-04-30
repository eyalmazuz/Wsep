package Domain.TradingSystem;

import java.util.Map;

public class SupplyHandler {
    private final String config;

    public SupplyHandler(String config) throws Exception {
        if (config.equals("Error")){
            throw new Exception("Failed To connect Supply Handler");
        }
        if (config.equals("No supplies")) SupplySystemMock.succeedSupply = false;
        else SupplySystemMock.succeedSupply = true;
        this.config = config;
    }

    public boolean requestSupply(int sessionid, Map<Integer, Map<Integer, Integer>> storeProductsIds) {
        // communicate with supply system
        return SupplySystemMock.requestSupply(sessionid, storeProductsIds);
    }
}
