package Domain.TradingSystem;

import java.util.Map;

public interface IPaymentSystem {
    boolean attemptPurchase(int sessionId, String paymentDetails, Map<Integer, Map<Integer, Integer>> storeProductDetails);
    boolean requestRefund(int sessionId, Map<Integer, Map<Integer, Integer>> storeProductDetails);
}