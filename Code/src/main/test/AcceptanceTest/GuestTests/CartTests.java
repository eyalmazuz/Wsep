package AcceptanceTest.GuestTests;

import AcceptanceTest.ServiceTest;
import org.junit.Before;
import org.junit.Test;

public class CartTests extends ServiceTest {

    @Before
    public void setUp(){
        super.setUp();
    }

    @Test
    public void testViewCartSuccessful(){
        viewCart();
    }


    @Test
    public void testEditAmountInCartSuccessful(){
        assertTrue(updateAmount( 3));
        assertTrue(updateAmount( 3));
        assertTrue(updateAmount(200));
    }

    @Test
    public void testEditAmountInCartFailure(){
        assertFalse(updateAmount(-5));


    }

    @Test
    public void testDeleteItemInCartSuccessful(){
        assertTrue(deleteItemInCart("Iphone"));
    }

    @Test
    public void testDeleteAllCartSuccessful(){
        assertTrue(clearCart());
    }


}
