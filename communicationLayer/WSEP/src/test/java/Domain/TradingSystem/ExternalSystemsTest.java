package Domain.TradingSystem;

import DTOs.ResultCode;
import Domain.BGUExternalSystems.PaymentSystem;
import Domain.BGUExternalSystems.SupplySystem;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

public class ExternalSystemsTest extends TestCase {

    PaymentSystem paymentSystem;
    SupplySystem supplySystem;

    @Before
    public void setUp() {
        paymentSystem = new PaymentSystem();
        supplySystem = new SupplySystem();
    }

    @Test
    public void testHandShake() {
        assertTrue(paymentSystem.handshake());
    }

    @Test
    public void testAttemptPurchase() {
        int transactionId = paymentSystem.attemptPurchase("12345678", "04", "2021", "me", "777", "123123123").getId();
        assertTrue(transactionId > 0);
    }

    @Test
    public void testRequestRefund() {
        int transactionId = paymentSystem.attemptPurchase("12345678", "04", "2021", "me", "777", "123123123").getId();
        assertEquals(paymentSystem.requestRefund(transactionId).getResultCode(), ResultCode.SUCCESS);
    }

    @Test
    public void testRequestSupply() {
        boolean success = supplySystem.requestSupply("Michael Scott", "1725 Slough Avenue", "Scranton", "PA, United States", "12345").getResultCode() == ResultCode.SUCCESS;
        assertTrue(success);
    }

    @Test
    public void testCancelSupply() {
        int transactionId = supplySystem.requestSupply("Michael Scott", "1725 Slough Avenue", "Scranton", "PA, United States", "12345").getId();
        assertEquals(supplySystem.cancelSupply(transactionId).getResultCode(), ResultCode.SUCCESS);
    }

    @Test
    public void testPaymentContactFail() {
        PaymentSystem.loseContact = true;
        assertFalse(paymentSystem.handshake());
        assertSame(paymentSystem.attemptPurchase("12345678", "04", "2021", "me", "777", "123123123").getResultCode(),
                ResultCode.ERROR_PAYMENT_SYSTEM_UNAVAILABLE);
        assertSame(paymentSystem.requestRefund(123).getResultCode(), ResultCode.ERROR_PAYMENT_SYSTEM_UNAVAILABLE);
        PaymentSystem.loseContact = false;
    }

    @Test
    public void testSupplyContactFail() {
        SupplySystem.loseContact = true;
        assertFalse(supplySystem.handshake());
        assertSame(supplySystem.requestSupply("Michael Scott", "1725 Slough Avenue", "Scranton", "PA, United States", "12345").getResultCode(),
                ResultCode.ERROR_SUPPLY_SYSTEM_UNAVAILABLE);
        assertSame(supplySystem.cancelSupply(123).getResultCode(), ResultCode.ERROR_SUPPLY_SYSTEM_UNAVAILABLE);
        SupplySystem.loseContact = false;
    }

}
