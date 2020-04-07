package Domain.TradingSystem;

import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private User guest;
    private User subscriber;

    @Before
    public void setUp(){
        guest = new User();
        subscriber = new User();
        subscriber.setState(new Subscriber());
    }

    @Test
    void checkSubscriberOnlyFunctions(){
        //Test that ensures guest will fail any subscriber functionality
        assertFalse(guest.logout());
        assertFalse(guest.addProductToStore(0,0,0));
        assertFalse(guest.editProductInStore(0,0,""));
        assertFalse(guest.deleteProductFromStore(0,0));

    }

}