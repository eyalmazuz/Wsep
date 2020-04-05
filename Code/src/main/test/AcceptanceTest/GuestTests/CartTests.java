package AcceptanceTest.GuestTests;

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
    }


    //USE CASE 2.7.1
    @Test
    public void testViewCartSuccessful(){
        assertEquals(viewCart(), "Item: Iphone 15 XS SUPER MAX ULTRA DELUX, Amount: 50");
    }


    //USE CASE 2.7.2
    @Test
    public void testEditAmountInCartSuccessful(){
        assertTrue(updateAmount(3));
        assertTrue(updateAmount(3));
        assertTrue(updateAmount(200));
    }

    @Test
    public void testEditAmountInCartFailure(){
        assertFalse(updateAmount(-5));


    }

    //USE CASE 2.7.3
    @Test
    public void testDeleteItemInCartSuccessful(){
        assertTrue(deleteItemInCart("Iphone"));
    }

    //USE CASE 2.7.4
    @Test
    public void testDeleteAllCartSuccessful(){
        assertTrue(clearCart());
    }


}
