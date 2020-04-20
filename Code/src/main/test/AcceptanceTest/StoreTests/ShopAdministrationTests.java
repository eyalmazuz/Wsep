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
        login("chika", "12345");
    }


    // USE CASES 4.3
    @Test
    public void testAppointAnotherManagerSuccessful(){
        assertTrue(appointManager(1, Database.userToId.get("you")));
    }

    @Test
    public void testAppointAnotherManagerFailureNonExistingUser(){
        assertFalse(appointManager(Database.userToStore.get("chika"), -5));
    }

    @Test
    public void testAppointAnotherManagerFailureAlreadyManager(){
        assertFalse(appointManager(1,Database.userToId.get("dia")));
    }

    // USE CASES 4.5
    @Test
    public void testAppointAnotherOwnerSuccessful(){
        assertTrue(appointOwner(Database.userToStore.get("chika"),Database.userToId.get("mari")));
    }

    @Test
    public void testAppointAnotherOwnerFailureNonExistingUser(){
        assertFalse(appointOwner(Database.userToStore.get("chika"),-7));
    }

    @Test
    public void testAppointAnotherOwnerFailureAlreadyManager(){
        assertFalse(appointOwner(Database.userToStore.get("chika"),Database.userToId.get("kanan")));
    }

    // USE CASES 4.7
    @Test
    public void testRemoveManagerSuccessful(){
        assertTrue(removeManager(Database.userToStore.get("chika"), Database.userToId.get("dia")));
    }

    @Test
    public void testRemoveManagerFailureNonExistingUser(){
        assertFalse(removeManager(Database.userToStore.get("chika"), -7));
    }

    //USE CASE 4.10
    @Test
    public void testViewShopHistory(){
        String shopHistory = viewShopHistory(Database.userToStore.get("chika"));
        assertNotNull(shopHistory);
    }


    //USE CASE 5.1
    @Test
    public void testUpdateDiscountItemSuccessful(){
        assertTrue(updateItemDiscount(Database.userToStore.get("chika"), 5, 20));
    }

    @Test
    public void testUpdateDiscountItemFailureInvalidID(){
        assertFalse(updateItemDiscount(Database.userToStore.get("chika"),15, 20));
        assertFalse(updateItemDiscount(Database.userToStore.get("chika"),25, 20));
        assertFalse(updateItemDiscount(Database.userToStore.get("chika"),35, 20));
    }

    @Test
    public void testUpdateDiscountItemFailureInvalidDiscount(){
        assertFalse(updateItemDiscount(Database.userToStore.get("chika"),5, -20));
        assertFalse(updateItemDiscount(Database.userToStore.get("chika"),5, 500));
        assertFalse(updateItemDiscount(Database.userToStore.get("chika"),5, 0));
    }


}
