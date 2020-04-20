package AcceptanceTest.GuestTests;

import AcceptanceTest.Data.Database;
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
        addToCart(Database.userToStore.get("chika"),1, 5);
        addToCart(Database.userToStore.get("chika"),2, 5);
    }


    //USE CASE 2.7.1
    @Test
    public void testViewCartSuccessful(){
        String cart = viewCart();
        System.out.println(cart);
        assertEquals(cart, Database.Cart2);
        logout();
        login("hanamaru", "123456");
        clearCart();
        addToCart(Database.userToStore.get("chika"),1,5);
        addToCart(Database.userToStore.get("chika"),2,5);
        assertEquals(viewCart(), Database.Cart2);
    }


    //USE CASE 2.7.2
    @Test
    public void testEditAmountInCartSuccessful(){
        assertTrue(updateAmount(Database.userToStore.get("chika"),1, 3));
        assertTrue(updateAmount(Database.userToStore.get("chika"),2, 5));
    }

    @Test
    public void testEditAmountInCartFailure(){
        assertFalse(updateAmount(Database.userToStore.get("chika"),1,-5));


    }

    //USE CASE 2.7.3
    @Test
    public void testDeleteItemInCartSuccessful(){
        assertTrue(deleteItemInCart(Database.userToStore.get("chika"),1));
    }

    //USE CASE 2.7.4
    @Test
    public void testDeleteAllCartSuccessful(){
        assertTrue(clearCart());
    }


}
