package AcceptanceTest.SystemTests;

import AcceptanceTest.ServiceTest;
import junit.framework.Test;
import junit.framework.TestSuite;

public class SystemTests extends ServiceTest {

    public static Test suite(){
        TestSuite suite = new TestSuite("Admin tests tests");
        suite.addTest(new TestSuite(SystemBoostTests.class));
        return suite;
    }

}
