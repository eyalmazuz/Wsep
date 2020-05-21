package Domain.TradingSystem;

import java.util.Map;

public class PaymentHandler {
    private final String config;
    private PaymentSystemProxy paymentSystem;

    public PaymentHandler(String config) throws Exception {
        if (config.equals("Error")){
            throw new Exception("Failed To connect Payment Handler");
        }
        paymentSystem = new PaymentSystemProxy();
        if (config.equals("No payments")) paymentSystem.succeedPurchase = false;
        else paymentSystem.succeedPurchase = true;
        this.config = config;
    }

    public PaymentHandler(String config, IPaymentSystem paymentSystem) throws Exception {
        this(config);
        this.paymentSystem.setPaymentSystem(paymentSystem);
    }

    public void setProxyPurchaseSuccess(boolean success) {
        paymentSystem.succeedPurchase = success;
    }

    // usecase 2.8.3
    // receives external purchase details and a map: (store id -> (product id -> amount))
    public boolean makePayment(int sessionId, String paymentDetails, Map<Integer, Map<Integer, Integer>> storeProductsIds, double price) {
        return paymentSystem.attemptPurchase(sessionId, paymentDetails, storeProductsIds, price);
    }

    public boolean requestRefund(int sessionId, Map<Integer, Map<Integer, Integer>> storeProductsIds) {
        return paymentSystem.requestRefund(sessionId, storeProductsIds);
    }

}
