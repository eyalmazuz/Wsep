package AcceptanceTest.AdminTests;

import AcceptanceTest.Data.Database;
import AcceptanceTest.ServiceTest;
import Domain.TradingSystem.Store;
import Domain.TradingSystem.Subscriber;
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

        int sessionId = startSession();

        login(sessionId, "chika", "12345");

        int sid_1 = openStore(sessionId);
        Database.userToStore.put("chika", sid_1);
        addProdcut(true,sessionId, 1, sid_1, 5);
        addProdcut(true,sessionId, 2, sid_1, 5);
        logout(sessionId);

        login(sessionId, "hanamaru", "123456");
        addToCart(sessionId, Database.userToStore.get("chika"),1, 3);
        buyCart(sessionId, "Good payment details");
        logout(sessionId);
        login(sessionId, "admin", "admin");


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
        int sessionId = startSession();
        login(sessionId, "admin", "admin");
        assertNotNull(searchUserHistory(sessionId, Database.userToId.get("hanamaru")));
    }

    @Test
    public void testSearchUserHistoryFailure(){
        int sessionId = startSession();
        login(sessionId, "admin", "admin");
        assertNull(searchUserHistory(sessionId, -1));
    }


    //USE CASES 6.4.2
    @Test
    public void testSearchStoreHistorySuccessful(){
        int sessionId = startSession();
        login(sessionId, "admin", "admin");
        String history = getStoreHistory(sessionId, Database.userToStore.get("chika"));
        assertNotNull(history);
    }

    @Test
    public void testSearchStoreHistoryFailure(){
        int sessionId = startSession();
        login(sessionId, "admin", "admin");
        String storeHistory = getStoreHistory(sessionId, -2);
        assertNull(storeHistory);
    }


}
