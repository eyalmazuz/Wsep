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



    }

    @After
    public void tearDown(){
//        Database.userToId.clear();
//        Database.userToStore.clear();
    }



    @Test
    public void testPurchaseSuccessful(){

        setupSystem("Mock Config", "Mock Config");
        Database.sessionId = startSession();

        login(Database.sessionId, "chika", "12345");
        changeBuyingPolicy(Database.sessionId, true, Database.userToStore.get("chika"),"Any");

        logout(Database.sessionId);

        addToCart(Database.sessionId, Database.userToStore.get("chika"),1, 5);
        addToCart(Database.sessionId, Database.userToStore.get("chika"),2, 5);
        assertTrue(buyCart(Database.sessionId, "Good payment details"));
    }

    @Test
    public void testPurchaseFailureBadPolicy(){

        setupSystem("Mock Config", "Mock Config");
        Database.sessionId = startSession();

        login(Database.sessionId, "chika", "12345");
        changeBuyingPolicy(Database.sessionId, true, Database.userToStore.get("chika"),"No one is allowed");

        logout(Database.sessionId);

        addToCart(Database.sessionId, Database.userToStore.get("chika"),1, 5);
        addToCart(Database.sessionId, Database.userToStore.get("chika"),2, 5);
        assertFalse(buyCart(Database.sessionId, "Good payment details"));

    }

    @Test
    public void testPurchaseFailureNotEnoughItemsInStore(){

        setupSystem("Mock Config", "Mock Config");
        Database.sessionId = startSession();

        addToCart(Database.sessionId, Database.userToStore.get("chika"),1, 500);
        addToCart(Database.sessionId, Database.userToStore.get("chika"),2, 500);
        assertFalse(buyCart(Database.sessionId, "Good payment details"));

    }

    @Test
    public void testPurchaseFailureInvalidDetails(){

        setupSystem("Mock Config", "Mock Config");
        Database.sessionId = startSession();

        addToCart(Database.sessionId, Database.userToStore.get("chika"),1, 5);
        addToCart(Database.sessionId, Database.userToStore.get("chika"),3, 5);
        assertFalse(buyCart(Database.sessionId, "Bad payment details"));

    }

    @Test
    public void testPurchaseFailedSupplySystem(){

        setupSystem("No supplies", "Mock Config");
        Database.sessionId = startSession();

        addToCart(Database.sessionId, Database.userToStore.get("chika"),1, 5);
        addToCart(Database.sessionId, Database.userToStore.get("chika"),2, 5);
        assertFalse(buyCart(Database.sessionId, "Good payment details"));
    }

    @Test
    public void testPurchaseFailedPaymentSystem(){

        setupSystem("Mock Config", "No payments");
        Database.sessionId = startSession();

        addToCart(Database.sessionId, Database.userToStore.get("chika"),1, 5);
        addToCart(Database.sessionId, Database.userToStore.get("chika"),2, 5);
        assertFalse(buyCart(Database.sessionId, "Good payment details"));
    }


}