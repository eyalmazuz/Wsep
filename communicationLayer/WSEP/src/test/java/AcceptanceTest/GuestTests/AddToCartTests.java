package AcceptanceTest.GuestTests;

import AcceptanceTest.Data.Database;
import AcceptanceTest.ServiceTest;
import org.junit.After;
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

        int sessionId = startSession();
        login(sessionId, "chika", "12345");
        int sid_1 = openStore(sessionId);
        Database.userToStore.put("chika", sid_1);
        addProdcut(true,sessionId, 1, sid_1, 5);
        addProdcut(true,sessionId, 2, sid_1, 5);
        logout(sessionId);

        login(sessionId, "you", "12345");

    }

    @After
    public void tearDown(){
        super.tearDown();

//        Database.userToId.clear();
//        Database.userToStore.clear();
    }



    @Test
    public void testAddToCartSuccessful(){
        int sessionId = startSession();
        assertTrue(addToCart(sessionId, Database.userToStore.get("chika"),1, 5));
        assertTrue(addToCart(sessionId, Database.userToStore.get("chika"),2, 5));
    }

    @Test
    public void testAddToCartFailure(){
        int sessionId = startSession();
        assertFalse(addToCart(sessionId, Database.userToStore.get("chika"),2, 0));
        assertFalse(addToCart(sessionId, Database.userToStore.get("chika"),2, -5));
    }
}
