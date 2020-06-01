package Domain.TradingSystem;

import DataAccess.DAOManager;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class PaymentHandlerTest extends TestCase {

    PaymentHandler handler;
    Map<Integer, Map<Integer, Integer>> map;

    @Before
    public void setUp(){
        System.testing = true;

        try {
            handler = new PaymentHandler("Mock Config");
        } catch (Exception e) {
            e.printStackTrace();
        }

        map = new HashMap<>();
        Map<Integer, Integer> productAmounts = new HashMap<>();
        productAmounts.put(4, 5);
        map.put(1, productAmounts);
    }

    @After
    public void tearDown() {
        DAOManager.clearDatabase();
    }

    @Test
    public void testMakePaymentFailure() {
        // bad payment details
        assertFalse(handler.makePayment(1, "Bad payment details", map, 0));
    }

    @Test
    public void testMakePaymentSuccess() {

        assertTrue(handler.makePayment(1, "Ok details", map, 0));
    }



}
