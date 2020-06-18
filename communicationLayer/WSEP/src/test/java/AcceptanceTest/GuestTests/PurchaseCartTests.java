package AcceptanceTest.GuestTests;

import AcceptanceTest.Data.Database;
import AcceptanceTest.ServiceTest;
import Domain.BGUExternalSystems.PaymentSystem;
import Domain.TradingSystem.PaymentHandler;
import Domain.TradingSystem.PaymentSystemProxy;
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
        addProductToStore(true,Database.sessionId, 1, sid_1, 5);
        addProductToStore(true,Database.sessionId, 2, sid_1, 5);
        logout(Database.sessionId);

        login(Database.sessionId, "hanamaru", "12345");
        int sid_2 = openStore(Database.sessionId);
        Database.userToStore.put("hanamaru", sid_2);
        addProductToStore(true,Database.sessionId, 2, sid_2, 10);
        logout(Database.sessionId);
    }

    @After
    public void tearDown(){
        super.tearDown();

//        Database.userToId.clear();
//        Database.userToStore.clear();
    }



    @Test
    public void testPurchaseSuccessful(){

        setupSystem("Mock Config", "Mock Config","");
        Database.sessionId = startSession();

        login(Database.sessionId, "chika", "12345");
        changeBuyingPolicy(Database.sessionId, true, Database.userToStore.get("chika"),"Any");

        logout(Database.sessionId);

        addToCart(Database.sessionId, Database.userToStore.get("chika"),1, 5);
        addToCart(Database.sessionId, Database.userToStore.get("chika"),2, 5);
        assertTrue(buyCart(Database.sessionId, "12345678", "04", "2021", "me", "777",
                "12123123", "me", "1428 Elm Street", "Springwood", "Ohio, United States", "12345"));
    }

    @Test
    public void testPurchaseFailureBadPolicy(){

        setupSystem("Mock Config", "Mock Config","");
        Database.sessionId = startSession();

        login(Database.sessionId, "chika", "12345");
        changeBuyingPolicy(Database.sessionId, true, Database.userToStore.get("chika"),"No one is allowed");

        logout(Database.sessionId);

        addToCart(Database.sessionId, Database.userToStore.get("chika"),1, 5);
        addToCart(Database.sessionId, Database.userToStore.get("chika"),2, 5);
        assertFalse(buyCart(Database.sessionId, "12345678", "04", "2021", "me", "777",
                "12123123", "me", "1428 Elm Street", "Springwood", "Ohio, United States", "12345"));

    }

    @Test
    public void testPurchaseFailureNotEnoughItemsInStore(){

        setupSystem("Mock Config", "Mock Config","");
        Database.sessionId = startSession();

        addToCart(Database.sessionId, Database.userToStore.get("chika"),1, 500);
        addToCart(Database.sessionId, Database.userToStore.get("chika"),2, 500);
        assertFalse(buyCart(Database.sessionId, "12345678", "04", "2021", "me", "777",
                "12123123", "me", "1428 Elm Street", "Springwood", "Ohio, United States", "12345"));

    }

    @Test
    public void testPurchaseFailedSupplySystem(){

        setupSystem("No supplies", "Mock Config","");
        Database.sessionId = startSession();

        addToCart(Database.sessionId, Database.userToStore.get("chika"),1, 5);
        addToCart(Database.sessionId, Database.userToStore.get("chika"),2, 5);
        assertFalse(buyCart(Database.sessionId, "12345678", "04", "2021", "me", "777",
                "12123123", "me", "1428 Elm Street", "Springwood", "Ohio, United States", "12345"));
    }

    @Test
    public void testPurchaseFailedPaymentSystem(){

        setupSystem("Mock Config", "No payments","");
        Database.sessionId = startSession();

        PaymentSystemProxy.testing = true;
        addToCart(Database.sessionId, Database.userToStore.get("chika"),1, 5);
        addToCart(Database.sessionId, Database.userToStore.get("chika"),2, 5);
        assertFalse(buyCart(Database.sessionId, "12345678", "04", "2021", "me", "777",
                "12123123", "me", "1428 Elm Street", "Springwood", "Ohio, United States", "12345"));
    }


}
