package AcceptanceTest.StoreTests;

import AcceptanceTest.Data.Database;
import AcceptanceTest.ServiceTest;
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

        login(Database.sessionId, "chika", "12345");
        int sid_1 = openStore(Database.sessionId);
        Database.userToStore.put("chika", sid_1);
        addProdcut(Database.sessionId, 1, sid_1, 5);
        addProdcut(Database.sessionId, 2, sid_1, 5);
        appointManager(Database.sessionId, sid_1, Database.userToId.get("dia"));
        appointOwner(Database.sessionId, sid_1, Database.userToId.get("kanan"));
        logout(Database.sessionId);

        login(Database.sessionId, "dia", "12345");
        appointManager(Database.sessionId, sid_1, Database.userToId.get("ruby"));
        logout(Database.sessionId);

        login(Database.sessionId, "chika", "12345");
    }

    @After
    public void tearDown(){
        Database.userToId.clear();
        Database.userToStore.clear();
    }


    //USECASES 4.6.1
    @Test
    public void testEditManagerOptionSuccessful(){
        assertTrue(editManagerOptions(Database.sessionId, Database.userToStore.get("chika"), Database.userToId.get("dia"), "can edit prices"));
        assertTrue(editManagerOptions(Database.sessionId, Database.userToStore.get("chika"), Database.userToId.get("dia"), "can change amount"));
        assertTrue(editManagerOptions(Database.sessionId, Database.userToStore.get("chika"), Database.userToId.get("dia"),"can go fuck himself"));
    }

    // TEST HERE SUPPOSE TO FAIL CAUSE NO IMPLEMENTATION YET
    @Test
    public void testEditManagerOptionFailureInvalidOption(){
        assertFalse(editManagerOptions(Database.sessionId, Database.userToStore.get("chika"), Database.userToId.get("dia"), "delete store"));
        assertFalse(editManagerOptions(Database.sessionId, Database.userToStore.get("chika"), Database.userToId.get("dia"),"can break system"));
        assertFalse(editManagerOptions(Database.sessionId, Database.userToStore.get("chika"), Database.userToId.get("dia"), "can give free money"));
    }


}
