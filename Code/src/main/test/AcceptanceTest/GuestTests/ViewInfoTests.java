package AcceptanceTest.GuestTests;

import AcceptanceTest.ServiceTest;
import org.junit.Before;
import org.junit.Test;

public class ViewInfoTests extends ServiceTest {
    /*
     * USE CASES 2.4
     *
     * */
    @Before
    public void setUp(){
        super.setUp();
    }

    @Test
    public void testgetAllInfoSuccessful(){
        assertEquals(getAllInfo(), "Store: Ebay, Products: Iphone 15 XS SUPER MAX ULTRA DELUX");
    }
}
