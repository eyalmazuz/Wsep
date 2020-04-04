package AcceptanceTest.GuestTests;

import AcceptanceTest.ServiceTest;
import org.junit.Before;
import org.junit.Test;

public class SearchProductTests extends ServiceTest {

    @Before
    public void setUp(){
        super.setUp();
    }

    @Test
    public void testSearchProductSuccessful(){
        assertEquals(searchProducts("Iphone", null, null, null), "Name: Iphone 11, Price: 3000, Category: Phones\n Name: Iphone 10, Price: 2500, Category: Phones");
    }

    @Test
    public void testSearchProductsFilterSuccessful(){
        assertEquals(searchProducts("Iphone", null, null, "Price > 2500"), "Name: Iphone 11, Price: 3000, Category: Phones");

    }

    public void testSearchProductsFailure(){
        assertEquals(searchProducts("Samsung", null, null, null), "Error Bad Search Word");

    }


}
