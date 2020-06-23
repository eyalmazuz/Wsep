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
        addProductToStore(true,Database.sessionId, 1, sid_1, 5);
        addProductToStore(true,Database.sessionId, 2, sid_1, 5);
        appointManager(Database.sessionId, sid_1, Database.userToId.get("dia"));
        appointOwner(Database.sessionId, sid_1, Database.userToId.get("kanan"));
        logout(Database.sessionId);

        login(Database.sessionId, "dia", "12345");
        appointManager(Database.sessionId, sid_1, Database.userToId.get("ruby"));
        logout(Database.sessionId);

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

        login(Database.sessionId, "chika", "12345");
        assertTrue(editManagerOptions(Database.sessionId, Database.userToStore.get("chika"), Database.userToId.get("dia"), "add product"));
        assertTrue(editManagerOptions(Database.sessionId, Database.userToStore.get("chika"), Database.userToId.get("dia"), "any"));
        assertTrue(editManagerOptions(Database.sessionId, Database.userToStore.get("chika"), Database.userToId.get("dia"),"delete product"));
    }

    // TEST HERE SUPPOSE TO FAIL CAUSE NO IMPLEMENTATION YET
    @Test
    public void testEditManagerOptionFailureInvalidOption(){
        login(Database.sessionId, "chika", "12345");
        assertFalse(editManagerOptions(Database.sessionId, Database.userToStore.get("chika"), Database.userToId.get("dia"), "delete store"));
        assertFalse(editManagerOptions(Database.sessionId, Database.userToStore.get("chika"), Database.userToId.get("dia"),"can break system"));
        assertFalse(editManagerOptions(Database.sessionId, Database.userToStore.get("chika"), Database.userToId.get("dia"), "can give free money"));
    }

    @Test
    public void testPermissionFailCantAdd(){
        logout(Database.sessionId);
        Database.userToId.put("foo", register(Database.sessionId, "foo", "bar"));
        login(Database.sessionId, "chika", "12345");
        appointManager(Database.sessionId, Database.userToStore.get("chika"), Database.userToId.get("foo"));
        logout(Database.sessionId);
        login(Database.sessionId, "foo", "bar");
        assertFalse(addProductToStore(false, Database.sessionId, 5, Database.userToStore.get("chika"), 5));
    }


    @Test
    public void testPermissionSucessCanAdd(){
        boolean good;
        Database.userToId.put("goo", register(Database.sessionId, "goo", "bar"));
        login(Database.sessionId, "admin", "admin");
        addProductInfo(Database.sessionId, 5, "bamba", "food", 10);
        logout(Database.sessionId);
        good = login(Database.sessionId, "chika", "12345");
        good = appointManager(Database.sessionId, Database.userToStore.get("chika"), Database.userToId.get("goo"));
        good = editManagerOptions(Database.sessionId, Database.userToStore.get("chika"), Database.userToId.get("goo"), "add product");
        good = logout(Database.sessionId);
        good = login(Database.sessionId, "goo", "bar");
        assertTrue(addProductToStore(false, Database.sessionId, 5, Database.userToStore.get("chika"), 5));
    }


}
