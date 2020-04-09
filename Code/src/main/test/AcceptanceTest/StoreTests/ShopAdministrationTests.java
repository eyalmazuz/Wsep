package AcceptanceTest.StoreTests;

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
        login("hanamru", "123456");
        login("chika", "12345");
        openStore();
        appointOwner(1,"hanamaru");
        appointManager(1, "dia");

    }


    // USE CASES 4.3
    @Test
    public void testAppointAnotherManagerSuccessful(){
        assertTrue(appointManager(1,"ruby"));
    }

    @Test
    public void testAppointAnotherManagerFailureNonExistingUser(){
        assertFalse(appointManager(1, "danny"));
    }

    @Test
    public void testAppointAnotherManagerFailureAlreadyManager(){
        assertFalse(appointManager(1,"hanamaru"));
    }

    // USE CASES 4.5
    @Test
    public void testAppointAnotherOwnerSuccessful(){
        assertTrue(appointOwner(1,"kanan"));
    }

    @Test
    public void testAppointAnotherOwnerFailureNonExistingUser(){
        assertFalse(appointOwner(1,"danny"));
    }

    @Test
    public void testAppointAnotherOwnerFailureAlreadyManager(){
        assertFalse(appointOwner(1,"dia"));
    }

    // USE CASES 4.7
    @Test
    public void testRemoveManagerSuccessful(){
        assertTrue(removeManager(1, "hanamaru"));
    }

    @Test
    public void testRemoveManagerFailureNonExistingUser(){
        assertFalse(removeManager(1, "danny"));
    }

    //USE CASE 4.10
    //TODO FIX THIS
    @Test
    public void testViewShopHistory(){
        assertEquals(viewShopHistory(), "THIS IS A SHOP");
    }


    //USE CASE 5.1
    @Test
    public void testUpdateDiscountItemSuccessful(){
        assertTrue(updateItemDiscount(1, 5, 20));
    }

    @Test
    public void testUpdateDiscountItemFailureInvalidID(){
        assertFalse(updateItemDiscount(1,15, 20));
        assertFalse(updateItemDiscount(1,25, 20));
        assertFalse(updateItemDiscount(1,35, 20));
    }

    @Test
    public void testUpdateDiscountItemFailureInvalidDiscount(){
        assertFalse(updateItemDiscount(1,5, -20));
        assertFalse(updateItemDiscount(1,5, 500));
        assertFalse(updateItemDiscount(1,5, 0));
    }


}
