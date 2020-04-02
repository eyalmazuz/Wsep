package AcceptenceTest;

import junit.framework.Test;
import junit.framework.TestSuite;

//TODO:Remove this class if find it unnecessary

/**An Example of Testsuite**/
public class AllTests {

    public static Test suite(){
        TestSuite suite = new TestSuite("purchase integration");
        suite.addTest(new TestSuite(ServiceTest.class));
        return suite;
    }
}
