package AcceptanceTest.StoreTests;

import AcceptanceTest.Data.Database;
import AcceptanceTest.ServiceTest;
import org.junit.After;
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

        login(Database.sessionId, "chika", "12345");
        int sid_1 = openStore(Database.sessionId);
        Database.userToStore.put("chika", sid_1);
        addProdcut(true,Database.sessionId, 1, sid_1, 5);
        addProdcut(true,Database.sessionId, 2, sid_1, 5);

    }

    @After
    public void tearDown(){
        super.tearDown();

//        Database.userToId.clear();
//        Database.userToStore.clear();
    }


    //USE CASES 4.1.1
    @Test
    public void testAddProductSuccessful(){
        assertTrue(addProdcut(true,Database.sessionId, 1, Database.userToStore.get("chika"),4));
        assertTrue(addProdcut(true,Database.sessionId, 2, Database.userToStore.get("chika"),6));

    }


    @Test
    public void testAddProductFailureInvalidCount(){
        assertFalse(addProdcut(true,Database.sessionId, 1, Database.userToStore.get("chika"),-4));
        assertFalse(addProdcut(true,Database.sessionId, 1, Database.userToStore.get("chika"), 0));
        assertFalse(addProdcut(true,Database.sessionId, 2, Database.userToStore.get("chika"),-200));
    }


    //USE CASES 4.1.2
    @Test
    public void testEditProductSuccessful(){
        assertTrue(editProduct(true,Database.sessionId, Database.userToStore.get("chika"), 1, "category: food"));
        assertTrue(editProduct(true,Database.sessionId, Database.userToStore.get("chika"), 2, "category: goods"));

    }

    @Test
    public void testEditProductFailureNonExisting(){
        assertFalse(editProduct(true,Database.sessionId, Database.userToStore.get("chika"), -50, "food"));
        assertFalse(editProduct(true,Database.sessionId, -2, 1, null));
    }


    //USE CASES 4.1.3
    @Test
    public void testDeleteProductSuccessful(){
        assertTrue(deleteProduct(true,Database.sessionId, Database.userToStore.get("chika"), 1));
        assertTrue(deleteProduct(true,Database.sessionId, Database.userToStore.get("chika"), 2));

    }

    @Test
    public void testDeleteProductFailureNonExisting(){
        assertFalse(deleteProduct(true,Database.sessionId, Database.userToStore.get("chika"), 12313));
        assertFalse(deleteProduct(true,Database.sessionId, Database.userToStore.get("chika"), 14252));
    }





}
