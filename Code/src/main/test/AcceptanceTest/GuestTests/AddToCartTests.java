package AcceptanceTest.GuestTests;

import AcceptanceTest.ServiceTest;
import org.junit.Before;
import org.junit.Test;

public class AddToCartTests extends ServiceTest {


    @Before
    public void setUp(){
        super.setUp();
    }


    @Test
    public void testAddToCartSuccessful(){
        assertTrue(addToCart("Food", 5));
        assertTrue(addToCart("Tablet", 5));
        assertTrue(addToCart("KB", 5));
        assertTrue(addToCart("UO", 5));
        assertTrue(addToCart("Aqours dome tour single ", 100));
    }

    @Test
    public void testAddToCartFailure(){
        assertFalse(addToCart("Iphone", 5));
        assertFalse(addToCart("Reasons to live", 0));
        assertFalse(addToCart("Life", null));
    }
}
