package AcceptanceTest.AdminTests;

import AcceptanceTest.ServiceTest;
import junit.framework.Test;
import junit.framework.TestSuite;

public class AdminTests extends ServiceTest {

    public static Test suite(){
        TestSuite suite = new TestSuite("Admin tests tests");
        suite.addTest(new TestSuite(AdminSearchTests.class));
        return suite;
    }

}
