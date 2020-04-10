package AcceptanceTest.GuestTests;

import AcceptanceTest.Data.Database;
import AcceptanceTest.Data.ShoppingCart;
import AcceptanceTest.ServiceTest;
import org.junit.Before;
import org.junit.Test;

public class CartTests extends ServiceTest {

    /*
     * USE CASES 2.7.1-2.7.4
     *
     * */
    @Before
    public void setUp(){
        super.setUp();
        login("chika", "12345");
        openStore();
        addProdcut(1,1,  500);
        logout();
        login("hanamaru", "123456");
        addToCart(1, 5);
    }


    //USE CASE 2.7.1
    @Test
    public void testViewCartSuccessful(){
        assertEquals(viewCart(), Database.hcart);
    }


    //USE CASE 2.7.2
    @Test
    public void testEditAmountInCartSuccessful(){
        assertTrue(updateAmount(1, 3));
        assertTrue(updateAmount(1, 5));
    }

    @Test
    public void testEditAmountInCartFailure(){
        assertFalse(updateAmount(1,-5));


    }

    //USE CASE 2.7.3
    @Test
    public void testDeleteItemInCartSuccessful(){
        assertTrue(deleteItemInCart(1));
    }

    //USE CASE 2.7.4
    @Test
    public void testDeleteAllCartSuccessful(){
        assertTrue(clearCart());
    }


}
