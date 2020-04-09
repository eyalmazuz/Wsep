package AcceptanceTest.GuestTests;

import AcceptanceTest.ServiceTest;
import org.junit.Before;
import org.junit.Test;

public class PurchaseCartTests extends ServiceTest {
    /*
     * USE CASES 2.8.1-2.8.4
     *
     * */
    @Before
    public void setUp(){
        super.setUp();
    }


    @Test
    public void testPurchaseSuccessful(){
        assertTrue(buyCart("bob", null));
    }

    @Test
    public void testPurchaseFailureBadPolicy(){
        assertFalse(buyCart("danny", null));

    }

    @Test
    public void testPurchaseFailureNotEnoughItemsInStore(){
        assertFalse(buyCart("bob", null));

    }

    @Test
    public void testPurchaseFailureInvalidDetails(){
        assertFalse(buyCart("bob", null));

    }
}
