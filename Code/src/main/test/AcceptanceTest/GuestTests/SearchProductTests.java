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
        assertNotNull(searchProducts(1, "KB", null, null, null, null, null));
    }

    @Test
    public void testSearchProductsFilterSuccessful(){
        assertNotNull(searchProducts(1, "KB", null, null, null, null, null));

    }

    public void testSearchProductsFailure(){
        assertNull(searchProducts(0, null, null, null, null, 2500, null));

    }


}
