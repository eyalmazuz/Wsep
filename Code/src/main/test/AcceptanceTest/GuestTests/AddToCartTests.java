package AcceptanceTest.GuestTests;

import AcceptanceTest.ServiceTest;
import org.junit.Before;
import org.junit.Test;

public class AddToCartTests extends ServiceTest {

    /*
     * USE CASES 2.6
     *
     * */
    @Before
    public void setUp(){
        super.setUp();
        login("you", "12345");

    }


    @Test
    public void testAddToCartSuccessful(){
        assertTrue(addToCart(1, 5));
        assertTrue(addToCart(2, 5));
    }

    @Test
    public void testAddToCartFailure(){
        assertFalse(addToCart(1, 15));
        assertFalse(addToCart(2, 0));
        assertFalse(addToCart(2, -5));
        assertFalse(addToCart(3, null));
    }
}
