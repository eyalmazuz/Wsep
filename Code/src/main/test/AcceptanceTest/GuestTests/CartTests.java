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
        addToCart(1,1, 5);
        addToCart(1,2, 2);
    }


    //USE CASE 2.7.1
    @Test
    public void testViewCartSuccessful(){
        assertEquals(viewCart(), Database.Cart2);
    }


    //USE CASE 2.7.2
    @Test
    public void testEditAmountInCartSuccessful(){
        //TODO FIX THIS
        updateAmount(1,1, 3);
        updateAmount(1,2, 5);
    }

    @Test
    public void testEditAmountInCartFailure(){
        //TODO FIX THIS
        updateAmount(1,1,-5);


    }

    //USE CASE 2.7.3
    @Test
    public void testDeleteItemInCartSuccessful(){

        //TODO FIX THIS
        deleteItemInCart(1,1);
    }

    //USE CASE 2.7.4
    @Test
    public void testDeleteAllCartSuccessful(){
        //TODO FIX THIS
        clearCart();
    }


}
