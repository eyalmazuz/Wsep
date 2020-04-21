package AcceptanceTest.SystemTests;

import AcceptanceTest.ServiceTest;
import Domain.TradingSystem.System;
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
    public void testBootSystemSuccessful() {
        assertTrue(setupSystem("Mock Config", "Mock Config"));

    }


}
