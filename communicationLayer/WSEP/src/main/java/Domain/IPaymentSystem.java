package Domain;

import java.util.Map;

public interface IPaymentSystem {

    boolean handshake();

    int attemptPurchase(String cardNumber, String expirationMonth, String expirationYear, String holder, String ccv, String cardId);
    boolean requestRefund(int transactionId);
}