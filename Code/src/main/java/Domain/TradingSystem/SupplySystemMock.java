package Domain.TradingSystem;

import java.util.Map;

public class SupplySystemMock {
    public static boolean succeedSupply = true;

    public static boolean requestSupply(int sessionId, Map<Integer, Map<Integer, Integer>> storeProductIds) {
        return succeedSupply;
    }
}
