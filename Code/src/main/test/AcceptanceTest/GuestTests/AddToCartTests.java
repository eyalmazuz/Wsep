package AcceptanceTest.GuestTests;

import AcceptanceTest.Data.Database;
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
        addToCart(1,1, 5);
        assertEquals(Database.Cart1, viewCart());
        addToCart(1,2, 5);
        assertEquals(Database.Cart1, viewCart());
    }

    @Test
    public void testAddToCartFailure(){
        addToCart(1,1, 15);
        assertEquals(Database.Cart0, viewCart());
        addToCart(1,2, 0);
        assertEquals(Database.Cart0, viewCart());
        addToCart(1,2, -5);
        assertEquals(Database.Cart0, viewCart());
        addToCart(1,3, null);
        assertEquals(Database.Cart0, viewCart());
    }
}
