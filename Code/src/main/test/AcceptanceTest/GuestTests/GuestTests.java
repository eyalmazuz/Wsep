package AcceptanceTest.GuestTests;

import AcceptanceTest.ServiceTest;
import junit.framework.Test;
import junit.framework.TestSuite;

public class GuestTests extends ServiceTest {

    public static Test suite(){
        TestSuite suite = new TestSuite("guest user tests");
        suite.addTest(new TestSuite(RegistrationTests.class));
        suite.addTest(new TestSuite(LoginTests.class));
        suite.addTest(new TestSuite(ViewInfoTests.class));
        suite.addTest(new TestSuite(SearchProductTests.class));
        suite.addTest(new TestSuite(AddToCartTests.class));
        return suite;
    }
}
