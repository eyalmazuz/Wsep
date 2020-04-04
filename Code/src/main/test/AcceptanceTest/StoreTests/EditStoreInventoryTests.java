package AcceptanceTest.StoreTests;

import AcceptanceTest.ServiceTest;
import org.junit.Before;
import org.junit.Test;

public class EditStoreInventoryTests extends ServiceTest {
    /*
     * USE CASES 4.1-4.3
     *
     * */
    @Before
    public void setUp(){
        super.setUp();
    }


    //USE CASES 4.1.1
    @Test
    public void testAddProductSuccessful(){
        assertTrue(addProdcut(123, 4));
        assertTrue(addProdcut(124, 6));

    }

    @Test
    public void testAddProductFailureNonExisting(){
        assertFalse(addProdcut(12313, 4));
        assertFalse(addProdcut(14252, 4));
    }

    @Test
    public void testAddProductFailureInvalidCount(){
        assertFalse(addProdcut(123, -4));
        assertFalse(addProdcut(123, 0));
        assertFalse(addProdcut(123, -200));
    }


    //USE CASES 4.1.2
    @Test
    public void testEditProductSuccessful(){
        assertTrue(editProduct(123, 4));
        assertTrue(editProduct(124, 6));

    }

    @Test
    public void testEditProductFailureNonExisting(){
        assertFalse(editProduct(12313, 4));
        assertFalse(editProduct(14252, 4));
    }

    @Test
    public void testEditProductFailureInvalidCount(){
        assertFalse(editProduct(123, -4));
        assertFalse(editProduct(123, 0));
        assertFalse(editProduct(123, -200));
    }


    //USE CASES 4.1.3
    @Test
    public void testDeleteProductSuccessful(){
        assertTrue(deleteProduct(123));
        assertTrue(deleteProduct(124));

    }

    @Test
    public void testDeleteProductFailureNonExisting(){
        assertFalse(deleteProduct(12313));
        assertFalse(deleteProduct(14252));
    }





}
