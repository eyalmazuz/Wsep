package AcceptanceTest.StoreTests;

import AcceptanceTest.Data.Database;
import AcceptanceTest.ServiceTest;
import org.junit.Before;
import org.junit.Test;

public class EditStoreInventoryTests extends ServiceTest {
    /*
     * USE CASES 4.1-4.1.3
     *
     * */
    @Before
    public void setUp(){
        super.setUp();
        login(Database.sessionId,"chika", "12345");
    }


    //USE CASES 4.1.1
    @Test
    public void testAddProductSuccessful(){
        assertTrue(addProdcut(Database.sessionId, 3, Database.userToStore.get("chika"),4));
        assertTrue(addProdcut(Database.sessionId, 4, Database.userToStore.get("chika"),6));

    }


    @Test
    public void testAddProductFailureInvalidCount(){
        assertFalse(addProdcut(Database.sessionId, 1, Database.userToStore.get("chika"),-4));
        assertFalse(addProdcut(Database.sessionId, 1, Database.userToStore.get("chika"), 0));
        assertFalse(addProdcut(Database.sessionId, 2, Database.userToStore.get("chika"),-200));
    }


    //USE CASES 4.1.2
    @Test
    public void testEditProductSuccessful(){
        assertTrue(editProduct(Database.sessionId, Database.userToStore.get("chika"), 1, "category: food"));
        assertTrue(editProduct(Database.sessionId, Database.userToStore.get("chika"), 2, "category: goods"));

    }

    @Test
    public void testEditProductFailureNonExisting(){
        assertFalse(editProduct(Database.sessionId, Database.userToStore.get("chika"), -50, "food"));
        assertFalse(editProduct(Database.sessionId, -2, 1, null));
    }


    //USE CASES 4.1.3
    @Test
    public void testDeleteProductSuccessful(){
        assertTrue(deleteProduct(Database.sessionId, Database.userToStore.get("chika"), 1));
        assertTrue(deleteProduct(Database.sessionId, Database.userToStore.get("chika"), 2));

    }

    @Test
    public void testDeleteProductFailureNonExisting(){
        assertFalse(deleteProduct(Database.sessionId, Database.userToStore.get("chika"), 12313));
        assertFalse(deleteProduct(Database.sessionId, Database.userToStore.get("chika"), 14252));
    }





}
