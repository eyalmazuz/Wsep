package AcceptanceTest.RegisteredUserTest;

import AcceptanceTest.Data.Database;
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
        addToCart(Database.userToStore.get("chika"),1, 5);
        addToCart(Database.userToStore.get("chika"),2, 5);
        buyCart();

    }

    //USE CASES 3.1
    @Test
    public void testLogoutSuccessful(){
        login("you", "12345");
        assertTrue(logout());
        login("chika", "12345");
        assertTrue(logout());
        login("ruby", "12345");
        assertTrue(logout());


    }

    @Test
    public void testLogoutFailedNotLoggedIn(){
        assertFalse(logout());
    }


    //USE CASES 3.2
    @Test
    public void testOpenStoreSuccessful(){
        login("you", "12345");
        assertTrue(openStore() > 0);
        assertTrue(openStore() > 0);
    }

    @Test
    public void testOpenStoreFailure(){
        assertEquals(-1, openStore());
        assertEquals(-1, openStore());
    }


    //USE CASES 3.7
    @Test
    public void testViewPurchaseHistory(){
        login("you", "12345");
        addToCart(Database.userToStore.get("chika"),1, 5);
        addToCart(Database.userToStore.get("chika"),2, 5);
        buyCart();
        assertNotNull(viewPurchaseHistory());
    }

}
