package Domain.TradingSystem;

import java.util.Map;

public class SupplyHandler {
    private final String config;
    private SupplySystemProxy supplySystemProxy;

    public SupplyHandler(String config) throws Exception {
        if (config.equals("Error")){
            throw new Exception("Failed To connect Supply Handler");
        }
        supplySystemProxy = new SupplySystemProxy();
        if (config.equals("No supplies")) supplySystemProxy.succeedSupply = false;
        else supplySystemProxy.succeedSupply = true;
        this.config = config;
    }

    public SupplyHandler(String config, ISupplySystem supplySystem) throws Exception {
        this(config);
        this.supplySystemProxy.setSupplySystem(supplySystem);
    }

    public void setProxySupplySuccess(boolean success) {
        supplySystemProxy.succeedSupply = success;
    }

    public boolean requestSupply(int sessionid, Map<Integer, Map<Integer, Integer>> storeProductsIds) {
        // communicate with supply system
        return supplySystemProxy.requestSupply(sessionid, storeProductsIds);
    }
}
