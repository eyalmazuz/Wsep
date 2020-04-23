package AcceptanceTest.GuestTests;

import AcceptanceTest.Data.Database;
import AcceptanceTest.ServiceTest;
import org.junit.After;
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

        login(Database.sessionId, "chika", "12345");
        int sid_1 = openStore(Database.sessionId);
        Database.userToStore.put("chika", sid_1);
        addProdcut(true,Database.sessionId, 1, sid_1, 5);
        addProdcut(true,Database.sessionId, 2, sid_1, 5);
        logout(Database.sessionId);

        login(Database.sessionId, "hanamaru", "12345");
        int sid_2 = openStore(Database.sessionId);
        Database.userToStore.put("hanamaru", sid_2);
        addProdcut(true,Database.sessionId, 2, sid_2, 10);
        logout(Database.sessionId);


    }

    @After
    public void tearDown(){
        Database.userToId.clear();
        Database.userToStore.clear();
    }



    @Test
    public void testPurchaseSuccessful(){
        addToCart(Database.sessionId, Database.userToStore.get("chika"),1, 5);
        addToCart(Database.sessionId, Database.userToStore.get("chika"),2, 5);
        assertTrue(buyCart(Database.sessionId));
    }

    // TESTS HERE SUPPOSE TO FAIL CAUSE NO IMPLEMENTATION YET
    @Test
    public void testPurchaseFailureBadPolicy(){
        addToCart(Database.sessionId, Database.userToStore.get("chika"),1, 5);
        addToCart(Database.sessionId, Database.userToStore.get("chika"),2, 5);
        assertFalse(buyCart(Database.sessionId));

    }

    @Test
    public void testPurchaseFailureNotEnoughItemsInStore(){
        addToCart(Database.sessionId, Database.userToStore.get("chika"),1, 500);
        addToCart(Database.sessionId, Database.userToStore.get("chika"),2, 500);
        assertFalse(buyCart(Database.sessionId));

    }

    @Test
    public void testPurchaseFailureInvalidDetails(){
        addToCart(Database.sessionId, Database.userToStore.get("chika"),1, 5);
        addToCart(Database.sessionId, Database.userToStore.get("chika"),3, 5);
        assertFalse(buyCart(Database.sessionId));

    }
}
