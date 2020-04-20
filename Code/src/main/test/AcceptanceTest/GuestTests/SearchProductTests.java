package AcceptanceTest.GuestTests;

import AcceptanceTest.ServiceTest;
import org.junit.Before;
import org.junit.Test;

public class SearchProductTests extends ServiceTest {
    /*
     * USE CASES 2.5
     *
     * */
    @Before
    public void setUp(){
        super.setUp();

    }

    @Test
    public void testSearchProductSuccessful(){
        String products = searchProducts(1, "KB", null, 0, 0, 0, 0);
        System.out.println(products);
        assertNotNull(products);
    }

    @Test
    public void testSearchProductsFilterSuccessful(){
        assertNotNull(searchProducts(1, "KB", null, 0, 0, 0, 0));

    }

    public void testSearchProductsFailure(){
        assertNull(searchProducts(0, null, null, 0, 0, 2500, 0));

    }


}
