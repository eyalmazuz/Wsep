package Domain.TradingSystem.IntegrationTests;


import Domain.TradingSystem.IntegrationTests.SystemTests;
import junit.framework.Test;
import junit.framework.TestSuite;

public class IntegrationTests {

    public static Test suite(){
        TestSuite suite = new TestSuite("All UnitTesting");
        suite.addTest(new TestSuite(SystemTests.class));

        return suite;
    }
}