package AcceptanceTest.RegisteredUserTest;

import AcceptanceTest.ServiceTest;
import org.junit.Before;
import org.junit.Test;

public class UserActionsTests extends ServiceTest {
    /*
     * USE CASES 3.1-3.2,3.7
     *
     * */
    @Before
    public void setUp(){
        super.setUp();
        login("you", "12345");
        addToCart(1, 5);
        addToCart(2, 5);
        buyCart(viewCart());

    }

    //USE CASES 3.1
    @Test
    public void testLogout(){
        assertTrue(logout());
    }


    //USE CASES 3.2
    @Test
    public void testOpenStoreSuccessful(){
        assertTrue(openStore());
        assertTrue(openStore());
    }

    //USE CASES 3.7
    @Test
    public void testViewPurchaseHistory(){

        assertNotNull(viewPurchaseHistory());
    }

}
