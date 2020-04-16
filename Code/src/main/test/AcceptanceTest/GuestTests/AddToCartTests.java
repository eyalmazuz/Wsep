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
        login("chika", "12345");
        openStore();
        addProdcut(1, 1,5);
        addProdcut(2, 1,5);
        addProdcut(3, 1,5);
        addProdcut(4, 1,5);
        addProdcut(5, 1,5);
        logout();
        login("hanamaru", "123456");
        addToCart(1, 5);
    }


    @Test
    public void testAddToCartSuccessful(){
        assertTrue(addToCart(2, 5));
        assertTrue(addToCart(3, 5));
        assertTrue(addToCart(4, 5));
        assertTrue(addToCart(5, 100));
    }

    @Test
    public void testAddToCartFailure(){
        assertFalse(addToCart(1, 5));
        assertFalse(addToCart(2, 0));
        assertFalse(addToCart(2, -5));
        assertFalse(addToCart(3, null));
    }
}
