package Domain.TradingSystem;

import java.util.Map;

public class PaymentHandler {
    private final String config;

    public PaymentHandler(String config) throws Exception {
        if (config.equals("Error")){
            throw new Exception("Failed To connect Payment Handler");
        }
        if (config.equals("No payments")) PaymentSystemMock.succeedPurchase = false;
        else PaymentSystemMock.succeedPurchase = true;
        this.config = config;
    }

    // usecase 2.8.3
    // receives external purchase details and a map: (store id -> (product id -> amount))
    public boolean makePayment(int sessionId, String paymentDetails, Map<Integer, Map<Integer, Integer>> storeProductsIds) {
        return PaymentSystemMock.attemptPurchase(sessionId, paymentDetails, storeProductsIds);
    }

    public boolean requestRefund(int sessionId, Map<Integer, Map<Integer, Integer>> storeProductsIds) {
        return PaymentSystemMock.requestRefund(sessionId, storeProductsIds);
    }
}