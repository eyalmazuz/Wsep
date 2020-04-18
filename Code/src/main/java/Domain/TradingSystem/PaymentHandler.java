package Domain.TradingSystem;

import java.util.Map;

public class PaymentHandler {
    private final String config;

    public PaymentHandler(String config) {
        this.config = config;
    }

    // receives user object for user details and a map: (store id -> (product id -> amount))
    public boolean makePayment(User user, Map<Integer, Map<Integer, Integer>> storeProductsIds) {
        return true;
    }

    public void cancelPayment(User user, Map<Integer, Map<Integer, Integer>> storeProductsIds) {

    }
}
