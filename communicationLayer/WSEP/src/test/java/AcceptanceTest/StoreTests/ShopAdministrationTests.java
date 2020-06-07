package AcceptanceTest.StoreTests;

import AcceptanceTest.Data.Database;
import AcceptanceTest.ServiceTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ShopAdministrationTests extends ServiceTest {

    /*
     * USE CASES 4.3, 4.5, 4.7, 4.10, 5.1
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

        login(Database.sessionId,"kanan","12345");
        appointOwner(Database.sessionId,sid_1,Database.userToId.get("iggy"));

        login(Database.sessionId, "dia", "12345");
        appointManager(Database.sessionId, sid_1, Database.userToId.get("ruby"));
        logout(Database.sessionId);

        login(Database.sessionId,"chika", "12345");


    }

    @After
    public void tearDown(){
        super.tearDown();

//        Database.userToId.clear();
//        Database.userToStore.clear();
    }


    // USE CASES 4.3
    @Test
    public void testAppointAnotherManagerSuccessful(){
        assertTrue(appointManager(Database.sessionId, Database.userToStore.get("chika"), Database.userToId.get("you")));
    }

    @Test
    public void testAppointAnotherManagerFailureNonExistingUser(){
        assertFalse(appointManager(Database.sessionId, Database.userToStore.get("chika"), -5));
    }

    @Test
    public void testAppointAnotherManagerFailureAlreadyManager(){
        assertFalse(appointManager(Database.sessionId,1,Database.userToId.get("dia")));
    }

    // USE CASES 4.4

    @Test
    public void testRemoveOwnerSucess(){
        assertTrue(removeOwner(Database.sessionId,Database.userToStore.get("chika"),Database.userToId.get("kanan")));

    }

    @Test
    public void testRemoveOwnerNoOwner(){
        assertFalse(removeOwner(Database.sessionId,Database.userToStore.get("chika"),Database.userToId.get("dia")));

    }

    @Test
    public void testRemoveOwnerNoGrantedBy(){
        assertFalse(removeOwner(Database.sessionId,Database.userToStore.get("chika"),Database.userToId.get("iggy")));

    }

    @Test
    public void testRemoveOwnerInvalidOwnerID(){
        assertFalse(removeOwner(Database.sessionId,Database.userToStore.get("chika"),-2));

    }


    // USE CASES 4.5
    @Test
    public void testAppointAnotherOwnerSuccessful(){
        assertTrue(appointOwner(Database.sessionId, Database.userToStore.get("chika"),Database.userToId.get("mari")));
    }

    @Test
    public void testAppointAnotherOwnerFailureNonExistingUser(){
        assertFalse(appointOwner(Database.sessionId, Database.userToStore.get("chika"),-7));
    }

    @Test
    public void testAppointAnotherOwnerFailureAlreadyManager(){
        assertFalse(appointOwner(Database.sessionId, Database.userToStore.get("chika"),Database.userToId.get("kanan")));
    }

    // USE CASES 4.7
    @Test
    public void testRemoveManagerSuccessful(){
        assertTrue(removeManager(Database.sessionId, Database.userToStore.get("chika"), Database.userToId.get("dia")));
    }

    @Test
    public void testRemoveManagerFailureNonExistingUser(){
        assertFalse(removeManager(Database.sessionId, Database.userToStore.get("chika"), -7));
    }

    //USE CASE 4.10
    @Test
    public void testViewShopHistory(){
        String shopHistory = viewShopHistory(Database.sessionId, Database.userToStore.get("chika"));
        assertNotNull(shopHistory);
    }



}
