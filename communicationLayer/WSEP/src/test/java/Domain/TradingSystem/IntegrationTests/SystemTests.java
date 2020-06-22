package Domain.TradingSystem.IntegrationTests;

import DTOs.IntActionResultDto;
import DTOs.Notification;
import DTOs.ResultCode;
import DTOs.StatisticsResultsDTO;
import DataAccess.DAOManager;
import Domain.BGUExternalSystems.PaymentSystem;
import DataAccess.DatabaseFetchException;
import Domain.BGUExternalSystems.SupplySystem;
import Domain.TradingSystem.System;
import Domain.TradingSystem.*;
import NotificationPublisher.MessageBroker;
import NotificationPublisher.Publisher;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class SystemTests extends TestCase {

    //System Unitesting
    System test;
    private int sessionId;
    private int store1Id;
    private ProductInfo info;
    private User u;
    private PaymentHandler paymentHandler = null;
    private SupplyHandler supplyHandler = null;

    @Before
    public void setUp() {
        System.testing = true;

        test = new System();
        Publisher publisherMock = new Publisher((path,subscribers, message) -> null);
        test.setPublisher(publisherMock);

        PaymentSystemProxy.testing = true;
        PaymentSystemProxy.succedPurchase = true;

        SupplySystemProxy.testing = true;
        SupplySystemProxy.succeedSupply = true;
    }

    @After
    public void tearDown() {
        test.deleteStores();
        test.deleteUsers();
        test.clearStats();
        DAOManager.clearDatabase();
    }



    //USE CASES 4.1 tests

    @Test
    public void testAddProductToStore() throws DatabaseFetchException {
        int sessionId = test.startSession().getId();
        test.register(sessionId, "eyal", "1234");
        test.login(sessionId, "eyal", "1234");
        int storeId = test.openStore(sessionId).getId();
        test.addProductInfo(1, "bamba", "hatif", 10);
        assertSame(test.addProductToStore(sessionId, storeId, 1, 5).getResultCode(), ResultCode.SUCCESS);
        assertNotSame(test.addProductToStore(sessionId, storeId, 3, 5).getResultCode(), ResultCode.SUCCESS); //productid does not exist

    }

    @Test
    public void testEditProductInStore() throws DatabaseFetchException {
        int sessionId = test.startSession().getId();
        test.register(sessionId, "eyal", "1234");
        test.login(sessionId, "eyal", "1234");
        int storeId = test.openStore(sessionId).getId();
        test.addProductInfo(1, "bamba", "hatif", 10);
        assertNotSame(test.editProductInStore(sessionId, storeId, 1, "contains peanuts").getResultCode(), ResultCode.SUCCESS);
        test.addProductToStore(sessionId, storeId, 1, 5);
        assertSame(test.editProductInStore(sessionId, storeId, 1, "contains peanuts").getResultCode(), ResultCode.SUCCESS);
        assertNotSame(test.editProductInStore(sessionId, storeId, -12, "contains peanuts").getResultCode(), ResultCode.SUCCESS);

    }


    @Test
    public void testDeleteProductFromStore() throws DatabaseFetchException {
        int sessionId = test.startSession().getId();
        test.register(sessionId, "eyal", "1234");
        test.login(sessionId, "eyal", "1234");
        int storeId = test.openStore(sessionId).getId();
        test.addProductInfo(1, "bamba", "hatif", 10);
        test.addProductToStore(sessionId, storeId, 1, 5);
        assertSame(test.deleteProductFromStore(sessionId, storeId, 1).getResultCode(), ResultCode.SUCCESS);
        assertNotSame(test.deleteProductFromStore(sessionId, storeId, 2).getResultCode(), ResultCode.SUCCESS);

    }


    // USECASE 2.8

    @Test
    public void testSavePurchaseHistory() throws DatabaseFetchException {
        int sessionId = test.startSession().getId();
        Store store1 = new Store(0);
        ProductInfo info = new ProductInfo(4, "lambda", "snacks", 10);
        store1.addProduct(info, 5);

        test.register(sessionId, "user", "passw0rd");
        test.login(sessionId, "user", "passw0rd");

        User u = test.getUser(sessionId);
        u.addProductToCart(store1, info, 4);

        u.saveCurrentCartAsPurchase();
        Map<Store, List<PurchaseDetails>> map = u.getStorePurchaseLists();
        assertNotNull(map.get(store1));
        assertEquals(map.get(store1).size(), 1);
        PurchaseDetails details = map.get(store1).get(0);
        assertEquals((int)details.getProducts().get(info), 4);

        List<PurchaseDetails> storeHistory = store1.getStorePurchaseHistory();
        assertEquals(storeHistory.size(), 1);
        details = storeHistory.get(0);
        assertEquals((int)details.getProducts().get(info), 4);
        assertEquals(details.getUser(), u);
    }

    @Test
    public void testUpdateStoreSupplies() throws DatabaseFetchException {
        int sessionId = test.startSession().getId();
        test.register(sessionId, "eyal", "1234");
        test.login(sessionId, "eyal", "1234");
        int store1Id = test.openStore(sessionId).getId();
        Store store1 = test.getStoreById(store1Id);

        ProductInfo info = new ProductInfo(4, "lambda", "snacks", 10);
        store1.addProduct(info, 5);
        User u = test.getUser(sessionId);
        u.addProductToCart(store1, info, 4);

        test.updateStoreSupplies(sessionId);
        // after this the store should have 4 less lambda

        assertEquals(store1.getProductAmount(4), 1);
    }

    @Test
    public void testSaveOngoingPurchaseForUser() throws DatabaseFetchException {
        int sessionId = test.startSession().getId();
        Store store1 = new Store(0);
        ProductInfo info = new ProductInfo(4, "lambda", "snacks", 10);
        store1.addProduct(info, 5);
        User u = test.getUser(sessionId);
        u.addProductToCart(store1, info, 4);

        test.saveOngoingPurchaseForUser(sessionId);
        Map<Integer, Map<Integer, Integer>> storeProductIds = test.getOngoingPurchases().get(sessionId);
        assertNotNull(storeProductIds);
        Map<Integer, Integer> productAmounts = storeProductIds.get(store1.getId());
        assertNotNull(productAmounts);
        assertEquals((int)productAmounts.get(4), 4);
    }

    @Test
    public void testRequestSupply() throws DatabaseFetchException {
        int sessionId = test.startSession().getId();
        Store store1 = new Store(0);
        ProductInfo info = new ProductInfo(4, "lambda", "snacks", 10);
        store1.addProduct(info, 5);
        User u = test.getUser(sessionId);
        u.addProductToCart(store1, info, 4);
        test.saveOngoingPurchaseForUser(sessionId);
        SupplyHandler supplyHandler = null;
        try {
            supplyHandler = new SupplyHandler("None");
            test.setSupplyHandler(supplyHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }

        SupplySystemProxy.succeedSupply = false;
        assertNotSame(test.requestSupply(sessionId, "Michael Scott", "1725 Slough Avenue", "Scranton", "PA, United States", "12345").getResultCode(), ResultCode.SUCCESS);

        SupplySystemProxy.succeedSupply = true;
        assertSame(test.requestSupply(sessionId, "Michael Scott", "1725 Slough Avenue", "Scranton", "PA, United States", "12345").getResultCode(), ResultCode.SUCCESS);
    }

    @Test
    public void testRestoreSupplies() throws DatabaseFetchException {
        int sessionId = test.startSession().getId();
        test.register(sessionId, "user", "passw0rd");
        test.login(sessionId, "user", "passw0rd");
        int id = test.openStore(sessionId).getId();
        test.addProductInfo(4, "lambda", "snacks", 10);
        test.addProductToStore(sessionId, id, 4, 5);
        test.addToCart(sessionId, id, 4, 4);
        test.saveOngoingPurchaseForUser(sessionId);
        test.updateStoreSupplies(sessionId); // this REMOVES the items. now we wanna return them

        test.restoreSupplies(sessionId);
        assertEquals(test.getStoreById(id).getProductAmount(4), 5);
    }

    @Test
    public void testRestoreHistories() throws DatabaseFetchException {
        int sessionId = test.startSession().getId();
        int id = test.addStore();
        Store store1 = test.getStores().get(id);
        ProductInfo info = new ProductInfo(4, "lambda", "snacks", 10);
        store1.addProduct(info, 5);
        User u = test.getUser(sessionId);
        u.setState(new Subscriber());
        u.addProductToCart(store1, info, 4);
        test.saveOngoingPurchaseForUser(sessionId);

        test.savePurchaseHistory(sessionId); // this saves history, now we wanna see it gone

        test.restoreHistories(sessionId);
        assertTrue(u.getStorePurchaseLists().isEmpty());
    }

    @Test
    public void testRestoreCart() throws DatabaseFetchException {
        int sessionId = test.startSession().getId();
        int id =test.addStore();
        Store store1 = test.getStores().get(id);
        test.addProductInfo(4, "lambda", "snacks", 10);
        ProductInfo info = test.getProductInfoById(4);
        store1.addProduct(info, 5);
        User u = test.getUser(sessionId);
        u.setState(new Subscriber());
        u.addProductToCart(store1, info, 4);
        test.saveOngoingPurchaseForUser(sessionId);

        test.emptyCart(sessionId); // empty the cart, now we wanna see it back

        test.restoreCart(sessionId);
        Map<Integer, Integer> productAmounts = u.getShoppingCart().getStoreProductsIds().get(store1.getId());
        assertNotNull(productAmounts);
        assertEquals((int) productAmounts.get(4), 4);
    }

    @Test
    public void testRemoveOngoingPurchase() throws DatabaseFetchException {
        int sessionId = test.startSession().getId();
        int id = test.addStore();
        Store store1 = test.getStores().get(id);
        ProductInfo info = new ProductInfo(4, "lambda", "snacks", 10);
        store1.addProduct(info, 5);
        User u = test.getUser(sessionId);
        u.setState(new Subscriber());
        u.addProductToCart(store1, info, 4);
        test.saveOngoingPurchaseForUser(sessionId); // now remove it

        test.removeOngoingPurchase(sessionId);
        assertNull(test.getOngoingPurchases().get(sessionId));
    }




    private void setUpPurchase() throws DatabaseFetchException {
        sessionId = test.startSession().getId();
        store1Id = test.addStore();
        test.addProductInfo(4, "lambda", "snacks", 10);
        info = test.getProductInfoById(4);
        test.addProductToStore(sessionId, store1Id, 4, 5);
        u = test.getUser(sessionId);
        test.register(sessionId, "user", "passw0rd");
        test.login(sessionId, "user", "passw0rd");
        test.addToCart(sessionId, store1Id, 4, 4);

        try {
            paymentHandler = new PaymentHandler("None");
            test.setPaymentHandler(paymentHandler);
            supplyHandler = new SupplyHandler("None");
            test.setSupplyHandler(supplyHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPurchaseSuccess() throws DatabaseFetchException {
        setUpPurchase();

        assertFalse(test.isCartEmpty(sessionId));
        assertSame(test.checkBuyingPolicy(sessionId).getResultCode(), ResultCode.SUCCESS);
        double price = test.checkSuppliesAndGetPrice(sessionId);
        assertFalse(price < 0);

        assertSame(test.makePayment(sessionId, "12345678", "04", "2021", "me", "777",
                "12123123").getResultCode(), ResultCode.SUCCESS);
        test.savePurchaseHistory(sessionId);
        test.saveOngoingPurchaseForUser(sessionId);

        assertTrue(test.updateStoreSupplies(sessionId));
        test.emptyCart(sessionId);
        assertSame(test.requestSupply(sessionId, "Michael Scott", "1725 Slough Avenue", "Scranton", "PA, United States", "12345").getResultCode(), ResultCode.SUCCESS);
        test.removeOngoingPurchase(sessionId);

        // check state
        assertTrue(!u.getStorePurchaseLists().isEmpty() &&
                !test.getStoreById(store1Id).getStorePurchaseHistory().isEmpty() &&
                test.getStoreById(store1Id).getProductAmount(4) == 1 &&
                 u.getShoppingCart().isEmpty() && !u.getStorePurchaseLists().isEmpty());
    }

    @Test
    public void testPurchaseFailBuyingPolicy() throws DatabaseFetchException {
        setUpPurchase();
        test.getStoreById(store1Id).setBuyingPolicy(new BuyingPolicy("No one is allowed"));
        assertNotSame(test.checkBuyingPolicy(sessionId).getResultCode(), ResultCode.SUCCESS);
        assertTrue(checkPurchaseProcessNoChanges(u, test.getStoreById(store1Id)));
    }

    @Test
    public void testPurhcaseFailMissingSupplies() throws DatabaseFetchException {
        setUpPurchase();

        test.getStoreById(store1Id).setBuyingPolicy(new BuyingPolicy("None"));
        u.editCartProductAmount(test.getStoreById(store1Id), info, 6);
        double price = test.checkSuppliesAndGetPrice(sessionId);
        assertEquals(price, -1.0);
        u.editCartProductAmount(test.getStoreById(store1Id), info, 4);
        assertTrue(checkPurchaseProcessNoChanges(u, test.getStoreById(store1Id)));
    }

    @Test
    public void testPurchaseFailPaymentSystem() throws DatabaseFetchException {
        setUpPurchase();
        PaymentSystemProxy.succedPurchase = false;
        confirmPurchase(sessionId, false);
        assertTrue(checkPurchaseProcessNoChanges(u, test.getStoreById(store1Id)));
    }

    @Test
    public void testPurchaseFailSupplySystem() throws DatabaseFetchException {
        setUpPurchase();
        SupplySystemProxy.succeedSupply = false;
        confirmPurchase(sessionId, false);
        assertTrue(checkPurchaseProcessNoChanges(u, test.getStoreById(store1Id)));
        SupplySystemProxy.succeedSupply = true;
    }

    @Test
    public void testPurchaseFailSyncProblem() throws DatabaseFetchException {
        setUpPurchase();
        confirmPurchase(sessionId, true);
        assertTrue(checkPurchaseProcessNoChanges(u, test.getStoreById(store1Id)));

    }

    private void confirmPurchase(int sessionId, boolean syncProblem) throws DatabaseFetchException {
        IntActionResultDto result = test.makePayment(sessionId, "12345678", "04", "2021", "me", "777",
                "12123123");
        if (result.getResultCode() != ResultCode.SUCCESS) return;
        int transactionId = result.getId();

        test.savePurchaseHistory(sessionId);
        test.saveOngoingPurchaseForUser(sessionId);

        // updateStoreSupplies would fail only if there is a sync problem
        if (!syncProblem) {
            test.updateStoreSupplies(sessionId);
            test.emptyCart(sessionId);
        } else {
            test.requestRefund(sessionId, transactionId);
            test.restoreHistories(sessionId);
            test.removeOngoingPurchase(sessionId);
            return;
        }
        if (test.requestSupply(sessionId, "Michael Scott", "1725 Slough Avenue", "Scranton", "PA, United States", "12345").getResultCode() != ResultCode.SUCCESS) {
            test.requestRefund(sessionId, transactionId);
            test.restoreSupplies(sessionId);
            test.restoreHistories(sessionId);
            test.restoreCart(sessionId);
        }

        test.removeOngoingPurchase(sessionId);

    }

    private boolean checkPurchaseProcessNoChanges(User u, Store store) {
        return u.getStorePurchaseLists().isEmpty() &&
            store.getStorePurchaseHistory().isEmpty() &&
            store.getProductAmount(4) == 5 &&
            u.getShoppingCart().getStoreProductsIds().get(store.getId()).get(4) == 4;
    }

    @Test
    public void testAddOwnerSucess() throws DatabaseFetchException {
        int openerSessionId = test.startSession().getId();
        test.register(openerSessionId, "Amir", "1234");
        test.login(openerSessionId, "Amir", "1234");
        int storeid = test.openStore(openerSessionId).getId();

        int newOwnerSessionId = test.startSession().getId();
        int newOwnerSubId = test.register(newOwnerSessionId, "Bob", "1234").getId();

        assertSame(test.addStoreOwner(openerSessionId, storeid, newOwnerSubId).getResultCode(), ResultCode.SUCCESS);

    }

    @Test
    public void testAddManager() throws DatabaseFetchException {
        int openerSessionId = test.startSession().getId();
        test.register(openerSessionId,"Amir","1234");
        test.login(openerSessionId,"Amir","1234");
        int storeid = test.openStore(openerSessionId).getId();

        int newOwnerSessionId = test.startSession().getId();
        int newOwnerSubId = test.register(newOwnerSessionId,"Bob","1234").getId();

        assertSame(test.addStoreManager(openerSessionId,storeid,newOwnerSubId).getResultCode(), ResultCode.SUCCESS);
        assertNotSame(test.addStoreManager(openerSessionId,storeid,newOwnerSubId).getResultCode(), ResultCode.SUCCESS);//already manager
    }

    @Test
    public void testDeleteManager() throws DatabaseFetchException {
        int openerSessionId = test.startSession().getId();
        test.register(openerSessionId,"Amir","1234");
        test.login(openerSessionId,"Amir","1234");
        int storeid = test.openStore(openerSessionId).getId();

        int newOwnerSessionId = test.startSession().getId();
        int newOwnerSubId = test.register(newOwnerSessionId,"Bob","1234").getId();

        test.addStoreManager(openerSessionId,storeid,newOwnerSubId);
        assertNotSame(test.deleteManager(newOwnerSessionId,storeid,newOwnerSubId).getResultCode(), ResultCode.SUCCESS);
        assertSame(test.deleteManager(openerSessionId,storeid,newOwnerSubId).getResultCode(), ResultCode.SUCCESS);

    }

    @Test
    public void testSetManagerDetails() throws DatabaseFetchException {
        int openerSessionId = test.startSession().getId();
        test.register(openerSessionId,"Amir","1234");
        test.login(openerSessionId,"Amir","1234");
        int storeid = test.openStore(openerSessionId).getId();

        int newOwnerSessionId = test.startSession().getId();
        int newOwnerSubId = test.register(newOwnerSessionId,"Bob","1234").getId();

        test.addStoreManager(openerSessionId,storeid,newOwnerSubId);

        assertSame(test.setManagerDetalis(openerSessionId,newOwnerSubId,storeid,"any").getResultCode(), ResultCode.SUCCESS);
        assertNotSame(test.setManagerDetalis(openerSessionId,newOwnerSubId,storeid,"").getResultCode(), ResultCode.SUCCESS);
    }


    @Test
    public void testGetHistory() throws DatabaseFetchException {
        int sessionId = test.startSession().getId();
        int id = test.addStore();
        Store store1 = test.getStores().get(id);
        ProductInfo info = new ProductInfo(4, "lambda", "snacks", 10.0);
        store1.addProduct(info, 5);
        User u = test.getUser(sessionId);
        u.setState(new Subscriber());
        u.addProductToCart(store1, info, 4);

        try {
            test.setSupplyHandler(new SupplyHandler("Mock Config"));
            test.setPaymentHandler(new PaymentHandler("Mock Config"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        String history = "Basket Purchase for store ID: "+store1.getId()+"\nlambda\nAmount: 4\nPrice: 40.0\n\n";

        store1.setBuyingPolicy(new BuyingPolicy("Any"));
        u.saveCurrentCartAsPurchase();

        assertEquals(history, u.getHistory().toString());

    }

    public HashMap<String, Object> testBuyingPoliciesSetup() throws DatabaseFetchException {
        int sessionId = test.startSession().getId();
        int storeId = test.addStore();
        Store store1 = test.getStores().get(storeId);
        ProductInfo info = new ProductInfo(4, "lambda", "snacks", 10);
        store1.addProduct(info, 10);
        User u = test.getUser(sessionId);
        u.setState(new Subscriber());
        u.addProductToCart(store1, info, 4);

        HashMap<String, Object> nec = new HashMap<>();
        nec.put("store1", store1);
        nec.put("u", u);
        nec.put("sessionId", sessionId);
        nec.put("info", info);
        return nec;
    }

    @Test
    public void testBuyingPoliciesSimpleNoOne() throws DatabaseFetchException {
        Store store1 = (Store) testBuyingPoliciesSetup().get("store1");

        // bad
        BuyingPolicy policy = new BuyingPolicy("No one is allowed");
        store1.setBuyingPolicy(policy);
        assertNotSame(test.checkBuyingPolicy(sessionId).getResultCode(), ResultCode.SUCCESS);

    }

    @Test
    public void testBuyingPoliciesSimpleDetails() throws DatabaseFetchException {
        Store store1 = (Store) testBuyingPoliciesSetup().get("store1");


        // bad
        BuyingPolicy policy = new BuyingPolicy("No one is allowed");
        store1.setBuyingPolicy(policy);
        assertNotSame(test.checkBuyingPolicy(sessionId).getResultCode(), ResultCode.SUCCESS);

    }

    @Test
    public void testBuyingPoliciesSimpleMinAmountForProductFailure() throws DatabaseFetchException {
        Store store1 = (Store) testBuyingPoliciesSetup().get("store1");


        BuyingPolicy policy = new BuyingPolicy("No one is allowed");
        // bad
        policy.addSimpleBuyingType(new BasketBuyingConstraint.MinAmountForProductConstraint(info, 5));
        store1.setBuyingPolicy(policy);
        assertNotSame(test.checkBuyingPolicy(sessionId).getResultCode(), ResultCode.SUCCESS);

    }

    @Test
    public void testBuyingPoliciesSimpleMinAmountForProductSuccess() throws DatabaseFetchException {
        testBuyingPoliciesSetup();

        BuyingPolicy policy = new BuyingPolicy("No one is allowed");
        // good
        policy.addSimpleBuyingType(new BasketBuyingConstraint.MinAmountForProductConstraint(info, 2));
        assertSame(test.checkBuyingPolicy(sessionId).getResultCode(), ResultCode.SUCCESS);

    }

    @Test
    public void testBuyingPoliciesSimpleMaxAmountForProductFailure() throws DatabaseFetchException {
        HashMap<String, Object> nec = testBuyingPoliciesSetup();
        Store store1 = (Store) nec.get("store1");
        int sessionId = (Integer) nec.get("sessionId");
        ProductInfo info = (ProductInfo) nec.get("info");

        BuyingPolicy policy = new BuyingPolicy("No one is allowed");
        policy.setDetails("blah");
        // bad
        policy.addSimpleBuyingType(new BasketBuyingConstraint.MaxAmountForProductConstraint(info, 3));
        store1.setBuyingPolicy(policy);
        assertNotSame(test.checkBuyingPolicy(sessionId).getResultCode(), ResultCode.SUCCESS);

    }

    @Test
    public void testBuyingPoliciesSimpleMaxAmountForProductSuccess() throws DatabaseFetchException {
        HashMap<String, Object> nec = testBuyingPoliciesSetup();
        Store store1 = (Store) nec.get("store1");
        int sessionId = (Integer) nec.get("sessionId");
        ProductInfo info = (ProductInfo) nec.get("info");

        BuyingPolicy policy = new BuyingPolicy("blah");
        store1.setBuyingPolicy(policy);
        policy.clearBuyingTypes();
        // good
        policy.addSimpleBuyingType(new BasketBuyingConstraint.MaxAmountForProductConstraint(info, 4));
        store1.setBuyingPolicy(policy);
        assertSame(test.checkBuyingPolicy(sessionId).getResultCode(), ResultCode.SUCCESS);

    }

    @Test
    public void testBuyingPoliciesSimpleMaxAmountForProductFailureWithAdd() throws DatabaseFetchException {
        HashMap<String, Object> nec = testBuyingPoliciesSetup();

        Store store1 = (Store) nec.get("store1");
        User u = (User) nec.get("u");
        int sessionId = (Integer) nec.get("sessionId");
        ProductInfo info = (ProductInfo) nec.get("info");

        BuyingPolicy policy = new BuyingPolicy("blah");
        store1.setBuyingPolicy(policy);
        // bad
        policy.addSimpleBuyingType(new BasketBuyingConstraint.MaxProductAmountConstraint(40));
        u.addProductToCart(store1, info, 39);
        assertNotSame(test.checkBuyingPolicy(sessionId).getResultCode(), ResultCode.SUCCESS);

    }

    @Test
    public void testBuyingPoliciesSimpleNoOneSuccessWithEdit() throws DatabaseFetchException {
        HashMap<String, Object> nec = testBuyingPoliciesSetup();

        Store store1 = (Store) nec.get("store1");
        User u = (User) nec.get("u");


        BuyingPolicy policy = new BuyingPolicy("No one is allowed");
        // good
        u.editCartProductAmount(store1, info, 39);
        assertSame(test.checkBuyingPolicy(sessionId).getResultCode(), ResultCode.SUCCESS);

    }

    @Test
    public void testBuyingPoliciesSimpleFailureNotOnDay() throws DatabaseFetchException {
        Store store1 = (Store) testBuyingPoliciesSetup().get("store1");


        BuyingPolicy policy = new BuyingPolicy("No one is allowed");
        store1.setBuyingPolicy(policy);
        policy.setDetails("blah");
        // bad
        int today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        policy.addSimpleBuyingType(new SystemBuyingConstraint.NotOnDayConstraint(today));
        assertNotSame(test.checkBuyingPolicy(sessionId).getResultCode(), ResultCode.SUCCESS);
    }

    @Test
    public void testBuyingPoliciesSimpleNotOutsideCountryFailure() throws DatabaseFetchException {
        HashMap<String, Object> nec = testBuyingPoliciesSetup();

        Store store1 = (Store) nec.get("store1");
        User u = (User) nec.get("u");


        BuyingPolicy policy = new BuyingPolicy("blah");
        store1.setBuyingPolicy(policy);
        policy.clearBuyingTypes();
        policy.addSimpleBuyingType(new UserBuyingConstraint.NotOutsideCountryConstraint("Israel"));
        u.setCountry("Brazil");
        assertNotSame(test.checkBuyingPolicy(sessionId).getResultCode(), ResultCode.SUCCESS);
    }

    @Test
    public void testBuyingPoliciesSimpleNotOutsideCountrySuccess() throws DatabaseFetchException {
        HashMap<String, Object> nec = testBuyingPoliciesSetup();

        Store store1 = (Store) nec.get("store1");
        User u = (User) nec.get("u");


        BuyingPolicy policy = new BuyingPolicy("No one is allowed");
        policy.addSimpleBuyingType(new UserBuyingConstraint.NotOutsideCountryConstraint("Israel"));
        u.setCountry("Israel");
        assertSame(test.checkBuyingPolicy(sessionId).getResultCode(), ResultCode.SUCCESS);
    }


    @Test
    public void testBuyingPoliciesComplex() throws DatabaseFetchException {
        int sessionId = test.startSession().getId();
        int storeId = test.addStore();
        Store store1 = test.getStores().get(storeId);
        ProductInfo info = new ProductInfo(4, "lambda", "snacks", 10);
        store1.addProduct(info, 10);
        User u = test.getUser(sessionId);
        u.setState(new Subscriber());
        u.addProductToCart(store1, info, 4);

        BuyingPolicy policy = new BuyingPolicy("blah");
        store1.setBuyingPolicy(policy);
        List<BuyingType> buyingConstraints = new ArrayList<>();
        buyingConstraints.add(new BasketBuyingConstraint.MinAmountForProductConstraint(info, 5));
        buyingConstraints.add(new BasketBuyingConstraint.MaxAmountForProductConstraint(info, 10));
        policy.addAdvancedBuyingType(new AdvancedBuying.LogicalBuying(buyingConstraints, AdvancedBuying.LogicalOperation.AND), false);

        // 4 is not 5 <= x <= 10
        assertNotSame(test.checkBuyingPolicy(sessionId).getResultCode(), ResultCode.SUCCESS);

        // 11 is not 5 <= x <= 10
        u.editCartProductAmount(store1, info, 11);
        assertNotSame(test.checkBuyingPolicy(sessionId).getResultCode(), ResultCode.SUCCESS);

        // 5 <= x <= 10
        u.editCartProductAmount(store1, info, 10);
        assertSame(test.checkBuyingPolicy(sessionId).getResultCode(), ResultCode.SUCCESS);

        u.editCartProductAmount(store1, info, 5);
        assertSame(test.checkBuyingPolicy(sessionId).getResultCode(), ResultCode.SUCCESS);

        u.editCartProductAmount(store1, info, 7);
        assertSame(test.checkBuyingPolicy(sessionId).getResultCode(), ResultCode.SUCCESS);

        // if its not today, can only buy 20 of product 4
        policy.clearBuyingTypes();
        buyingConstraints.clear();
        buyingConstraints.add(new SystemBuyingConstraint.NotOnDayConstraint(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)));
        buyingConstraints.add(new BasketBuyingConstraint.MaxAmountForProductConstraint(info, 20));
        //policy.addBuyingType(new AdvancedBuying.LogicalBuying(buyingConstraints, AdvancedBuying.LogicalOperation.OR));
        policy.addAdvancedBuyingType(new AdvancedBuying.LogicalBuying(buyingConstraints, AdvancedBuying.LogicalOperation.IMPLIES), false);

        // its today and user wants more than 20 - ok
        u.editCartProductAmount(store1, info, 30);
        assertSame(test.checkBuyingPolicy(sessionId).getResultCode(), ResultCode.SUCCESS);

        // its not today (huh?) and user wants more than 20 - bad!

        buyingConstraints.clear();
        buyingConstraints.add(new SystemBuyingConstraint.NotOnDayConstraint(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) + 1));
        buyingConstraints.add(new BasketBuyingConstraint.MaxAmountForProductConstraint(info, 20));
        assertNotSame(test.checkBuyingPolicy(sessionId).getResultCode(), ResultCode.SUCCESS);
    }

    public HashMap<String, Object> testDiscountPoliciesSimpleSetup() throws DatabaseFetchException {
        int sessionId = test.startSession().getId();
        int store1Id = test.addStore();
        Store store1 = test.getStores().get(store1Id);
        test.openStore(sessionId);

        test.addProductInfo(4, "bamba", "snacks", 10);
        test.addProductInfo(5, "apple", "fruits", 10);

        ProductInfo infoBamba = test.getProductInfoById(4);
        ProductInfo infoApple = test.getProductInfoById(5);

        store1.addProduct(infoBamba, 10);
        store1.setProductPrice(4, 10);
        store1.addProduct(infoApple, 5);
        store1.setProductPrice(5, 20);

        User u = test.getUser(sessionId);
        u.setState(new Subscriber());

        HashMap<String, Object> nec = new HashMap<>();

        nec.put("store1", store1);
        nec.put("infoApple", infoApple);
        nec.put("infoBamba", infoBamba);

        return nec;

    }


    @Test
    public void testDiscountPoliciesSimpleProductSaleDiscount() throws DatabaseFetchException {
        HashMap<String, Object> nec = testDiscountPoliciesSimpleSetup();
        Store store1 = (Store) nec.get("store1");
        ProductInfo infoBamba = (ProductInfo) nec.get("infoBamba");

        DiscountPolicy policy = new DiscountPolicy("test");
        policy.addSimpleDiscountType(new ProductDiscount.ProductSaleDiscount(4, 0.5));
        store1.setDiscountPolicy(policy);
        HashMap<ProductInfo, Integer> productsAmount = new HashMap<>();
        productsAmount.put(infoBamba, 1);
        assertEquals(5.0, store1.getPrice(u, productsAmount).getPrice());
        policy.clearDiscountTypes();

        productsAmount = new HashMap<>();
        productsAmount.put(infoBamba, 5);
        assertEquals(5 * 10.0, store1.getPrice(u, productsAmount).getPrice());

        policy = new DiscountPolicy("test");
        policy.addSimpleDiscountType(new ProductDiscount.ProductSaleDiscount(4, 0.5));
        store1.setDiscountPolicy(policy);
        productsAmount = new HashMap<>();
        productsAmount.put(infoBamba, 5);
        assertEquals(5 * 5.0, store1.getPrice(u, productsAmount).getPrice());
        policy.clearDiscountTypes();
    }

    @Test
    public void testDiscountPoliciesSimpleMultipleProductSaleDiscounts() throws DatabaseFetchException {
        HashMap<String, Object> nec = testDiscountPoliciesSimpleSetup();
        Store store1 = (Store) nec.get("store1");
        ProductInfo infoBamba = (ProductInfo) nec.get("infoBamba");
        ProductInfo infoApple = (ProductInfo) nec.get("infoApple");

        DiscountPolicy policy;
        HashMap<ProductInfo, Integer> productsAmount;

        policy = new DiscountPolicy("test");
        policy.addSimpleDiscountType(new ProductDiscount.ProductSaleDiscount(4, 0.5));
        policy.addSimpleDiscountType(new ProductDiscount.ProductSaleDiscount(5, 0.75));
        store1.setDiscountPolicy(policy);
        productsAmount = new HashMap<>();
        productsAmount.put(infoBamba, 1);
        assertEquals(5.0, store1.getPrice(u, productsAmount).getPrice());
        policy.clearDiscountTypes();


        policy = new DiscountPolicy("test");
        policy.addSimpleDiscountType(new ProductDiscount.ProductSaleDiscount(4, 0.5));
        policy.addSimpleDiscountType(new ProductDiscount.ProductSaleDiscount(5, 0.75));
        store1.setDiscountPolicy(policy);

        productsAmount = new HashMap<>();
        productsAmount.put(infoBamba, 5);
        productsAmount.put(infoApple, 10);

        assertEquals((1-0.5) * 5 * 10 + (1-0.75) * 10 * 20, store1.getPrice(u, productsAmount).getPrice());
    }

    @Test
    public void testDiscountPoliciesSimpleCategorySaleDiscount() throws DatabaseFetchException {
        HashMap<String, Object> nec = testDiscountPoliciesSimpleSetup();
        Store store1 = (Store) nec.get("store1");
        ProductInfo infoBamba = (ProductInfo) nec.get("infoBamba");
        ProductInfo infoApple = (ProductInfo) nec.get("infoApple");

        DiscountPolicy policy;
        HashMap<ProductInfo, Integer> productsAmount;

        policy = new DiscountPolicy("test");
        policy.addSimpleDiscountType(new ProductDiscount.CategorySaleDiscount("fruits", 0.75));

        store1.setDiscountPolicy(policy);

        productsAmount = new HashMap<>();
        productsAmount.put(infoApple, 10);

        assertEquals((1-0.75)*10*20, store1.getPrice(u, productsAmount).getPrice());
        policy.clearDiscountTypes();


        policy = new DiscountPolicy("test");
        policy.addSimpleDiscountType(new ProductDiscount.CategorySaleDiscount("fruits", 0.75));

        store1.setDiscountPolicy(policy);

        productsAmount = new HashMap<>();
        productsAmount.put(infoBamba, 5);
        productsAmount.put(infoApple, 10);
        assertEquals(5*10 + (1-0.75)*10*20, store1.getPrice(u, productsAmount).getPrice());
        policy.clearDiscountTypes();
    }

    @Test
    public void testDiscountPoliciesSimpleProductAndCategorySales() throws DatabaseFetchException {
        HashMap<String, Object> nec = testDiscountPoliciesSimpleSetup();
        Store store1 = (Store) nec.get("store1");
        ProductInfo infoBamba = (ProductInfo) nec.get("infoBamba");
        ProductInfo infoApple = (ProductInfo) nec.get("infoApple");

        DiscountPolicy policy;
        HashMap<ProductInfo, Integer> productsAmount;

        policy = new DiscountPolicy("test");
        policy.addSimpleDiscountType(new ProductDiscount.ProductSaleDiscount(4, 0.5));
        policy.addSimpleDiscountType(new ProductDiscount.ProductSaleDiscount(5, 0.75));
        policy.addSimpleDiscountType(new ProductDiscount.CategorySaleDiscount("fruits", 0.5));

        //java.lang.System.out.println(policy);

        store1.setDiscountPolicy(policy);

        productsAmount = new HashMap<>();
        productsAmount.put(infoBamba, 5);
        productsAmount.put(infoApple, 10);

        assertEquals((1-0.5) * 5 * 10 + (1-0.75) * 10 * 20 * (1-0.5), store1.getPrice(u, productsAmount).getPrice());
        policy.clearDiscountTypes();
        test.removeProduct(4);
        test.removeProduct(5);
    }


    @Test
    public void testDiscountPoliciesComplex() throws DatabaseFetchException {
        int sessionId = test.startSession().getId();
        int store1Id = test.addStore();
        Store store1 = test.getStores().get(store1Id);
        test.openStore(sessionId);

        test.addProductInfo(4, "bamba", "snacks", 10);
        test.addProductInfo(5, "apple", "fruits", 10);
        ProductInfo infoBamba = test.getProductInfoById(4);
        ProductInfo infoApple = test.getProductInfoById(5);

        store1.addProduct(infoBamba, 10);
        store1.setProductPrice(4, 10);
        store1.addProduct(infoApple, 30);
        store1.setProductPrice(5, 20);

        User u = test.getUser(sessionId);
        u.setState(new Subscriber());


        DiscountPolicy policy = new DiscountPolicy("test");

        List<DiscountType> discounts = new ArrayList<>();
        discounts.add(new ProductDiscount.ProductSaleDiscount(4, 0.5));
        discounts.add(new ProductDiscount.CategorySaleDiscount("fruits", 0.75));


        policy.addAdvancedDiscountType(new AdvancedDiscount.LogicalDiscount(discounts, AdvancedDiscount.LogicalOperation.AND), false);
        store1.setDiscountPolicy(policy);
        HashMap<ProductInfo, Integer> productsAmount = new HashMap<>();
        productsAmount.put(infoBamba, 5);
        productsAmount.put(infoApple, 10);


        assertEquals((1-0.5) * 5 * 10 + (1-0.75) * 10 * 20, store1.getPrice(u, productsAmount).getPrice());
        policy.clearDiscountTypes();


        discounts = new ArrayList<>();
        discounts.add(new ProductDiscount.CategorySaleDiscount("fruits", 0.75));
        discounts.add(new ProductDiscount.ProductSaleDiscount(4, 0.5));

        policy = new DiscountPolicy("test");

        AdvancedDiscount.LogicalDiscount hi = new AdvancedDiscount.LogicalDiscount(discounts, AdvancedDiscount.LogicalOperation.OR);

        policy.addAdvancedDiscountType(new AdvancedDiscount.LogicalDiscount(discounts, AdvancedDiscount.LogicalOperation.OR), false);

        //java.lang.System.out.println(policy);
        store1.setDiscountPolicy(policy);
        productsAmount = new HashMap<>();
        productsAmount.put(infoBamba, 5);
        productsAmount.put(infoApple, 10);


        assertEquals(5*10 + (1-0.75)*20*10, store1.getPrice(u, productsAmount).getPrice());
        policy.clearDiscountTypes();


        discounts = new ArrayList<>();
        discounts.add(new ProductDiscount.CategorySaleDiscount("fruits", 0.75));
        discounts.add(new ProductDiscount.ProductSaleDiscount(4, 0.5));
        policy = new DiscountPolicy("test");
        policy.addAdvancedDiscountType(new AdvancedDiscount.LogicalDiscount(discounts, AdvancedDiscount.LogicalOperation.XOR), false);
        store1.setDiscountPolicy(policy);
        productsAmount = new HashMap<>();
        productsAmount.put(infoBamba, 5);
        productsAmount.put(infoApple, 10);


        assertEquals((1-0.5)*5*10 + 10*20, store1.getPrice(u, productsAmount).getPrice());
        policy.clearDiscountTypes();
        test.removeProduct(4);
        test.removeProduct(5);

    }

    @Test
    public void testAddProductUpdateNotification() throws DatabaseFetchException {

        int openerSessionId = test.startSession().getId();
        int subId = test.register(openerSessionId,"Amir","1234").getId();
        test.login(openerSessionId,"Amir","1234");
        int storeid = test.openStore(openerSessionId).getId();
        test.addProductInfo(4, "bamba", "snacks", 10);
        test.addProductInfo(5, "apple", "fruits", 10);
        ProductInfo infoBamba = test.getProductInfoById(4);
        ProductInfo infoApple = test.getProductInfoById(5);

        test.addProductToStore(openerSessionId,storeid,4,5);
        ArrayList<Notification> noties = test.getUserHandler().getSubscriber(subId).getAllNotification();
        assertEquals(1,noties.size());

    }

    //tests for usecase 4.5

    /**
     * Simple owner deletion.
     */
    @Test
    public void testDeleteOwnerSingleManager() throws DatabaseFetchException {
        //Set up data
        int firstId = test.startSession().getId();
        test.register(firstId,"amir","1234");
        int ownerId = test.register(firstId,"bob","1234").getId();
        int managerId = test.register(firstId,"mo","1234").getId();
        test.login(firstId,"amir","1234");
        int storeId = test.openStore(firstId).getId();
        Store store = test.getStoreById(storeId);
        test.addStoreOwner(firstId,storeId,ownerId);
        test.logout(firstId);
        test.login(firstId,"bob","1234");
        test.addStoreManager(firstId,storeId,managerId);
        test.logout(firstId);

        //do action
        test.login(firstId,"amir","1234");
        test.deleteOwner(firstId,storeId,ownerId);

        //Test
        assertEquals(1,store.getAllManagers().size());


    }

    /**
     * Complex Owner deletion (Owner inside Owner)
     */
    public void testDeleteOwnerMultyManager() throws DatabaseFetchException {
        //Set up data
        int firstId = test.startSession().getId();

        test.register(firstId,"amir","1234");
        int bobId = test.register(firstId,"bob","1234").getId();
        int moId = test.register(firstId,"mo","1234").getId();
        int larryId = test.register(firstId,"larry","1234").getId();

        test.login(firstId,"amir","1234");
        int storeId = test.openStore(firstId).getId();
        Store store = test.getStoreById(storeId);
        test.addStoreOwner(firstId,storeId,bobId);
        test.logout(firstId);

        test.login(firstId,"bob","1234");
        test.addStoreOwner(firstId,storeId,moId);
        test.logout(firstId);

        test.login(firstId,"mo","1234");
        test.addStoreManager(firstId,storeId,larryId);
        test.logout(firstId);

        //do action
        test.login(firstId,"amir","1234");
        test.deleteOwner(firstId,storeId,bobId);

        //Test
        assertEquals(1,store.getAllManagers().size());

        Subscriber larry = test.getUserHandler().getSubscriber(larryId);
        assertNull(larry.getPermission(storeId));


    }

    /**
     * Checks that remove not exising manager results in failure
     */
    @Test
    public void testDeleteOwnerNotExisting() throws DatabaseFetchException {
        int firstId = test.startSession().getId();
        test.register(firstId,"amir","1234");
        test.login(firstId,"amir","1234");
        int storeId = test.openStore(firstId).getId();
        Store store = test.getStoreById(storeId);
        assertEquals(ResultCode.ERROR_STORE_MANAGER_MODIFICATION,test.deleteOwner(firstId,storeId,99).getResultCode());


    }

    /**
     * Checks if notificationQueue of removed manager has been increased
     */
    @Test
    public void testDeleteOwnerNotification() throws DatabaseFetchException {
        //Set up data
        int firstId = test.startSession().getId();
        test.register(firstId,"amir","1234");
        int ownerId = test.register(firstId,"bob","1234").getId();
        Subscriber bob = test.getUserHandler().getSubscriber(ownerId);
        test.login(firstId,"amir","1234");
        int storeId = test.openStore(firstId).getId();
        test.addStoreOwner(firstId,storeId,ownerId);

        //Do action
        int preNotifications = bob.getAllNotification().size();
        test.deleteOwner(firstId,storeId,ownerId);
        int postNotifications = bob.getAllNotification().size();

        //Test
        assertEquals(1,postNotifications-preNotifications);


    }

    /**
     * Checks if removing owner not granted by yourself cause error
     */
    @Test
    public void testDeleteOwnerNotGrantedBy() throws DatabaseFetchException {
        int firstId = test.startSession().getId();
        int masterId = test.register(firstId,"amir","1234").getId();
        int ownerId = test.register(firstId,"bob","1234").getId();
        test.login(firstId,"amir","1234");
        int storeId = test.openStore(firstId).getId();
        test.addStoreOwner(firstId,storeId,ownerId);
        test.logout(firstId);

        test.login(firstId,"bob","1234");
        assertEquals(ResultCode.ERROR_DELETE,test.deleteOwner(firstId,storeId,masterId).getResultCode());

    }


    //Test for usecase 1.1
    @Test
    public void testSetupConfigFileOpenStore() throws DatabaseFetchException {
        try {
            FileWriter file = new FileWriter("testFile.txt",false);
            file.write("register(bob);\nopen-store(bob,bob-store);\n");
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        test.setup("123","123","testFile.txt");
        assertTrue(test.getStoreByName("bob-store") >= 0);


    }

    @Test
    public void testSetupConfigFileSyntaxError(){

        try {
            FileWriter file = new FileWriter("testFile.txt",false);
            file.write("registerfsdfsfsd(bob);\nopen-store(bob,bob-store);\n");
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals(ResultCode.ERROR_SETUP,test.setup("123","123","testFile.txt").getResultCode());

    }

    @Test
    public void testSetupConfigFileLogicError(){
        //Test that commands who fail on the system will fail the setup,
        // open store with none registered user:
        try {
            FileWriter file = new FileWriter("testFile.txt",false);
            file.write("open-store(bob,bob-store);\n");
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals(ResultCode.ERROR_SETUP,test.setup("123","123","testFile.txt").getResultCode());

    }

    @Test
    public void testSetupNonExistFile(){
        assertEquals(ResultCode.ERROR_SETUP,test.setup("123","123","2Boys1Test.txt").getResultCode());

    }

    /*
    a. Register users u1,u2,u3,u4,u5,u6.
    b. Make u1 admin.
    c. u2 open store s1.
    d. u2 add item “diapers” to store s1 with cost 30 and quantity 20.
    e. u2 appoint u3 to a store manager with permission to manage inventory.
    f. u3 appoint u5 and u5 appoint u6 to a store manager.
     */
    @Test
    public void testSetupRequirmentsFile() throws DatabaseFetchException {
        try {
            FileWriter file = new FileWriter("initFile.txt",false);
            file.write("register(u1);\nregister(u2);\nregister(u3);\nregister(u4);\nregister(u5);\nregister(u6);\n"+
                    "set-admin(u1);\nopen-store(u2,s1);\nadd-product(u2,s1,diapers,20,30);\n"+
                    "appoint-owner(u2,s1,u3,manage-inventory);\nappoint-owner(u3,s1,u5,manager);\nappoint-manager(u5,s1,u6,manager);\n");
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        test.setup("123","123","initFile.txt");
        assertEquals(4,test.getStoreById(test.getStoreByName("s1")).getAllManagers().size());
    }


    //Test for new usecase 4.3

    /**
     * Amir appoints two owners, therfore the second one should br approved
     * test id there are 3 managers after approval
     */
    @Test
    public void testApproveStoreOwnerSuccess() throws DatabaseFetchException {
        int sessionId = test.startSession().getId();
        test.register(sessionId,"amir","1234");
        int ownerId1 = test.register(sessionId,"bob","1234").getId();
        int ownerId2 = test.register(sessionId,"mo","1234").getId();
        test.login(sessionId,"amir","1234");
        int storeId = test.openStore(sessionId).getId();
        Store store = test.getStoreById(storeId);
        test.addStoreOwner(sessionId,storeId,ownerId1);
        test.addStoreOwner(sessionId,storeId,ownerId2);
        test.logout(sessionId);
        test.login(sessionId,"bob","1234");
        test.approveStoreOwner(sessionId,storeId,ownerId2);

        assertEquals(3, store.getAllManagers().size());

    }

    /**
     * Amir appoints two owners, therfore the second one should br approved
     * test id there are no pending agreemant after approval
     */
    @Test
    public void testApproveStoreOwnerAgreementRemoved() throws DatabaseFetchException {
        int sessionId = test.startSession().getId();
        test.register(sessionId,"amir","1234");
        int ownerId1 = test.register(sessionId,"bob","1234").getId();
        int ownerId2 = test.register(sessionId,"mo","1234").getId();
        test.login(sessionId,"amir","1234");
        int storeId = test.openStore(sessionId).getId();
        Store store = test.getStoreById(storeId);
        test.addStoreOwner(sessionId,storeId,ownerId1);
        test.addStoreOwner(sessionId,storeId,ownerId2);
        test.logout(sessionId);
        test.login(sessionId,"bob","1234");
        test.approveStoreOwner(sessionId,storeId,ownerId2);

        assertEquals(0, store.getAllGrantingAgreements().size());

    }

    /**
     * Check if the need to approve Manager got notification after appoint new manager
     */
    @Test
    public void testApproveStoreOwnerNotificationSent() throws DatabaseFetchException {
        int sessionId = test.startSession().getId();
        test.register(sessionId,"amir","1234");
        int ownerId1 = test.register(sessionId,"bob","1234").getId();
        Subscriber bob = test.getUserHandler().getSubscriber(ownerId1);
        int ownerId2 = test.register(sessionId,"mo","1234").getId();
        test.login(sessionId,"amir","1234");
        int storeId = test.openStore(sessionId).getId();
        test.addStoreOwner(sessionId,storeId,ownerId1);
        int before = bob.getAllNotification().size();
        test.addStoreOwner(sessionId,storeId,ownerId2);
        int after = bob.getAllNotification().size();

        assertEquals(1,after-before);




    }
    /**
     * Amir appoints two owners, therfore the second one should br approved
     * test id there are 2 managers before approval
     */
    @Test
    public void testNoApproveStoreOwner() throws DatabaseFetchException {
        int sessionId = test.startSession().getId();
        test.register(sessionId,"amir","1234");
        int ownerId1 = test.register(sessionId,"bob","1234").getId();
        int ownerId2 = test.register(sessionId,"mo","1234").getId();
        test.login(sessionId,"amir","1234");
        int storeId = test.openStore(sessionId).getId();
        Store store = test.getStoreById(storeId);
        test.addStoreOwner(sessionId,storeId,ownerId1);
        test.addStoreOwner(sessionId,storeId,ownerId2);


        assertEquals(2, store.getAllManagers().size());

    }

    /**
     * Amir appoints two owners, therfore the second one should br approved
     * test id there are no pending agreemants after approval
     */
    @Test
    public void testNoApproveStoreOwnerAgreementExist() throws DatabaseFetchException {
        int sessionId = test.startSession().getId();
        test.register(sessionId,"amir","1234");
        int ownerId1 = test.register(sessionId,"bob","1234").getId();
        int ownerId2 = test.register(sessionId,"mo","1234").getId();
        test.login(sessionId,"amir","1234");
        int storeId = test.openStore(sessionId).getId();
        Store store = test.getStoreById(storeId);
        test.addStoreOwner(sessionId,storeId,ownerId1);
        test.addStoreOwner(sessionId,storeId,ownerId2);



        assertEquals(1, store.getAllGrantingAgreements().size());

    }

    /**
     * try to approve owner with non existing agreement;
     */
    @Test
    public void testApproveStoreOwnerNoAgreement() throws DatabaseFetchException {
        int sessionId = test.startSession().getId();
        test.register(sessionId,"amir","1234");
        int ownerId1 = test.register(sessionId,"bob","1234").getId();
        test.login(sessionId,"amir","1234");
        int storeId = test.openStore(sessionId).getId();

        assertEquals(ResultCode.ERROR_STORE_OWNER_MODIFICATION
                    ,test.approveStoreOwner(sessionId,storeId,ownerId1).getResultCode());

    }

    /**
     * Test if by removal all pending owners to approve, owning request gets approved.
     */
    @Test
    public void testDeleteOwnerToApprove() throws DatabaseFetchException {
        int sessionId = test.startSession().getId();
        test.register(sessionId,"amir","1234");
        int ownerId1 = test.register(sessionId,"bob","1234").getId();
        int ownerId2 = test.register(sessionId,"mo","1234").getId();
        test.login(sessionId,"amir","1234");
        int storeId = test.openStore(sessionId).getId();
        Store store = test.getStoreById(storeId);
        test.addStoreOwner(sessionId,storeId,ownerId1);
        test.addStoreOwner(sessionId,storeId,ownerId2);
        test.deleteOwner(sessionId,storeId,ownerId1);

        List<Subscriber> managers = store.getAllManagers();
        boolean own1=false,own2=false;
        for(Subscriber subscriber : managers){
            if(subscriber.getId()==ownerId1)
                own1 = true;
            if(subscriber.getId() == ownerId2)
                own2=true;
        }
        assertTrue(own2 && !own1);

    }

    @Test
    public void testDeclineStoreOwnerSuccess() throws DatabaseFetchException {
        int sessionId = test.startSession().getId();
        test.register(sessionId,"amir","1234");
        int ownerId1 = test.register(sessionId,"bob","1234").getId();
        int ownerId2 = test.register(sessionId,"mo","1234").getId();
        test.login(sessionId,"amir","1234");
        int storeId = test.openStore(sessionId).getId();
        Store store = test.getStoreById(storeId);
        test.addStoreOwner(sessionId,storeId,ownerId1);
        test.addStoreOwner(sessionId,storeId,ownerId2);
        test.logout(sessionId);
        test.login(sessionId,"bob","1234");
        test.declineStoreOwner(sessionId,storeId,ownerId2);

        assertEquals(2, store.getAllManagers().size());
    }

    public void setUpStatsTests(){
        sessionId =test.startSession().getId();
        test.register(sessionId,"a","a");
        test.register(sessionId,"b","b");
    }

    @Test
    public void testStatisticsGuestLogin(){
        setUpStatsTests();
        assertEquals(1,test.getDailyStats().getGuests());
    }

    @Test
    public void testStatisticsSubscriberLogin(){
        setUpStatsTests();
        try {
            test.login(sessionId,"a","a");
        } catch (DatabaseFetchException e) {
            e.printStackTrace();
        }
        assertEquals(1,test.getDailyStats().getRegularSubs());
    }

    @Test
    public void testStatisticsOwnerLogin(){
        setUpStatsTests();
        sessionId =test.startSession().getId();
        try {
            test.login(sessionId,"a","a");
        } catch (DatabaseFetchException e) {
            e.printStackTrace();
        }
        test.openStore(sessionId);
        test.logout(sessionId);
        try {
            test.login(sessionId,"a","a");
        } catch (DatabaseFetchException e) {
            e.printStackTrace();
        }
        assertEquals(1,test.getDailyStats().getManagersOwners());

    }

    @Test
    public void testStatisticsManagerLogin(){
        setUpStatsTests();
        sessionId =test.startSession().getId();
        try {
            test.login(sessionId,"a","a");
        } catch (DatabaseFetchException e) {
            e.printStackTrace();
        }
        int storeId =test.openStore(sessionId).getId();
        test.logout(sessionId);
        try {
            test.login(sessionId,"a","a");
        } catch (DatabaseFetchException e) {
            e.printStackTrace();
        }
        int subId = 0;
        try {
            subId = test.getSubscriber("b","b");
        } catch (DatabaseFetchException e) {
            e.printStackTrace();
        }
        test.addStoreManager(sessionId,storeId,subId);
        test.logout(sessionId);
        try {
            test.login(sessionId,"b","b");
        } catch (DatabaseFetchException e) {
            e.printStackTrace();
        }
        assertEquals(1,test.getDailyStats().getManagersNotOwners());
    }




}