package AcceptanceTest;

import AcceptanceTest.AdminTests.AdminTests;
import AcceptanceTest.GuestTests.GuestTests;
import AcceptanceTest.RegisteredUserTest.RegisteredUserTests;
import AcceptanceTest.StoreTests.StoreTests;
import junit.framework.Test;
import junit.framework.TestSuite;

/**An Example of Testsuite**/
public class AllTests {

    public static Test suite(){
        TestSuite suite = new TestSuite("acceptance integration");
        suite.addTest(GuestTests.suite());
        suite.addTest(RegisteredUserTests.suite());
        suite.addTest(StoreTests.suite());
        suite.addTest(AdminTests.suite());
        return suite;
    }
}
