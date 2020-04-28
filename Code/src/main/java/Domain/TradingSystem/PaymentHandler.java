package Domain.TradingSystem;

import java.util.Map;

public class PaymentHandler {
    private final String config;

    public PaymentHandler(String config) throws Exception {
        if (config.equals("Error")){
            throw new Exception("Failed To connect Payment Handler");
        }
        this.config = config;
    }

    // usecase 2.8.3
    // receives external purchase details and a map: (store id -> (product id -> amount))
    public boolean makePayment(int sessionId, String paymentDetails, Map<Integer, Map<Integer, Integer>> storeProductsIds) {
        if (paymentDetails.equals("Bad payment details")) return false;
        if (config.equals("Mock Config")) return true;
        return false;
    }

    public boolean requestRefund(int sessionId, Map<Integer, Map<Integer, Integer>> storeProductsIds) {
        if (config.equals("Mock Config")) return true;
        return false;
    }
}
