package Domain.TradingSystem;

import DataAccess.DAOManager;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class SupplyHandlerTest extends TestCase {
    SupplyHandler handler;

    @Before
    public void setUp() {
        System.testing = true;
    }

    @After
    public void tearDown() {
        DAOManager.clearDatabase();
    }

    @Test
    public void testRequestSupply() {
        try {
            handler = new SupplyHandler("No supplies");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<Integer, Map<Integer, Integer>> map = new HashMap<>();
        Map<Integer, Integer> productAmounts = new HashMap<>();
        productAmounts.put(4, 5);
        map.put(1, productAmounts);
        assertFalse(handler.requestSupply(1, map));

        // test for good config is a system test for updateStoreProductSupplies
    }


}
