package AcceptenceTest;

import junit.framework.Test;
import junit.framework.TestSuite;

//TODO:Remove this class if find it unnecessary

/**An Example of Testsuite**/
public class AllTests {

    public static Test suite(){
        TestSuite suite = new TestSuite("acceptance integration");
        suite.addTest(GuestTests.suite());
        return suite;
    }
}
