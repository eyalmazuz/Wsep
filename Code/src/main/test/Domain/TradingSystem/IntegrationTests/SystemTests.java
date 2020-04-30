package Domain.TradingSystem.IntegrationTests;

import Domain.TradingSystem.*;
import Domain.TradingSystem.System;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SystemTests extends TestCase {

    //System Unitesting
    System test;

    @Before
    public void setUp(){
       test = new System();
    }

    @After
    public void tearDown(){
        test.deleteStores();
    }



    //USE CASES 4.1 tests

    @Test
    public void testAddProductToStore() {
        int sessionId = test.startSession();
        test.register(sessionId, "eyal", "1234");
        test.login(sessionId, "eyal", "1234");
        int storeId = test.openStore(sessionId);
        test.addProductInfo(1,"bamba","hatif");
        assertTrue(test.addProductToStore(sessionId,storeId, 1, 5));
        assertFalse(test.addProductToStore(sessionId,storeId, 3, 5));//productid does not exist

    }

    @Test
    public void testEditProductInStore() {
        int sessionId = test.startSession();
        test.register(sessionId, "eyal", "1234");
        test.login(sessionId, "eyal", "1234");
        int storeId = test.openStore(sessionId);
        test.addProductInfo(1,"bamba","hatif");
        assertFalse(test.editProductInStore(sessionId, storeId, 1, "contains peanuts"));
        test.addProductToStore(sessionId,storeId,1, 5);
        assertTrue(test.editProductInStore(sessionId, storeId, 1, "contains peanuts"));
        assertFalse(test.editProductInStore(sessionId, storeId, -12, "contains peanuts"));

    }


    @Test
    public void testDeleteProductFromStore() {
        int sessionId = test.startSession();
        test.register(sessionId, "eyal", "1234");
        test.login(sessionId, "eyal", "1234");
        int storeId = test.openStore(sessionId);
        test.addProductInfo(1,"bamba","hatif");
        test.addProductToStore(sessionId, storeId, 1, 5);
        assertTrue(test.deleteProductFromStore(sessionId,storeId, 1));
        assertFalse(test.deleteProductFromStore(sessionId, storeId, 2));

    }


    // USECASE 2.8

    @Test
    public void testCheckSuppliesAndGetPrice() {
        int sessionId = test.startSession();
        Store store1 = new Store();
        store1.addProduct(new ProductInfo(4, "lambda", "snacks"), 5);
        User u = test.getUser(sessionId);
        u.addProductToCart(store1, 4, 4);
        assertEquals(test.checkSuppliesAndGetPrice(sessionId), 0.0);

        u.addProductToCart(store1, 4, 10);
        assertEquals(test.checkSuppliesAndGetPrice(sessionId), -1.0);
    }

    @Test
    public void testMakePaymentFail() {
        // keep track of the original cart, history, store supplies

        int sessionId = test.startSession();
        Store store1 = new Store();
        store1.addProduct(new ProductInfo(4, "lambda", "snacks"), 5);
        User u = test.getUser(sessionId);
        u.addProductToCart(store1, 4, 4);
        u.setState(new Subscriber());

        try {
            test.setPaymentHandler(new PaymentHandler("None"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        PaymentSystemMock.succeedPurchase = false;
        assertFalse(test.makePayment(sessionId, "details"));

        // make sure nothing was changed
        assertTrue(u.getUserPurchaseHistory().getStorePurchaseLists().isEmpty());
        assertTrue(store1.getStorePurchaseHistory().getPurchaseHistory().isEmpty());
        assertEquals(store1.getProductAmount(4), 5);
        assertNotNull(u.getShoppingCart().getStoreProductsIds().get(store1.getId()));
        assertEquals((int) u.getShoppingCart().getStoreProductsIds().get(store1.getId()).get(4), 4);
    }

    @Test
    public void testSavePurchaseHistory() {
        int sessionId = test.startSession();
        Store store1 = new Store();
        ProductInfo info = new ProductInfo(4, "lambda", "snacks");
        store1.addProduct(info, 5);
        User u = test.getUser(sessionId);
        u.addProductToCart(store1, 4, 4);

        u.setState(new Subscriber());  // so it should indeed be saved
        u.saveCurrentCartAsPurchase();
        Map<Store, List<PurchaseDetails>> map = u.getUserPurchaseHistory().getStorePurchaseLists();
        assertNotNull(map.get(store1));
        assertEquals(map.get(store1).size(), 1);
        PurchaseDetails details = map.get(store1).get(0);
        assertEquals((int)details.getProducts().get(info), 4);

        List<PurchaseDetails> storeHistory = store1.getStorePurchaseHistory().getPurchaseHistory();
        assertEquals(storeHistory.size(), 1);
        details = storeHistory.get(0);
        assertEquals((int)details.getProducts().get(info), 4);
        assertEquals(details.getUser(), u);
    }

    @Test
    public void testUpdateStoreSupplies() {
        int sessionId = test.startSession();
        Store store1 = new Store();
        ProductInfo info = new ProductInfo(4, "lambda", "snacks");
        store1.addProduct(info, 5);
        User u = test.getUser(sessionId);
        u.addProductToCart(store1, 4, 4);

        test.updateStoreSupplies(sessionId);
        // after this the store should have 4 less lambda

        assertEquals(store1.getProductAmount(4), 1);
    }

    @Test
    public void testSaveOngoingPurchaseForUser() {
        int sessionId = test.startSession();
        Store store1 = new Store();
        ProductInfo info = new ProductInfo(4, "lambda", "snacks");
        store1.addProduct(info, 5);
        User u = test.getUser(sessionId);
        u.addProductToCart(store1, 4, 4);

        test.saveOngoingPurchaseForUser(sessionId);
        Map<Integer, Map<Integer, Integer>> storeProductIds = test.getOngoingPurchases().get(sessionId);
        assertNotNull(storeProductIds);
        Map<Integer, Integer> productAmounts = storeProductIds.get(store1.getId());
        assertNotNull(productAmounts);
        assertEquals((int)productAmounts.get(4), 4);
    }

    @Test
    public void testRequestSupply() {
        int sessionId = test.startSession();
        Store store1 = new Store();
        ProductInfo info = new ProductInfo(4, "lambda", "snacks");
        store1.addProduct(info, 5);
        User u = test.getUser(sessionId);
        u.addProductToCart(store1, 4, 4);
        test.saveOngoingPurchaseForUser(sessionId);
        try {
            test.setSupplyHandler(new SupplyHandler("None"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        SupplySystemMock.succeedSupply = false;
        assertFalse(test.requestSupply(sessionId));

        SupplySystemMock.succeedSupply = true;
        assertTrue(test.requestSupply(sessionId));
    }

    @Test
    public void testRestoreSupplies() {
        int sessionId = test.startSession();
        test.addStore();
        Store store1 = test.getStores().get(0);
        ProductInfo info = new ProductInfo(4, "lambda", "snacks");
        store1.addProduct(info, 5);
        User u = test.getUser(sessionId);
        u.addProductToCart(store1, 4, 4);
        test.saveOngoingPurchaseForUser(sessionId);
        test.updateStoreSupplies(sessionId); // this REMOVES the items. now we wanna return them

        test.restoreSupplies(sessionId);
        assertEquals(store1.getProductAmount(4), 5);
    }

    @Test
    public void testRestoreHistories() {
        int sessionId = test.startSession();
        test.addStore();
        Store store1 = test.getStores().get(0);
        ProductInfo info = new ProductInfo(4, "lambda", "snacks");
        store1.addProduct(info, 5);
        User u = test.getUser(sessionId);
        u.setState(new Subscriber());
        u.addProductToCart(store1, 4, 4);
        test.saveOngoingPurchaseForUser(sessionId);

        test.savePurchaseHistory(sessionId); // this saves history, now we wanna see it gone

        test.restoreHistories(sessionId);
        List<PurchaseDetails> detailsAfterRestore = u.getUserPurchaseHistory().getStorePurchaseLists().get(store1);
        assertTrue(detailsAfterRestore.isEmpty());
    }

    @Test
    public void testRestoreCart() {
        int sessionId = test.startSession();
        test.addStore();
        Store store1 = test.getStores().get(0);
        ProductInfo info = new ProductInfo(4, "lambda", "snacks");
        store1.addProduct(info, 5);
        User u = test.getUser(sessionId);
        u.setState(new Subscriber());
        u.addProductToCart(store1, 4, 4);
        test.saveOngoingPurchaseForUser(sessionId);

        test.emptyCart(sessionId); // empty the cart, now we wanna see it back

        test.restoreCart(sessionId);
        Map<Integer, Integer> productAmounts = u.getShoppingCart().getStoreProductsIds().get(store1.getId());
        assertNotNull(productAmounts);
        assertEquals((int)productAmounts.get(4), 4);
    }

    @Test
    public void testRemoveOngoingPurchase() {
        int sessionId = test.startSession();
        test.addStore();
        Store store1 = test.getStores().get(0);
        ProductInfo info = new ProductInfo(4, "lambda", "snacks");
        store1.addProduct(info, 5);
        User u = test.getUser(sessionId);
        u.setState(new Subscriber());
        u.addProductToCart(store1, 4, 4);
        test.saveOngoingPurchaseForUser(sessionId); // now remove it

        test.removeOngoingPurchase(sessionId);
        assertNull(test.getOngoingPurchases().get(sessionId));
    }

    @Test
    public void testEntirePurchaseProcessFailure() {
        int sessionId = test.startSession();
        test.addStore();
        Store store1 = test.getStores().get(0);
        ProductInfo info = new ProductInfo(4, "lambda", "snacks");
        store1.addProduct(info, 5);
        User u = test.getUser(sessionId);
        u.setState(new Subscriber());
        u.addProductToCart(store1, 4, 4);

        try {
            test.setSupplyHandler(new SupplyHandler("none"));
            test.setPaymentHandler(new PaymentHandler("none"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        store1.setBuyingPolicy(new BuyingPolicy("No one is allowed"));

        // the process
        assertFalse(test.isCartEmpty(sessionId));
        assertFalse(test.checkBuyingPolicy(sessionId));

        assertTrue(checkPurchaseProcessNoChanges(u, store1));

        store1.setBuyingPolicy(new BuyingPolicy("None"));
        u.editCartProductAmount(store1, 4, 6);
        double price = test.checkSuppliesAndGetPrice(sessionId);
        assertEquals(price, -1.0);
        u.editCartProductAmount(store1, 4, 4);
        assertTrue(checkPurchaseProcessNoChanges(u, store1));

        price = test.checkSuppliesAndGetPrice(sessionId);
        assertEquals(price, 0.0);

        PaymentSystemMock.succeedPurchase = false;
        confirmPurchase(sessionId);
        checkPurchaseProcessNoChanges(u, store1);

        PaymentSystemMock.succeedPurchase = true;
        SupplySystemMock.succeedSupply = false;
        confirmPurchase(sessionId);
        checkPurchaseProcessNoChanges(u, store1);

    }

    private void confirmPurchase(int sessionId) {
        if (test.makePayment(sessionId, "details")) {
            test.savePurchaseHistory(sessionId);
            test.updateStoreSupplies(sessionId);
            test.saveOngoingPurchaseForUser(sessionId);
            test.emptyCart(sessionId);

            if (!test.requestSupply(sessionId)) {
                test.requestRefund(sessionId);
                test.restoreSupplies(sessionId);
                test.restoreHistories(sessionId);
                test.restoreCart(sessionId);
            }

            test.removeOngoingPurchase(sessionId);
        }
    }

    private boolean checkPurchaseProcessNoChanges(User u, Store store) {
        return u.getUserPurchaseHistory().getStorePurchaseLists().isEmpty() &&
            store.getStorePurchaseHistory().getPurchaseHistory().isEmpty() &&
            store.getProductAmount(4) == 5 &&
            u.getShoppingCart().getStoreProductsIds().get(store.getId()).get(4) == 4 &&
            (int) u.getShoppingCart().getStoreProductsIds().get(store.getId()).get(4) == 4;
    }

    @Test
    public void testAddOwnerSucess() {
        int openerSessionId = test.startSession();
        test.register(openerSessionId,"Amir","1234");
        test.login(openerSessionId,"Amir","1234");
        int storeid = test.openStore(openerSessionId);

        int newOwnerSessionId = test.startSession();
        int newOwnerSubId = test.register(newOwnerSessionId,"Bob","1234");

        assertTrue(test.addStoreOwner(openerSessionId,storeid,newOwnerSubId));

    }

    @Test
    public void testAddManager() {
        int openerSessionId = test.startSession();
        test.register(openerSessionId,"Amir","1234");
        test.login(openerSessionId,"Amir","1234");
        int storeid = test.openStore(openerSessionId);

        int newOwnerSessionId = test.startSession();
        int newOwnerSubId = test.register(newOwnerSessionId,"Bob","1234");

        assertTrue(test.addStoreManager(openerSessionId,storeid,newOwnerSubId));
        assertFalse(test.addStoreManager(openerSessionId,storeid,newOwnerSubId));//already manager
    }

    @Test
    public void testDeleteManager() {
        int openerSessionId = test.startSession();
        test.register(openerSessionId,"Amir","1234");
        test.login(openerSessionId,"Amir","1234");
        int storeid = test.openStore(openerSessionId);

        int newOwnerSessionId = test.startSession();
        int newOwnerSubId = test.register(newOwnerSessionId,"Bob","1234");

        test.addStoreManager(openerSessionId,storeid,newOwnerSubId);
        assertFalse(test.deleteManager(newOwnerSessionId,storeid,newOwnerSubId));
        assertTrue(test.deleteManager(openerSessionId,storeid,newOwnerSubId));

    }

    @Test
    public void testSetManagerDetails() {
        int openerSessionId = test.startSession();
        test.register(openerSessionId,"Amir","1234");
        test.login(openerSessionId,"Amir","1234");
        int storeid = test.openStore(openerSessionId);

        int newOwnerSessionId = test.startSession();
        int newOwnerSubId = test.register(newOwnerSessionId,"Bob","1234");

        test.addStoreManager(openerSessionId,storeid,newOwnerSubId);

        assertTrue(test.setManagerDetalis(openerSessionId,newOwnerSessionId,storeid,"any"));
        assertFalse(test.setManagerDetalis(openerSessionId,newOwnerSessionId,storeid,""));
    }


}
