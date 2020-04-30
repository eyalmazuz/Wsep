package AcceptanceTest;

import AcceptanceTest.AdminTests.AdminTests;
import AcceptanceTest.GuestTests.GuestTests;
import AcceptanceTest.RegisteredUserTest.RegisteredUserTests;
import AcceptanceTest.StoreTests.StoreTests;
import AcceptanceTest.SystemTests.SystemTests;
import junit.framework.Test;
import junit.framework.TestSuite;

/**An Example of Testsuite**/
public class AcceptanceTests {

    public static Test suite(){
        TestSuite suite = new TestSuite("acceptance integration");
        suite.addTest(GuestTests.suite());
        suite.addTest(RegisteredUserTests.suite());
        suite.addTest(StoreTests.suite());
        suite.addTest(AdminTests.suite());
        suite.addTest(SystemTests.suite());
        return suite;
    }
}
