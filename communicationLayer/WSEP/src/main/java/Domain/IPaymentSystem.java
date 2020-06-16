package Domain;

import DTOs.ActionResultDTO;
import DTOs.IntActionResultDto;

public interface IPaymentSystem {

    boolean handshake();

    IntActionResultDto attemptPurchase(String cardNumber, String expirationMonth, String expirationYear, String holder, String ccv, String cardId);
    ActionResultDTO requestRefund(int transactionId);
}