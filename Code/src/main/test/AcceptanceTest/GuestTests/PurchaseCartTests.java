package AcceptanceTest.GuestTests;

import AcceptanceTest.Data.Database;
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
