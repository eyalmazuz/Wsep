package AcceptanceTest.GuestTests;

import AcceptanceTest.Data.Database;
import AcceptanceTest.ServiceTest;
import DataAccess.DAOManager;
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
    }

    @After
    public void tearDown(){
        super.tearDown();

//        Database.userToId.clear();
//        Database.userToStore.clear();
    }



    @Test
    public void testPurchaseSuccessful(){

        int sessionId = startSession();
        setupSystem("Mock Config", "Mock Config","");
        sessionId = startSession();

        login(sessionId, "chika", "12345");
        changeBuyingPolicy(sessionId, true, Database.userToStore.get("chika"),"Any");

        logout(sessionId);

        addToCart(sessionId, Database.userToStore.get("chika"),1, 5);
        addToCart(sessionId, Database.userToStore.get("chika"),2, 5);
        assertTrue(buyCart(sessionId, "Good payment details"));
    }

    @Test
    public void testPurchaseFailureBadPolicy(){

        setupSystem("Mock Config", "Mock Config","");
        int sessionId = startSession();

        login(sessionId, "chika", "12345");
        changeBuyingPolicy(sessionId, true, Database.userToStore.get("chika"),"No one is allowed");

        logout(sessionId);

        addToCart(sessionId, Database.userToStore.get("chika"),1, 5);
        addToCart(sessionId, Database.userToStore.get("chika"),2, 5);
        assertFalse(buyCart(sessionId, "Good payment details"));

    }

    @Test
    public void testPurchaseFailureNotEnoughItemsInStore(){

        setupSystem("Mock Config", "Mock Config","");
        int sessionId = startSession();

        addToCart(sessionId, Database.userToStore.get("chika"),1, 500);
        addToCart(sessionId, Database.userToStore.get("chika"),2, 500);
        assertFalse(buyCart(sessionId, "Good payment details"));

    }

    @Test
    public void testPurchaseFailureInvalidDetails(){

        setupSystem("Mock Config", "Mock Config","");
        int sessionId = startSession();

        addToCart(sessionId, Database.userToStore.get("chika"),1, 5);
        addToCart(sessionId, Database.userToStore.get("chika"),3, 5);
        assertFalse(buyCart(sessionId, "Bad payment details"));

    }

    @Test
    public void testPurchaseFailedSupplySystem(){

        setupSystem("No supplies", "Mock Config","");
        int sessionId = startSession();

        addToCart(sessionId, Database.userToStore.get("chika"),1, 5);
        addToCart(sessionId, Database.userToStore.get("chika"),2, 5);
        assertFalse(buyCart(sessionId, "Good payment details"));
    }

    @Test
    public void testPurchaseFailedPaymentSystem(){

        setupSystem("Mock Config", "No payments","");
        int sessionId = startSession();

        addToCart(sessionId, Database.userToStore.get("chika"),1, 5);
        addToCart(sessionId, Database.userToStore.get("chika"),2, 5);
        assertFalse(buyCart(sessionId, "Good payment details"));
    }


}
