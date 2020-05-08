package Domain.TradingSystem;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest extends TestCase {

    private User guest;
    private User subscriber;

    @Before
    public void setUp(){
        guest = new User();
        subscriber = new User();
        subscriber.setState(new Subscriber());
    }



    @Test
    public void testCheckSubscriberOnlyFunctions(){
        //Test that ensures guest will fail any subscriber functionality
        //usecase 3.1
        assertFalse(guest.logout());
        assertNull(guest.getHistory());
    }

    //Usecase 3.2
    @Test
    public void testOpenStoreTest(){
        assertNull(guest.openStore());
        assertNotNull(subscriber.openStore());
    }

    @Test
    public void testSetStateNotNull(){

        assertThrows(Exception.class,()->guest.setState(null));
    }

    @Test
    public void testSetShoppingCartNotNull(){
        ShoppingCart sc = new ShoppingCart(guest);
        guest.setShoppingCart(sc);
        guest.setShoppingCart(null);
        assertEquals(sc,guest.getShoppingCart());//setNull not change the cart
    }

}