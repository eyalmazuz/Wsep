package AcceptanceTest.RegisteredUserTest;

import AcceptanceTest.ServiceTest;
import junit.framework.Test;
import junit.framework.TestSuite;

public class RegisteredUserTests extends ServiceTest {

    public static Test suite(){
        TestSuite suite = new TestSuite("Registered user tests");
        suite.addTest(new TestSuite(UserActionsTests.class));
        return suite;
    }
}
