package AcceptanceTest.SystemTests;

import AcceptanceTest.ServiceTest;
import org.junit.Before;
import org.junit.Test;

public class SystemBoostTests extends ServiceTest {
    /*
     * USE CASES 1.1
     *
     * */
    @Before
    public void setUp(){
        super.setUp();



    }

    //USE CASES 1.1
    @Test
    public void BootSystemSuccessful(){
        assertNotNull(searchUserHistory("hanamaru"));
    }

    @Test
    public void BootSystemFailure(){
        assertNull(searchUserHistory("danny"));
    }



}
