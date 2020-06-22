package DALTests;

import DTOs.ResultCode;
import DataAccess.DAOManager;
import DataAccess.DatabaseFetchException;
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
    public void testPurchaseTransaction() throws DatabaseFetchException {
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
        guestUserHandler.confirmPurchase(sessionId, "12345678", "04", "2021", "me", "777",
                "12123123", "Michael Scott", "1725 Slough Avenue", "Scranton", "PA, United States", "12345");
        DAOManager.crashTransactions = false;

        // transaction fails (sql exception thrown) right after history would be saved
        // we want to make sure that history is empty

        assertEquals(test.getStoreById(storeId).getProducts().get(0).getAmount(), 10);
        assertEquals(test.getStoreById(storeId).getStorePurchaseHistory().size(), 0);
        assertEquals(DAOManager.loadStoreById(storeId).getStorePurchaseHistory().size(), 0);

    }

    @Test
    public void testRegisterTransaction() {
        int sessionId = test.startSession().getId();

        DAOManager.crashTransactions = true;
        int subId = test.register(sessionId, "18B", "weloveshahaf").getId();
        DAOManager.crashTransactions = false;

        assertNull(test.getSubscriber(subId));

    }

    @Test
    public void testAddProductToStoreTransaction() throws DatabaseFetchException {
        int sessionId = test.startSession().getId();
        test.register(sessionId, "eyal", "1234");
        test.login(sessionId, "eyal", "1234");
        int storeId = test.openStore(sessionId).getId();

        int newManagerSessionId = test.startSession().getId();
        int newManagerId = test.register(newManagerSessionId, "user2", "passw0rd").getId();

        test.addStoreManager(sessionId, storeId, newManagerId);

        test.addProductInfo(69,"bamba","food",5);
        DAOManager.crashTransactions = true;
        test.addProductToStore(sessionId,storeId,69,5);
        DAOManager.crashTransactions = false;

        assertNull(test.getStoreById(storeId).getProductInStoreById(69));
        assertEquals(0, test.getStoreById(storeId).getAllManagers().get(0).getAllNotification().size());

    }

    @Test
    public void testEditProductInStoreTransaction() throws DatabaseFetchException {
        int sessionId = test.startSession().getId();
        test.register(sessionId, "eyal", "1234");
        test.login(sessionId, "eyal", "1234");
        int storeId = test.openStore(sessionId).getId();

        int newManagerId = test.register(sessionId, "user2", "passw0rd").getId();

        test.addStoreManager(sessionId, storeId, newManagerId);

        test.addProductInfo(69,"bamba","food",5);
        test.addProductToStore(sessionId, storeId,69,5);
        DAOManager.crashTransactions = true;
        test.editProductInStore(sessionId, storeId,69,"hello");
        DAOManager.crashTransactions = false;

        java.lang.System.out.println(test.getStoreById(storeId).getProductInStoreById(69));
        assertNull(test.getStoreById(storeId).getProductInStoreById(69).getInfo());
        assertEquals(0, test.getStoreById(storeId).getAllManagers().get(0).getAllNotification().size());

    }

    @Test
    public void testDeleteProductFromStoreTransaction() throws DatabaseFetchException {
        int sessionId = test.startSession().getId();
        test.register(sessionId, "eyal", "1234");
        test.login(sessionId, "eyal", "1234");
        int storeId = test.openStore(sessionId).getId();

        int newManagerId = test.register(sessionId, "user2", "passw0rd").getId();

        test.addStoreManager(sessionId, storeId, newManagerId);

        test.addProductInfo(69,"bamba","food",5);
        test.addProductToStore(sessionId, storeId,69,5);
        DAOManager.crashTransactions = true;
        test.deleteProductFromStore(sessionId, storeId,69);
        DAOManager.crashTransactions = false;

        assertNotNull(test.getStoreById(storeId).getProductInStoreById(69));
        assertEquals(0, test.getStoreById(storeId).getAllManagers().get(0).getAllNotification().size());

    }


}