package Domain.TradingSystem;

import DTOs.ActionResultDTO;
import DTOs.IntActionResultDto;
import DTOs.ResultCode;
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
        this.config = config;
    }

    public PaymentHandler(String config, IPaymentSystem paymentSystem) throws Exception {
        this(config);
        this.paymentSystem.setPaymentSystem(paymentSystem);
    }

    // usecase 2.8.3
    // receives external purchase details and a map: (store id -> (product id -> amount))
    public IntActionResultDto makePayment(String cardNumber, String expirationMonth, String expirationYear, String holder, String ccv, String cardId) {
        if (config.equals("No payments")) return new IntActionResultDto(ResultCode.ERROR_PAYMENT_DENIED, "Payment system config does not allow payments.", -1);
        return paymentSystem.attemptPurchase(cardNumber, expirationMonth, expirationYear, holder, ccv, cardId);
    }

    public ActionResultDTO requestRefund(int transactionId) {
        return paymentSystem.requestRefund(transactionId);
    }

}
