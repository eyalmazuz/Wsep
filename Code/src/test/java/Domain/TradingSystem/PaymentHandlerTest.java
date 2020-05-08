package Domain.TradingSystem;

import Domain.TradingSystem.PaymentHandler;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class PaymentHandlerTest extends TestCase {

    PaymentHandler handler;

    @Test
    public void testMakePayment() {
        try {
            handler = new PaymentHandler("Mock Config");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<Integer, Map<Integer, Integer>> map = new HashMap<>();
        Map<Integer, Integer> productAmounts = new HashMap<>();
        productAmounts.put(4, 5);
        map.put(1, productAmounts);

        // bad payment details
        assertFalse(handler.makePayment(1, "Bad payment details", map));
        assertTrue(handler.makePayment(1, "Ok details", map));
    }

}
