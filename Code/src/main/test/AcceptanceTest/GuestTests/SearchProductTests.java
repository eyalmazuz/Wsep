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
        addProdcut(1 , 500);

    }

    @Test
    public void testSearchProductSuccessful(){
        assertEquals(searchProducts("Iphone", "Phones", null, null), Database.Products);
    }

    @Test
    public void testSearchProductsFilterSuccessful(){
        assertEquals(searchProducts("Iphone", "Phone", null, new FilterOption("price", "2500", "")), Database.ProductsFiltered);

    }

    public void testSearchProductsFailure(){
        assertEquals(searchProducts(null, null, null,  new FilterOption("price", "2500", "")), null);

    }


}
