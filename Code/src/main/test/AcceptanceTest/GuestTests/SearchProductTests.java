package AcceptanceTest.GuestTests;

import AcceptanceTest.Data.Database;
import AcceptanceTest.Data.FilterOption;
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
        login("chika", "12345");
        openStore();
        addProdcut(1,1 , 500);
        editProduct(1, 3000, "KB");
        addProdcut(2,1 , 500);
        logout();
        login("hanamaru", "123456");

    }

    @Test
    public void testSearchProductSuccessful(){
        assertNotNull(searchProducts(1, "KB", null, null));
    }

    @Test
    public void testSearchProductsFilterSuccessful(){
        assertNotNull(searchProducts(1, "KB", null, new FilterOption("price", "2500", "")));

    }

    public void testSearchProductsFailure(){
        assertNull(searchProducts(0, null, null,  new FilterOption("price", "2500", "")));

    }


}
