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

    }

    //USE CASES 3.1
    @Test
    public void testLogout(){
        login("chika", "12345");
        assertTrue(logout());
    }


    //USE CASES 3.2
    @Test
    public void testOpenStoreSuccessful(){
        login("chika", "12345");
        assertTrue(openStore());
    }

    //USE CASES 3.7
    @Test
    public void testViewPurchaseHistory(){
        login("chika", "12345");
        addToCart("Aqours KB", 5);
        addToCart("UO", 200);
        buyCart("chika", viewCart());
        assertNotNull(viewPurchaseHistory());
    }

}
