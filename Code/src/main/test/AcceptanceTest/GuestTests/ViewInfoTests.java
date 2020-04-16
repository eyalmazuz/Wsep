package AcceptanceTest.GuestTests;

import AcceptanceTest.Data.Database;
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
    public void testGetAllInfoSuccessful(){
        assertEquals(getAllInfo(), Database.Stores);
    }
}
