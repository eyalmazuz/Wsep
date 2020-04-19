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
        addToCart(1,1, 5);
        addToCart(1,2, 5);
        //TODO FIX THIS
        assertTrue(buyCart(viewCart()));
    }

    @Test
    public void testPurchaseFailureBadPolicy(){
        addToCart(1,1, 5);
        addToCart(1,2, 5);
        //TODO FIX THIS
        assertFalse(buyCart(viewCart()));

    }

    @Test
    public void testPurchaseFailureNotEnoughItemsInStore(){
        addToCart(1,1, 500);
        addToCart(1,2, 500);
        //TODO FIX THIS
        assertFalse(buyCart(viewCart()));

    }

    @Test
    public void testPurchaseFailureInvalidDetails(){
        addToCart(1,1, 5);
        addToCart(1,3, 5);
        //TODO FIX THIS
        assertFalse(buyCart(viewCart()));

    }
}
