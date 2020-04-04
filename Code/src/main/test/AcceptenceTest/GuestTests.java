package AcceptenceTest;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.junit.Before;

public class GuestTests extends ServiceTest {

    public static Test suite(){
        TestSuite suite = new TestSuite("guest user tests");
        suite.addTest(new TestSuite(RegistrationTests.class));
        suite.addTest(new TestSuite(LoginTests.class));
        return suite;
    }
}
