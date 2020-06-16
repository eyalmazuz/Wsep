package Domain.TradingSystem;


import junit.framework.Test;
import junit.framework.TestSuite;

public class UnitTest {

    public static Test suite(){
        TestSuite suite = new TestSuite("All UnitTesting");
        suite.addTest(new TestSuite(ShoppingCartTest.class));
        suite.addTest(new TestSuite(UserTest.class));
        suite.addTest(new TestSuite(UserHandlerTest.class));
        suite.addTest(new TestSuite(SystemTest.class));
        suite.addTest(new TestSuite(SubscriberTest.class));
        suite.addTest(new TestSuite(ExternalSystemsTest.class));
        suite.addTest(new TestSuite(SupplyHandlerTest.class));
        suite.addTest(new TestSuite(StoreTest.class));

        return suite;
    }
}
