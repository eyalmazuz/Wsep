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



//    @Test
//    public void checkSubscriberOnlyFunctions(){
//        //Test that ensures guest will fail any subscriber functionality
//        //usecase 3.1
//        assertFalse(guest.logout());
//        assertFalse(guest.addProductToStore(0,0,0));
//        assertFalse(guest.editProductInStore(0,0,""));
//        assertFalse(guest.deleteProductFromStore(0,0));
//        assertEquals("No History",guest.getHistory());
//    }

    //Usecase 3.2
    @Test
    public void openStoreTest(){
        assertNull(guest.openStore());
        assertNotNull(subscriber.openStore());
    }

}