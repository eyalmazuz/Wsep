package Domain.TradingSystem;

import DataAccess.DAOManager;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserTest extends TestCase {

    private User guest;
    private User subscriber;

    @Before
    public void setUp(){
        System.testing = true;

        guest = new User();
        subscriber = new User();
        subscriber.setState(new Subscriber());
    }

    @After
    public void tearDown() {
        DAOManager.clearDatabase();
    }


    @Test
    public void testCheckSubscriberOnlyLogout(){
        //Test that ensures guest will fail any subscriber functionality
        //usecase 3.1
        assertFalse(guest.logout());
    }

    @Test
    public void testCheckSubscriberOnlyFunctionsGetHistory(){
        //Test that ensures guest will fail any subscriber functionality
        //usecase 3.1
        assertNull(guest.getHistory());
    }

    //Usecase 3.2
    @Test
    public void testOpenStoreFailure(){
        assertNull(guest.openStore());
    }

    @Test
    public void testOpenStoreSuccess(){
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