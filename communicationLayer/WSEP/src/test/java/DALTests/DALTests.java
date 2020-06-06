package DALTests;

import Domain.TradingSystem.System;
import junit.framework.TestSuite;
import junit.framework.Test;

public class DALTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("data access tests");
        suite.addTest(new TestSuite(DatabaseTests.class));
        suite.addTest(new TestSuite(StartupTests.class));
        return suite;
    }

}
