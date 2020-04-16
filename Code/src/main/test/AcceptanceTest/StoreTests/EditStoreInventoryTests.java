package AcceptanceTest.StoreTests;

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
        openStore();
        addProdcut(1, 1, 5);
        addProdcut(2, 1, 5);
    }


    //USE CASES 4.1.1
    @Test
    public void testAddProductSuccessful(){
        assertTrue(addProdcut(3, 1,4));
        assertTrue(addProdcut(4, 1,6));

    }

    @Test
    public void testAddProductFailureNonExisting(){
        assertFalse(addProdcut(12313, 1,4));
        assertFalse(addProdcut(14252, 1,4));
    }

    @Test
    public void testAddProductFailureInvalidCount(){
        assertFalse(addProdcut(1, 1,-4));
        assertFalse(addProdcut(1, 1, 0));
        assertFalse(addProdcut(2, 1,-200));
    }


    //USE CASES 4.1.2
    @Test
    public void testEditProductSuccessful(){
        assertTrue(editProduct(1, 50, "food"));
        assertTrue(editProduct(2, 10, "KB"));

    }

    @Test
    public void testEditProductFailureNonExisting(){
        assertFalse(editProduct(2, -50, "food"));
        assertFalse(editProduct(1, 100, null));
    }


    //USE CASES 4.1.3
    @Test
    public void testDeleteProductSuccessful(){
        assertTrue(deleteProduct(1, 1));
        assertTrue(deleteProduct(1, 2));

    }

    @Test
    public void testDeleteProductFailureNonExisting(){
        assertFalse(deleteProduct(1, 12313));
        assertFalse(deleteProduct(1, 14252));
    }





}
