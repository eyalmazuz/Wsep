package Domain.TradingSystem;

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
        int transactionId = paymentSystem.attemptPurchase("12345678", "04", "2021", "me", "777", "123123123");
        assertTrue(transactionId > 0);
    }

    @Test
    public void testRequestRefund() {
        int transactionId = paymentSystem.attemptPurchase("12345678", "04", "2021", "me", "777", "123123123");
        assertTrue(paymentSystem.requestRefund(transactionId));
    }
/*
    @Test
    public void testRequestSupply() {
        int transactionId = supplySystem.attemptSupply("Michael Scott", "1725 Slough Avenue", "Scranton", "PA, United States", "12345");
        assertTrue(transactionId > 0);
    }

    @Test
    public void testCancelSupply() {
        int transactionId = supplySystem.attemptSupply("Michael Scott", "1725 Slough Avenue", "Scranton", "PA, United States", "12345");
        assertTrue(supplySystem.cancelSupply(transactionId));
    }*/

}
