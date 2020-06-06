package AcceptanceTest.RegisteredUserTest;

import AcceptanceTest.Data.Database;
import AcceptanceTest.ServiceTest;
import org.junit.After;
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

        int sessionId = startSession();
        login(sessionId, "chika", "12345");
        int sid_1 = openStore(sessionId);
        Database.userToStore.put("chika", sid_1);
        addProdcut(true,sessionId, 1, sid_1, 5);
        addProdcut(true,sessionId, 2, sid_1, 5);
        logout(sessionId);

        login(sessionId, "hanamaru", "12345");
        int sid_2 = openStore(sessionId);
        Database.userToStore.put("hanamaru", sid_2);
        addProdcut(true,sessionId, 2, sid_2, 10);
        logout(sessionId);

        login(sessionId, "you", "12345");
        addToCart(sessionId, Database.userToStore.get("chika"),1, 5);
        addToCart(sessionId, Database.userToStore.get("chika"),2, 5);
        buyCart(sessionId, "Good payment details");

    }

    @After
    public void tearDown(){
        super.tearDown();

//        Database.userToId.clear();
//        Database.userToStore.clear();
    }

    //USE CASES 3.1
    @Test
    public void testLogoutSuccessful(){
        int sessionId = startSession();
        login(sessionId, "you", "12345");
        assertTrue(logout(sessionId));
        login(sessionId, "chika", "12345");
        assertTrue(logout(sessionId));
        login(sessionId, "ruby", "12345");
        assertTrue(logout(sessionId));


    }

    @Test
    public void testLogoutFailedNotLoggedIn(){
        int sessionId = startSession();
        logout(sessionId);
        assertFalse(logout(sessionId));
    }


    //USE CASES 3.2
    @Test
    public void testOpenStoreSuccessful(){
        int sessionId = startSession();
        login(sessionId, "you", "12345");
        assertTrue(openStore(sessionId) > 0);
        assertTrue(openStore(sessionId) > 0);
    }

    @Test
    public void testOpenStoreFailure(){
        int sessionId = startSession();
        logout(sessionId);
        assertEquals(-1, openStore(sessionId));
        assertEquals(-1, openStore(sessionId));
    }


    //USE CASES 3.7
    @Test
    public void testViewPurchaseHistory(){
        int sessionId = startSession();
        login(sessionId, "you", "12345");
        addToCart(sessionId, Database.userToStore.get("chika"),1, 5);
        addToCart(sessionId, Database.userToStore.get("chika"),2, 5);
        buyCart(sessionId, "Good payment details");
        assertNotNull(viewPurchaseHistory(sessionId));
    }

}
