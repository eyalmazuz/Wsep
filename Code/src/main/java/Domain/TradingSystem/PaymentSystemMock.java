package Domain.TradingSystem;

import java.util.HashMap;
import java.util.Map;

public class PaymentSystemMock {
    public static boolean succeedPurchase = true;
    private static Map<Integer, Map<Integer, Map<Integer, Integer>>> userPurchases = new HashMap<>();


    public static boolean attemptPurchase(int sessionId, String paymentDetails, Map<Integer, Map<Integer, Integer>> storeProductDetails) {
        if (paymentDetails.equals("Bad payment details")) return false;
        if (succeedPurchase) userPurchases.put(sessionId, storeProductDetails);
        return succeedPurchase;
    }

    public static boolean requestRefund(int sessionId, Map<Integer, Map<Integer, Integer>> storeProductDetails) {
        return userPurchases.get(sessionId).equals(storeProductDetails);
    }
}
