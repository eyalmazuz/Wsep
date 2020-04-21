package Domain.TradingSystem;

import junit.framework.Test;
import junit.framework.TestSuite;

public class UnitTest {

    public static Test suite(){
        TestSuite suite = new TestSuite("All Unit Testing");
        suite.addTest(new TestSuite(ShoppingCartTest.class));
        suite.addTest(new TestSuite(UserTest.class));
        return suite;
    }
}
