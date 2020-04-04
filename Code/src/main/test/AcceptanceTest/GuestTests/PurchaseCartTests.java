package AcceptanceTest.GuestTests;

import AcceptanceTest.ServiceTest;
import org.junit.Before;
import org.junit.Test;

public class PurchaseCartTests extends ServiceTest {

    @Before
    public void setUp(){
        super.setUp();
    }


    @Test
    public void testPurchaseSuccessful(){
        assertTrue(buyCart("bob", "Name: Iphone 11, amount: 30"));
    }

    @Test
    public void testPurchaseFailureBadPolicy(){
        assertFalse(buyCart("danny", "Name: Iphone 11, amount: 30"));

    }

    @Test
    public void testPurchaseFailureNotEnoughItemsInStore(){
        assertFalse(buyCart("bob", "Name: Iphone 11, amount: 30000"));

    }

    @Test
    public void testPurchaseFailureInvalidDetails(){
        assertFalse(buyCart("bob", "Name: Iphonasaasd 11, amount: 30"));

    }
}
