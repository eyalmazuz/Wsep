package Domain.TradingSystem;

import java.util.Map;

public class SupplySystemProxy implements ISupplySystem {
    private ISupplySystem supplySystem = null;

    public boolean succeedSupply = true;

    public boolean requestSupply(int sessionId, Map<Integer, Map<Integer, Integer>> storeProductIds) {
        if (supplySystem != null) supplySystem.requestSupply(sessionId, storeProductIds);
        return succeedSupply;
    }

    public void setSupplySystem(ISupplySystem supplySystem) {
        this.supplySystem = supplySystem;
    }
}
