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
        login(Database.sessionId, "you", "12345");
        addToCart(Database.sessionId, Database.userToStore.get("chika"),1, 5);
        addToCart(Database.sessionId, Database.userToStore.get("chika"),2, 5);
        buyCart(Database.sessionId);

    }

    //USE CASES 3.1
    @Test
    public void testLogoutSuccessful(){
        login(Database.sessionId, "you", "12345");
        assertTrue(logout(Database.sessionId));
        login(Database.sessionId, "chika", "12345");
        assertTrue(logout(Database.sessionId));
        login(Database.sessionId, "ruby", "12345");
        assertTrue(logout(Database.sessionId));


    }

    @Test
    public void testLogoutFailedNotLoggedIn(){
        logout(Database.sessionId);
        assertFalse(logout(Database.sessionId));
    }


    //USE CASES 3.2
    @Test
    public void testOpenStoreSuccessful(){
        login(Database.sessionId, "you", "12345");
        assertTrue(openStore(Database.sessionId) > 0);
        assertTrue(openStore(Database.sessionId) > 0);
    }

    @Test
    public void testOpenStoreFailure(){
        logout(Database.sessionId);
        assertEquals(-1, openStore(Database.sessionId));
        assertEquals(-1, openStore(Database.sessionId));
    }


    //USE CASES 3.7
    @Test
    public void testViewPurchaseHistory(){
        login(Database.sessionId, "you", "12345");
        addToCart(Database.sessionId, Database.userToStore.get("chika"),1, 5);
        addToCart(Database.sessionId, Database.userToStore.get("chika"),2, 5);
        buyCart(Database.sessionId);
        assertNotNull(viewPurchaseHistory(Database.sessionId));
    }

}
