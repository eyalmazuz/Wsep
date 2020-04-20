package AcceptanceTest.GuestTests;

import AcceptanceTest.Data.Database;
import AcceptanceTest.ServiceTest;
import org.junit.Before;
import org.junit.Test;

public class AddToCartTests extends ServiceTest {

    /*
     * USE CASES 2.6
     *
     * */
    @Before
    public void setUp(){
        super.setUp();
        login("you", "12345");

    }


    @Test
    public void testAddToCartSuccessful(){
        assertTrue(addToCart(Database.userToStore.get("chika"),1, 5));
        assertTrue(addToCart(Database.userToStore.get("chika"),2, 5));
    }

    @Test
    public void testAddToCartFailure(){
        assertFalse(addToCart(Database.userToStore.get("chika"),1, 15));
        assertFalse(addToCart(Database.userToStore.get("chika"),2, 0));
        assertFalse(addToCart(Database.userToStore.get("chika"),2, -5));
        assertFalse(addToCart(Database.userToStore.get("chika"),3, null));
    }
}
