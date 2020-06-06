package AcceptanceTest.AdminTests;

import AcceptanceTest.Data.Database;
import AcceptanceTest.ServiceTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AdminSearchTests extends ServiceTest {
    /*
     * USE CASES 6.4.1-6.4.2
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

        login(Database.sessionId, "hanamaru", "123456");
        addToCart(Database.sessionId, Database.userToStore.get("chika"),1, 3);
        buyCart(Database.sessionId, "Good payment details");
        logout(Database.sessionId);
        login(Database.sessionId, "admin", "admin");


    }


    @After
    public void tearDown(){
        super.tearDown();
//        Database.userToId.clear();
//        Database.userToStore.clear();
    }


    //USE CASES 6.4.1
    @Test
    public void testSearchUserHistorySuccessful(){
        assertNotNull(searchUserHistory(Database.sessionId, Database.userToId.get("hanamaru")));
    }

    @Test
    public void testSearchUserHistoryFailure(){
        assertNull(searchUserHistory(Database.sessionId, -1));
    }


    //USE CASES 6.4.2
    @Test
    public void testSearchStoreHistorySuccessful(){
        String history = getStoreHistory(Database.sessionId, Database.userToStore.get("chika"));
        assertNotNull(history);
    }

    @Test
    public void testSearchStoreHistoryFailure(){
        String storeHistory = getStoreHistory(Database.sessionId, -2);
        assertNull(storeHistory);
    }


}
