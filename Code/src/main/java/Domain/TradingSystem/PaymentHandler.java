package Domain.TradingSystem;

import java.util.Map;

public class PaymentHandler {
    private final String config;

    public PaymentHandler(String config) {
        this.config = config;
    }

    // usecase 2.8.3
    // receives external purchase details for user details and a map: (store id -> (product id -> amount))
    public boolean makePayment(String paymentDetails, Map<Integer, Map<Integer, Integer>> storeProductsIds) {
        if (config.equals("Mock Config")) return true;
        return false;
    }

    public boolean cancelPayment(User user, Map<Integer, Map<Integer, Integer>> storeProductsIds) {
        if (config.equals("Mock Config")) return true;
        return false;
    }
}
