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

        login(Database.sessionId, "chika", "12345");
        int sid_1 = openStore(Database.sessionId);
        Database.userToStore.put("chika", sid_1);
        addProdcut(true,Database.sessionId, 1, sid_1, 5);
        addProdcut(true,Database.sessionId, 2, sid_1, 5);
        logout(Database.sessionId);

        login(Database.sessionId, "you", "12345");

    }

    @After
    public void tearDown(){
        Database.userToId.clear();
        Database.userToStore.clear();
    }



    @Test
    public void testAddToCartSuccessful(){
        assertTrue(addToCart(Database.sessionId, Database.userToStore.get("chika"),1, 5));
        assertTrue(addToCart(Database.sessionId, Database.userToStore.get("chika"),2, 5));
    }

    @Test
    public void testAddToCartFailure(){
        assertFalse(addToCart(Database.sessionId, Database.userToStore.get("chika"),2, 0));
        assertFalse(addToCart(Database.sessionId, Database.userToStore.get("chika"),2, -5));
    }
}
