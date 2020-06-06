package DALTests;

import DataAccess.DAOManager;
import Domain.TradingSystem.*;
import Domain.TradingSystem.System;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class StartupTests extends TestCase {
    System test;

    @Before
    public void setUp() {
        System.testing = true;
        test = new System();
    }

    @After
    public void tearDown() {
        test.deleteStores();
        test.deleteUsers();
        DAOManager.clearDatabase();
    }

    @Test
    public void testProductInfoStartup() {
        test.addProductInfo(1, "lambda", "snacks", 30);
        test.addProductInfo(2, "snickers", "snacks", 40);

        test = new System();

        assertEquals(test.getProducts().size(), 2);
        ProductInfo info = test.getProductInfoById(1);
        assertNotNull(info);
        assertEquals(info.getName(), "lambda");
        assertEquals(info.getCategory(), "snacks");
        assertEquals(info.getDefaultPrice(), 30.0);

        info = test.getProductInfoById(2);
        assertNotNull(info);
        assertEquals(info.getName(), "snickers");
        assertEquals(info.getCategory(), "snacks");
        assertEquals(info.getDefaultPrice(), 40.0);
    }


    @Test
    public void testStoreStartup() {
        int sessionId = test.startSession().getId();
        test.register(sessionId, "user", "passw0rd");
        test.login(sessionId, "user", "passw0rd");
        int storeId = test.openStore(sessionId).getId();
        Store store = test.getStoreById(storeId);
        store.setName("store name");
        test.addProductInfo(1, "lambda", "snacks", 30);
        test.addProductToStore(sessionId, storeId, 1, 15);

        int sessionId2 = test.startSession().getId();
        test.register(sessionId2, "user2", "passw0rd");
        test.login(sessionId2, "user2", "passw0rd");

        test.addStoreOwner(sessionId, storeId, sessionId2).getDetails();

        test = new System();

        assertEquals(test.getStores().size(), 1);
        assertNotNull(test.getStoreById(storeId));
        Store savedStore = test.getStoreById(storeId);
        assertEquals(savedStore.getName(), "store name");
        ProductInStore pis = savedStore.getProductInStoreById(1);
        assertEquals(pis.getProductInfoId(), 1);
        assertEquals(pis.getAmount(), 15);

        assertEquals(savedStore.getOwners().size(),2);
        assertEquals(savedStore.getOwners().get(0).getId(), sessionId);
        assertEquals(savedStore.getOwners().get(1).getId(), sessionId2);
    }

    @Test
    public void testSubscriberShoppingCartStartup() {

    }
}
