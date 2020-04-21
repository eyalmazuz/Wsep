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
        login("chika", "12345");
    }


    //USE CASES 4.1.1
    @Test
    public void testAddProductSuccessful(){
        assertTrue(addProdcut(3, Database.userToStore.get("chika"),4));
        assertTrue(addProdcut(4, Database.userToStore.get("chika"),6));

    }


    @Test
    public void testAddProductFailureInvalidCount(){
        assertFalse(addProdcut(1, Database.userToStore.get("chika"),-4));
        assertFalse(addProdcut(1, Database.userToStore.get("chika"), 0));
        assertFalse(addProdcut(2, Database.userToStore.get("chika"),-200));
    }


    //USE CASES 4.1.2
    @Test
    public void testEditProductSuccessful(){
        assertTrue(editProduct(Database.userToStore.get("chika"), 1, "category: food"));
        assertTrue(editProduct(Database.userToStore.get("chika"), 2, "category: goods"));

    }

    @Test
    public void testEditProductFailureNonExisting(){
        assertFalse(editProduct(Database.userToStore.get("chika"), -50, "food"));
        assertFalse(editProduct(-2, 1, null));
    }


    //USE CASES 4.1.3
    @Test
    public void testDeleteProductSuccessful(){
        assertTrue(deleteProduct(Database.userToStore.get("chika"), 1));
        assertTrue(deleteProduct(Database.userToStore.get("chika"), 2));

    }

    @Test
    public void testDeleteProductFailureNonExisting(){
        assertFalse(deleteProduct(Database.userToStore.get("chika"), 12313));
        assertFalse(deleteProduct(Database.userToStore.get("chika"), 14252));
    }





}
