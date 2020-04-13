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

        addProdcut(1, 1, 5);
        editProduct(1, 50, "Food");

        addProdcut(2, 1, 100);
        editProduct(2, 50, "Food");

        addProdcut(3, 1, 10);
        editProduct(3, 100, "Food");

        logout();

        login("ruby", "54321");
        addToCart(1, 5);
        buyCart("ruby", viewCart());
        logout();

        login("chika", "12345");

        appointOwner(1,"hanamaru");
        appointManager(1, "dia");
        logout();
        login("dia", "54321");
        appointManager(1, "yoshiko");

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
        assertTrue(removeManager(1, "dia"));
    }

    @Test
    public void testRemoveManagerFailureNonExistingUser(){
        assertFalse(removeManager(1, "danny"));
    }

    //USE CASE 4.10
    @Test
    public void testViewShopHistory(){
        assertNotNull(viewShopHistory());
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
