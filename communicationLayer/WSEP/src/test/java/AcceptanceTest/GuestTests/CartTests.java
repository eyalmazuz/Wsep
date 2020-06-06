package AcceptanceTest.GuestTests;

import AcceptanceTest.Data.Database;
import AcceptanceTest.ServiceTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CartTests extends ServiceTest {

    int sessionId = -1;

    /*
     * USE CASES 2.7.1-2.7.4
     *
     * */
    @Before
    public void setUp(){
        super.setUp();

        sessionId = startSession();
        login(sessionId, "chika", "12345");
        int sid_1 = openStore(sessionId);
        Database.userToStore.put("chika", sid_1);
        addProdcut(true,sessionId, 1, sid_1, 5);
        addProdcut(true,sessionId, 2, sid_1, 5);
        logout(sessionId);

        addToCart(sessionId, Database.userToStore.get("chika"),1, 5);
        addToCart(sessionId, Database.userToStore.get("chika"),2, 5);

        Database.Cart = "Basket for store ID: " + String.valueOf(Database.userToStore.get("chika")) +"\n" +
                "Product Name: Famichiki, amount: 5\n" +
                "Product Name: UO, amount: 5\n\n";

    }

    @After
    public void tearDown(){
        super.tearDown();

//        Database.userToId.clear();
//        Database.userToStore.clear();
    }


    //USE CASE 2.7.2
    @Test
    public void testEditAmountInCartSuccessful(){
        assertTrue(updateAmount(sessionId, Database.userToStore.get("chika"),1, 3));
        assertTrue(updateAmount(sessionId, Database.userToStore.get("chika"),2, 5));
    }

    @Test
    public void testEditAmountInCartFailure(){
        assertFalse(updateAmount(sessionId, Database.userToStore.get("chika"),1,-5));


    }

    //USE CASE 2.7.3
    @Test
    public void testDeleteItemInCartSuccessful(){
        login(sessionId, "chika", "12345");
        assertTrue(deleteItemInCart(sessionId, Database.userToStore.get("chika"),1));
    }

    //USE CASE 2.7.4
    @Test
    public void testDeleteAllCartSuccessful(){
        assertTrue(clearCart(sessionId));
    }


}
