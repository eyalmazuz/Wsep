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
        Database.userToId.clear();
        Database.userToStore.clear();
    }



    @Test
    public void testPurchaseSuccessful(){

        setupSystem("Mock Config", "Mock Config");
        Database.sessionId = startSession();

        login(Database.sessionId, "admin", "admin");
        addProductInfo(Database.sessionId, 1, "UO", "KB");
        addProductInfo(Database.sessionId, 2, "Famichiki", "Food");
        logout(Database.sessionId);

        for(String[] userData : Database.Users){
            int userId = register(Database.sessionId, userData[0], userData[1]);
            Database.userToId.put(userData[0], userId);
        }

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


        addToCart(Database.sessionId, Database.userToStore.get("chika"),1, 5);
        addToCart(Database.sessionId, Database.userToStore.get("chika"),2, 5);
        assertTrue(buyCart(Database.sessionId, "Good payment details"));
    }

    // TESTS HERE SUPPOSE TO FAIL CAUSE NO IMPLEMENTATION YET
    @Test
    public void testPurchaseFailureBadPolicy(){

        setupSystem("Mock Config", "Mock Config");
        Database.sessionId = startSession();

        login(Database.sessionId, "admin", "admin");
        addProductInfo(Database.sessionId, 1, "UO", "KB");
        addProductInfo(Database.sessionId, 2, "Famichiki", "Food");
        logout(Database.sessionId);

        for(String[] userData : Database.Users){
            int userId = register(Database.sessionId, userData[0], userData[1]);
            Database.userToId.put(userData[0], userId);
        }

        login(Database.sessionId, "chika", "12345");
        int sid_1 = openStore(Database.sessionId);
        Database.userToStore.put("chika", sid_1);
        addProdcut(true,Database.sessionId, 1, sid_1, 5);
        addProdcut(true,Database.sessionId, 2, sid_1, 5);

        //TODO when change policy is implemented, uncomment this
        changeBuyingPolicy(Database.sessionId, true, Database.userToStore.get("chika"),"No one is allowed");
        logout(Database.sessionId);

        login(Database.sessionId, "hanamaru", "12345");
        int sid_2 = openStore(Database.sessionId);
        Database.userToStore.put("hanamaru", sid_2);
        addProdcut(true,Database.sessionId, 2, sid_2, 10);
        logout(Database.sessionId);


        addToCart(Database.sessionId, Database.userToStore.get("chika"),1, 5);
        addToCart(Database.sessionId, Database.userToStore.get("chika"),2, 5);
        assertFalse(buyCart(Database.sessionId, "Good payment details"));

    }

    @Test
    public void testPurchaseFailureNotEnoughItemsInStore(){

        setupSystem("Mock Config", "Mock Config");
        Database.sessionId = startSession();

        login(Database.sessionId, "admin", "admin");
        addProductInfo(Database.sessionId, 1, "UO", "KB");
        addProductInfo(Database.sessionId, 2, "Famichiki", "Food");
        logout(Database.sessionId);

        for(String[] userData : Database.Users){
            int userId = register(Database.sessionId, userData[0], userData[1]);
            Database.userToId.put(userData[0], userId);
        }

        login(Database.sessionId, "chika", "12345");
        int sid_1 = openStore(Database.sessionId);
        Database.userToStore.put("chika", sid_1);
        addProdcut(true,Database.sessionId, 1, sid_1, 5);
        addProdcut(true,Database.sessionId, 2, sid_1, 5);

        changeBuyingPolicy(Database.sessionId, true, Database.userToStore.get("chika"),"No one is Allowed");
        logout(Database.sessionId);

        addToCart(Database.sessionId, Database.userToStore.get("chika"),1, 500);
        addToCart(Database.sessionId, Database.userToStore.get("chika"),2, 500);
        assertFalse(buyCart(Database.sessionId, "Good payment details"));

    }

    @Test
    public void testPurchaseFailureInvalidDetails(){

        setupSystem("Mock Config", "Mock Config");
        Database.sessionId = startSession();

        login(Database.sessionId, "admin", "admin");
        addProductInfo(Database.sessionId, 1, "UO", "KB");
        addProductInfo(Database.sessionId, 2, "Famichiki", "Food");
        logout(Database.sessionId);

        for(String[] userData : Database.Users){
            int userId = register(Database.sessionId, userData[0], userData[1]);
            Database.userToId.put(userData[0], userId);
        }

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


        //TODO add payment details to the buyCart method
        addToCart(Database.sessionId, Database.userToStore.get("chika"),1, 5);
        addToCart(Database.sessionId, Database.userToStore.get("chika"),3, 5);
        assertFalse(buyCart(Database.sessionId, "Bad payment details"));

    }

    @Test
    public void testPurchaseFailedSupplySystem(){

        setupSystem("No supplies", "Mock Config");
        Database.sessionId = startSession();

        login(Database.sessionId, "admin", "admin");
        addProductInfo(Database.sessionId, 1, "UO", "KB");
        addProductInfo(Database.sessionId, 2, "Famichiki", "Food");
        logout(Database.sessionId);

        for(String[] userData : Database.Users){
            int userId = register(Database.sessionId, userData[0], userData[1]);
            Database.userToId.put(userData[0], userId);
        }

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


        addToCart(Database.sessionId, Database.userToStore.get("chika"),1, 5);
        addToCart(Database.sessionId, Database.userToStore.get("chika"),2, 5);
        assertFalse(buyCart(Database.sessionId, "Good payment details"));
    }

    @Test
    public void testPurchaseFailedPaymentSystem(){

        setupSystem("Mock Config", "No payments");
        Database.sessionId = startSession();

        login(Database.sessionId, "admin", "admin");
        addProductInfo(Database.sessionId, 1, "UO", "KB");
        addProductInfo(Database.sessionId, 2, "Famichiki", "Food");
        logout(Database.sessionId);

        for(String[] userData : Database.Users){
            int userId = register(Database.sessionId, userData[0], userData[1]);
            Database.userToId.put(userData[0], userId);
        }

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


        addToCart(Database.sessionId, Database.userToStore.get("chika"),1, 5);
        addToCart(Database.sessionId, Database.userToStore.get("chika"),2, 5);
        assertFalse(buyCart(Database.sessionId, "Good payment details"));
    }


}
