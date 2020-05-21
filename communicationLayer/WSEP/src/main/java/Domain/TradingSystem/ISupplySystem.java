package Domain.TradingSystem;

import java.util.Map;

public interface ISupplySystem {
    boolean requestSupply(int sessionId, Map<Integer, Map<Integer, Integer>> storeProductIds);
}
