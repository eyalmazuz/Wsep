package AcceptanceTest.StoreTests;

import AcceptanceTest.Data.Database;
import AcceptanceTest.ServiceTest;
import Domain.TradingSystem.Store;
import Domain.TradingSystem.Subscriber;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.xml.crypto.Data;

public class ShopAdministrationTests extends ServiceTest {

    /*
     * USE CASES 4.3, 4.5, 4.7, 4.10, 5.1
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

        login(sessionId,"kanan","12345");
        appointOwner(sessionId,sid_1,Database.userToId.get("iggy"));

        login(sessionId, "dia", "12345");
        appointManager(sessionId, sid_1, Database.userToId.get("ruby"));
        logout(sessionId);

        login(sessionId,"chika", "12345");


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
        int sessionId = startSession();
        login(sessionId, "chika", "12345");
        assertTrue(appointManager(sessionId, Database.userToStore.get("chika"), Database.userToId.get("you")));
    }

    @Test
    public void testAppointAnotherManagerFailureNonExistingUser(){
        int sessionId = startSession();
        login(sessionId, "chika", "12345");
        assertFalse(appointManager(sessionId, Database.userToStore.get("chika"), -5));
    }

    @Test
    public void testAppointAnotherManagerFailureAlreadyManager(){
        int sessionId = startSession();
        login(sessionId, "chika", "12345");
        assertFalse(appointManager(sessionId,1,Database.userToId.get("dia")));
    }

    // USE CASES 4.4

    @Test
    public void testRemoveOwnerSucess(){
        int sessionId = startSession();
        login(sessionId, "chika", "12345");
        assertTrue(removeOwner(sessionId,Database.userToStore.get("chika"),Database.userToId.get("kanan")));

    }

    @Test
    public void testRemoveOwnerNoOwner(){
        int sessionId = startSession();
        login(sessionId, "chika", "12345");
        assertFalse(removeOwner(sessionId,Database.userToStore.get("chika"),Database.userToId.get("dia")));

    }

    @Test
    public void testRemoveOwnerNoGrantedBy(){
        int sessionId = startSession();
        login(sessionId, "chika", "12345");
        assertFalse(removeOwner(sessionId,Database.userToStore.get("chika"),Database.userToId.get("iggy")));

    }

    @Test
    public void testRemoveOwnerInvalidOwnerID(){
        int sessionId = startSession();
        login(sessionId, "chika", "12345");
        assertFalse(removeOwner(sessionId,Database.userToStore.get("chika"),-2));

    }


    // USE CASES 4.5
    @Test
    public void testAppointAnotherOwnerSuccessful(){
        int sessionId = startSession();
        login(sessionId, "chika", "12345");
        assertTrue(appointOwner(sessionId, Database.userToStore.get("chika"),Database.userToId.get("mari")));
    }

    @Test
    public void testAppointAnotherOwnerFailureNonExistingUser(){
        int sessionId = startSession();
        assertFalse(appointOwner(sessionId, Database.userToStore.get("chika"),-7));
    }

    @Test
    public void testAppointAnotherOwnerFailureAlreadyManager(){
        int sessionId = startSession();
        assertFalse(appointOwner(sessionId, Database.userToStore.get("chika"),Database.userToId.get("kanan")));
    }

    // USE CASES 4.7
    @Test
    public void testRemoveManagerSuccessful(){
        int sessionId = startSession();
        login(sessionId, "chika", "12345");
        assertTrue(removeManager(sessionId, Database.userToStore.get("chika"), Database.userToId.get("dia")));
    }

    @Test
    public void testRemoveManagerFailureNonExistingUser(){
        int sessionId = startSession();
        login(sessionId, "chika", "12345");
        assertFalse(removeManager(sessionId, Database.userToStore.get("chika"), -7));
    }

    //USE CASE 4.10
    @Test
    public void testViewShopHistory(){
        int sessionId = startSession();
        String shopHistory = viewShopHistory(sessionId, Database.userToStore.get("chika"));
        assertNotNull(shopHistory);
    }



}
