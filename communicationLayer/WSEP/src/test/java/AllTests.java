import AcceptanceTest.AcceptanceTests;
import DALTests.DALTests;
import DataAccess.DAOManager;
import Domain.TradingSystem.IntegrationTests.IntegrationTests;
import Domain.TradingSystem.UnitTest;
import junit.framework.Test;
import junit.framework.TestSuite;

//test
public class AllTests {

    public static Test suite(){
        TestSuite suite = new TestSuite("All THE FREAKING TESTS");
        suite.addTest(UnitTest.suite());
        suite.addTest(AcceptanceTests.suite());
        suite.addTest(IntegrationTests.suite());
        suite.addTest(DALTests.suite());

        return suite;
    }
}