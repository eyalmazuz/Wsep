package Domain.TradingSystem;



import Domain.TradingSystem.ShoppingCartTest;
import Domain.TradingSystem.UserTest;
import junit.framework.Test;
import junit.framework.TestSuite;

public class UnitTest {

    public static Test suite(){
        TestSuite suite = new TestSuite("All UnitTesting");
        suite.addTest(new TestSuite(ShoppingCartTest.class));
        suite.addTest(new TestSuite(UserTest.class));
        return suite;
    }
}
