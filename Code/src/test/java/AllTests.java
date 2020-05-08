import AcceptanceTest.AcceptanceTests;
import Domain.TradingSystem.IntegrationTests.IntegrationTests;
import Domain.TradingSystem.IntegrationTests.SystemTests;
import Domain.TradingSystem.UnitTest;
import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

    public static Test suite(){
        TestSuite suite = new TestSuite("All THE FREAKING TESTS");
        suite.addTest(UnitTest.suite());
        suite.addTest(AcceptanceTests.suite());
        suite.addTest(IntegrationTests.suite());

        return suite;
    }
}