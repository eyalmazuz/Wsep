package Domain.BGUExternalSystems;

import Domain.ISupplySystem;

import java.util.Map;

public class SupplySystem implements ISupplySystem {
    @Override
    public boolean requestSupply(int sessionId, Map<Integer, Map<Integer, Integer>> storeProductIds) {
        return false;
    }
}
