package AcceptanceTest.StoreTests;

import AcceptanceTest.ServiceTest;
import junit.framework.Test;
import junit.framework.TestSuite;

public class StoreTests extends ServiceTest{

    public static Test suite(){
        TestSuite suite = new TestSuite("Store tests tests");
        suite.addTest(new TestSuite(EditStoreInventoryTests.class));
        suite.addTest(new TestSuite(ShopAdministrationTests.class));
        suite.addTest(new TestSuite(ShopPermissionsTests.class));
        return suite;
    }
}
