package Domain.TradingSystem;

import java.util.HashMap;
import java.util.Map;

public class PaymentSystemProxy implements IPaymentSystem {

    private IPaymentSystem paymentSystem = null;

    public boolean succeedPurchase = true;
    private static Map<Integer, Map<Integer, Map<Integer, Integer>>> userPurchases = new HashMap<>();


    public boolean attemptPurchase(int sessionId, String paymentDetails, Map<Integer, Map<Integer, Integer>> storeProductDetails) {
        if (paymentSystem != null) return paymentSystem.attemptPurchase(sessionId, paymentDetails, storeProductDetails);

        if (paymentDetails.equals("Bad payment details")) return false;
        if (succeedPurchase) userPurchases.put(sessionId, storeProductDetails);
        return succeedPurchase;
    }

    public boolean requestRefund(int sessionId, Map<Integer, Map<Integer, Integer>> storeProductDetails) {
        if (paymentSystem != null) return paymentSystem.requestRefund(sessionId, storeProductDetails);

        return userPurchases.get(sessionId).equals(storeProductDetails);
    }

    public void setPaymentSystem(IPaymentSystem paymentSystem) {
        this.paymentSystem = paymentSystem;
    }
}
