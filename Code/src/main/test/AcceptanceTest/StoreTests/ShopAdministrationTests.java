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
    }


    // USE CASES 4.3
    @Test
    public void testAppointAnotherManagerSuccessful(){
        assertTrue(appointManager("bob"));
    }

    @Test
    public void testAppointAnotherManagerFailureNonExistingUser(){
        assertFalse(appointManager("danny"));
    }

    @Test
    public void testAppointAnotherManagerFailureAlreadyManager(){
        assertFalse(appointManager("chika"));
    }

    // USE CASES 4.5
    @Test
    public void testAppointAnotherOwnerSuccessful(){
        assertTrue(appointOwner("bob"));
    }

    @Test
    public void testAppointAnotherOwnerFailureNonExistingUser(){
        assertFalse(appointOwner("danny"));
    }

    @Test
    public void testAppointAnotherOwnerFailureAlreadyManager(){
        assertFalse(appointOwner("chika"));
    }

    // USE CASES 4.7
    @Test
    public void testRemoveManagerSuccessful(){
        assertTrue(removeManager(1));
    }

    @Test
    public void testRemoveManagerFailureNonExistingUser(){
        assertFalse(removeManager(2));
    }

    //USE CASE 4.10
    @Test
    public void testViewShopHistory(){
        assertEquals(viewShopHistory(), "THIS IS A SHOP");
    }


    //USE CASE 5.1
    @Test
    public void testUpdateDiscountItemSuccessful(){
        assertTrue(updateItemDiscount(5, 20));
    }

    @Test
    public void testUpdateDiscountItemFailureInvalidID(){
        assertFalse(updateItemDiscount(15, 20));
        assertFalse(updateItemDiscount(25, 20));
        assertFalse(updateItemDiscount(35, 20));
    }

    @Test
    public void testUpdateDiscountItemFailureInvalidDiscount(){
        assertFalse(updateItemDiscount(5, -20));
        assertFalse(updateItemDiscount(5, 500));
        assertFalse(updateItemDiscount(5, 0));
    }


}
