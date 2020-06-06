package AcceptanceTest.StoreTests;

import AcceptanceTest.Data.Database;
import AcceptanceTest.ServiceTest;
import Domain.TradingSystem.Store;
import Domain.TradingSystem.Subscriber;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ShopPermissionsTests extends ServiceTest {
    /*
     * USE CASES 4.6.1-4.6.2
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
        appointManager(sessionId, sid_1, Database.userToId.get("dia"));
        appointOwner(sessionId, sid_1, Database.userToId.get("kanan"));
        logout(sessionId);

        login(sessionId, "dia", "12345");
        appointManager(sessionId, sid_1, Database.userToId.get("ruby"));
        logout(sessionId);

        login(sessionId, "chika", "12345");
    }

    @After
    public void tearDown(){
        super.tearDown();

//        Database.userToId.clear();
//        Database.userToStore.clear();
    }


    //USECASES 4.6.1
    @Test
    public void testEditManagerOptionSuccessful(){
        int sessionId = startSession();
        login(sessionId, "chika", "12345");
        assertTrue(editManagerOptions(sessionId, Database.userToStore.get("chika"), Database.userToId.get("dia"), "add product"));
        assertTrue(editManagerOptions(sessionId, Database.userToStore.get("chika"), Database.userToId.get("dia"), "any"));
        assertTrue(editManagerOptions(sessionId, Database.userToStore.get("chika"), Database.userToId.get("dia"),"delete product"));
    }

    // TEST HERE SUPPOSE TO FAIL CAUSE NO IMPLEMENTATION YET
    @Test
    public void testEditManagerOptionFailureInvalidOption(){
        int sessionId = startSession();
        login(sessionId, "chika", "12345");
        assertFalse(editManagerOptions(sessionId, Database.userToStore.get("chika"), Database.userToId.get("dia"), "delete store"));
        assertFalse(editManagerOptions(sessionId, Database.userToStore.get("chika"), Database.userToId.get("dia"),"can break system"));
        assertFalse(editManagerOptions(sessionId, Database.userToStore.get("chika"), Database.userToId.get("dia"), "can give free money"));
    }


}
