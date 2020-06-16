package Domain.TradingSystem;

import Domain.IPaymentSystem;

import java.util.Map;

public class PaymentHandler {
    private final String config;
    private PaymentSystemProxy paymentSystem;

    public PaymentHandler(String config) throws Exception {
        if (config.equals("Error")){
            throw new Exception("Failed To connect Payment Handler");
        }
        paymentSystem = new PaymentSystemProxy();
        if (config.equals("No payments")) paymentSystem.testing = false;
        else paymentSystem.testing = true;
        this.config = config;
    }

    public PaymentHandler(String config, IPaymentSystem paymentSystem) throws Exception {
        this(config);
        this.paymentSystem.setPaymentSystem(paymentSystem);
    }

    // usecase 2.8.3
    // receives external purchase details and a map: (store id -> (product id -> amount))
    public int makePayment(String cardNumber, String expirationMonth, String expirationYear, String holder, String ccv, String cardId) {
        return paymentSystem.attemptPurchase(cardNumber, expirationMonth, expirationYear, holder, ccv, cardId);
    }

    public boolean requestRefund(int transactionId) {
        return paymentSystem.requestRefund(transactionId);
    }

}
