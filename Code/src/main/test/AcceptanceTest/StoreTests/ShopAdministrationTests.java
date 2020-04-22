package AcceptanceTest.StoreTests;

import AcceptanceTest.Data.Database;
import AcceptanceTest.ServiceTest;
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
        login(Database.sessionId,"chika", "12345");
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
        String shopHistory = getStoreHistory(Database.sessionId, Database.userToStore.get("chika"));
        assertNotNull(shopHistory);
    }



}
