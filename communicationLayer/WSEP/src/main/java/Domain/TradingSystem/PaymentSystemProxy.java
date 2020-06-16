package Domain.TradingSystem;

import DTOs.ActionResultDTO;
import DTOs.IntActionResultDto;
import DTOs.ResultCode;
import Domain.IPaymentSystem;

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

    public IntActionResultDto attemptPurchase(String cardNumber, String expirationMonth, String expirationYear, String holder, String ccv, String cardId) {
        if (testing) {
            int transactionId = -1;
            if (succedPurchase) {
                transactionId = fakeTransactionId;
                fakeTransactionId++;
            }
            if (transactionId != -1) return new IntActionResultDto(ResultCode.SUCCESS, null, transactionId);
            else return new IntActionResultDto(ResultCode.ERROR_PAYMENT_DENIED, null, transactionId);
        }

        if (paymentSystem != null && paymentSystem.handshake()) {
            IntActionResultDto result = paymentSystem.attemptPurchase(cardNumber, expirationMonth, expirationYear, holder, ccv, cardId);
            boolean success = result.getResultCode() == ResultCode.SUCCESS;
            int transactionId = result.getId();
            return success? new IntActionResultDto(ResultCode.SUCCESS, null, transactionId) : new IntActionResultDto(ResultCode.ERROR_PAYMENT_DENIED, "Payment system denied transaction.", -1);
        }
        return new IntActionResultDto(ResultCode.ERROR_PAYMENT_SYSTEM_UNAVAILABLE, "Could not contact the payment system. Try again later.", -1);
    }

    public ActionResultDTO requestRefund(int transactionId) {
        if (testing) {
            return new ActionResultDTO(ResultCode.SUCCESS, null);
        }

        if (paymentSystem != null && paymentSystem.handshake()) {
            boolean success = paymentSystem.requestRefund(transactionId).getResultCode() == ResultCode.SUCCESS;
            return success? new ActionResultDTO(ResultCode.SUCCESS, null) : new ActionResultDTO(ResultCode.ERROR_PAYMENT_DENIED, "Payment system denied refund.");
        }

        return new ActionResultDTO(ResultCode.ERROR_PAYMENT_SYSTEM_UNAVAILABLE, "Could not contact the payment system. Try again later.");
    }

    public void setPaymentSystem(IPaymentSystem paymentSystem) {
        this.paymentSystem = paymentSystem;
    }
}
