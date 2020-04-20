package AcceptanceTest.GuestTests;

import AcceptanceTest.Data.Database;
import AcceptanceTest.ServiceTest;
import org.junit.Before;
import org.junit.Test;

public class PurchaseCartTests extends ServiceTest {
    /*
     * USE CASES 2.8.1-2.8.4
     *
     * */
    @Before
    public void setUp(){
        super.setUp();


    }


    @Test
    public void testPurchaseSuccessful(){
        addToCart(Database.userToStore.get("chika"),1, 5);
        addToCart(Database.userToStore.get("chika"),2, 5);
        //TODO FIX THIS
        assertTrue(buyCart());
    }

    @Test
    public void testPurchaseFailureBadPolicy(){
        addToCart(Database.userToStore.get("chika"),1, 5);
        addToCart(Database.userToStore.get("chika"),2, 5);
        //TODO FIX THIS
        assertFalse(buyCart());

    }

    @Test
    public void testPurchaseFailureNotEnoughItemsInStore(){
        addToCart(Database.userToStore.get("chika"),1, 500);
        addToCart(Database.userToStore.get("chika"),2, 500);
        //TODO FIX THIS
        assertFalse(buyCart());

    }

    @Test
    public void testPurchaseFailureInvalidDetails(){
        addToCart(Database.userToStore.get("chika"),1, 5);
        addToCart(Database.userToStore.get("chika"),3, 5);
        //TODO FIX THIS
        assertFalse(buyCart());

    }
}
