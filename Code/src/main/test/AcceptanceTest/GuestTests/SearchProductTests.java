package AcceptanceTest.GuestTests;

import AcceptanceTest.Data.Database;
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
        String products = searchProducts(Database.sessionId, 1, "KB", null, -1, -1, 0, 0);
        System.out.println(products);
        assertNotNull(products);
    }

    @Test
    public void testSearchProductsFilterSuccessful(){
        assertNotNull(searchProducts(Database.sessionId, 1, "KB", null, -1, -1, 0, 0));

    }

    public void testSearchProductsFailure(){
        assertNull(searchProducts(Database.sessionId, 0, null, null, -1, -1, 2500, 0));

    }


}
