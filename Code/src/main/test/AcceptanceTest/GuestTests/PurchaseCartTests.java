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
        login("hanamaru", "123456");
        openStore();
        addProdcut(1, 1, 5);
        addProdcut(2, 1, 5);
        editProduct(1, 40, "Food");
        editProduct(2, 50, "Food");
        logout();


    }


    @Test
    public void testPurchaseSuccessful(){
        addToCart(1, 5);
        addToCart(2, 5);
        assertTrue(buyCart(viewCart()));
    }

    @Test
    public void testPurchaseFailureBadPolicy(){
        addToCart(1, 5);
        addToCart(2, 5);
        assertFalse(buyCart(viewCart()));

    }

    @Test
    public void testPurchaseFailureNotEnoughItemsInStore(){
        addToCart(1, 500);
        addToCart(2, 500);
        assertFalse(buyCart(viewCart()));

    }

    @Test
    public void testPurchaseFailureInvalidDetails(){
        addToCart(1, 5);
        addToCart(3, 5);
        assertFalse(buyCart(viewCart()));

    }
}
