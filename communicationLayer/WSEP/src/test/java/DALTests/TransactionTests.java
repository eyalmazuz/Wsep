package DALTests;

import DataAccess.DAOManager;
import Domain.TradingSystem.PaymentHandler;
import Domain.TradingSystem.SupplyHandler;
import Domain.TradingSystem.System;
import Service.GuestUserHandler;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TransactionTests extends TestCase {

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
    public void testPurchaseTransaction() {
        int sessionId = test.startSession().getId();
        GuestUserHandler guestUserHandler = new GuestUserHandler(test);
        try {
            test.setPaymentHandler(new PaymentHandler("none"));
            test.setSupplyHandler(new SupplyHandler("none"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        test.register(sessionId, "user", "passw0rd");
        test.login(sessionId, "user", "passw0rd");
        int storeId = test.openStore(sessionId).getId();
        test.addProductInfo(1, "lambda", "snacks", 20);
        test.addProductToStore(sessionId, storeId, 1, 10);
        test.addToCart(sessionId, storeId, 1, 5);
        DAOManager.crashTransactions = true;
        guestUserHandler.confirmPurchase(sessionId, "good details");
        DAOManager.crashTransactions = false;

        // transaction fails (sql exception thrown) right after history would be saved
        // we want to make sure that history is empty

        assertEquals(test.getStoreById(storeId).getProducts().get(0).getAmount(), 10);
        assertEquals(test.getStoreById(storeId).getStorePurchaseHistory().size(), 0);
        assertEquals(DAOManager.loadStoreById(storeId).getStorePurchaseHistory().size(), 0);

    }
}