package AcceptanceTest.GuestTests;

import AcceptanceTest.ServiceTest;
import org.junit.Before;
import org.junit.Test;

public class ViewInfoTests extends ServiceTest {

    @Before
    public void setUp(){
        super.setUp();
    }

    @Test
    public void testgetAllInfoSuccessful(){
        getAllInfo();
    }
}
