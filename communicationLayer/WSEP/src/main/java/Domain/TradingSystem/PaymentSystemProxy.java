package Domain.TradingSystem;

import Domain.IPaymentSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentSystemProxy implements IPaymentSystem {

    private IPaymentSystem paymentSystem = null;

    public static boolean testing = false;
    public static boolean succedPurchase = true;

    private static int fakeTransactionId = 10000;

    @Override
    public boolean handshake() {
        if (paymentSystem == null) return true;
        return paymentSystem.handshake();
    }

    public int attemptPurchase(String cardNumber, String expirationMonth, String expirationYear, String holder, String ccv, String cardId) {
        if (testing) {
            int transactionId = -1;
            if (succedPurchase) {
                transactionId = fakeTransactionId;
                fakeTransactionId++;
            }
            return transactionId;
        }

        if (paymentSystem != null && paymentSystem.handshake()) return paymentSystem.attemptPurchase(cardNumber, expirationMonth, expirationYear, holder, ccv, cardId);
        return -1;
    }

    public boolean requestRefund(int transactionId) {
        if (testing) {
            return true;
        }

        if (paymentSystem != null && paymentSystem.handshake()) return paymentSystem.requestRefund(transactionId);

        return false;
    }

    public void setPaymentSystem(IPaymentSystem paymentSystem) {
        this.paymentSystem = paymentSystem;
    }
}
