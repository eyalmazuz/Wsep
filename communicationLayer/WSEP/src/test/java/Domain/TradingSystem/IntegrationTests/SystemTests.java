package Domain.TradingSystem.IntegrationTests;

import DTOs.Notification;
import DTOs.ResultCode;
import Domain.TradingSystem.System;
import Domain.TradingSystem.*;
import NotificationPublisher.MessageBroker;
import NotificationPublisher.Publisher;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class SystemTests extends TestCase {

    //System Unitesting
    System test;

    @Before
    public void setUp() {
        test = new System();
    }

    @After
    public void tearDown() {
        test.deleteStores();
        test.deleteUsers();
    }



    //USE CASES 4.1 tests

    @Test
    public void testAddProductToStore() {
        int sessionId = test.startSession().getId();
        test.register(sessionId, "eyal", "1234");
        test.login(sessionId, "eyal", "1234");
        int storeId = test.openStore(sessionId).getId();
        test.addProductInfo(1, "bamba", "hatif");
        assertSame(test.addProductToStore(sessionId, storeId, 1, 5).getResultCode(), ResultCode.SUCCESS);
        assertNotSame(test.addProductToStore(sessionId, storeId, 3, 5).getResultCode(), ResultCode.SUCCESS); //productid does not exist

    }

    @Test
    public void testEditProductInStore() {
        int sessionId = test.startSession().getId();
        test.register(sessionId, "eyal", "1234");
        test.login(sessionId, "eyal", "1234");
        int storeId = test.openStore(sessionId).getId();
        test.addProductInfo(1, "bamba", "hatif");
        assertNotSame(test.editProductInStore(sessionId, storeId, 1, "contains peanuts").getResultCode(), ResultCode.SUCCESS);
        test.addProductToStore(sessionId, storeId, 1, 5);
        assertSame(test.editProductInStore(sessionId, storeId, 1, "contains peanuts").getResultCode(), ResultCode.SUCCESS);
        assertNotSame(test.editProductInStore(sessionId, storeId, -12, "contains peanuts").getResultCode(), ResultCode.SUCCESS);

    }


    @Test
    public void testDeleteProductFromStore() {
        int sessionId = test.startSession().getId();
        test.register(sessionId, "eyal", "1234");
        test.login(sessionId, "eyal", "1234");
        int storeId = test.openStore(sessionId).getId();
        test.addProductInfo(1, "bamba", "hatif");
        test.addProductToStore(sessionId, storeId, 1, 5);
        assertSame(test.deleteProductFromStore(sessionId, storeId, 1).getResultCode(), ResultCode.SUCCESS);
        assertNotSame(test.deleteProductFromStore(sessionId, storeId, 2).getResultCode(), ResultCode.SUCCESS);

    }


    // USECASE 2.8

    @Test
    public void testCheckSuppliesAndGetPrice() {
        int sessionId = test.startSession().getId();
        Store store1 = new Store();
        ProductInfo pi4 = new ProductInfo(4, "lambda", "snacks");
        store1.addProduct(pi4 , 5);
        User u = test.getUser(sessionId);
        u.addProductToCart(store1, pi4, 4);
        assertEquals(test.checkSuppliesAndGetPrice(sessionId), 0.0);

        u.addProductToCart(store1, pi4, 10);
        assertEquals(test.checkSuppliesAndGetPrice(sessionId), -1.0);
    }

    @Test
    public void testMakePaymentFail() {
        // keep track of the original cart, history, store supplies

        int sessionId = test.startSession().getId();
        Store store1 = new Store();
        ProductInfo pi4 = new ProductInfo(4, "lambda", "snacks");
        store1.addProduct(pi4, 5);
        User u = test.getUser(sessionId);
        u.addProductToCart(store1, pi4, 4);
        u.setState(new Subscriber());

        PaymentHandler paymentHandler = null;
        try {
            paymentHandler = new PaymentHandler("None");
            test.setPaymentHandler(paymentHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
        paymentHandler.setProxyPurchaseSuccess(false);
        assertNotSame(test.makePayment(sessionId, "details").getResultCode(), ResultCode.SUCCESS);

        // make sure nothing was changed
        assertTrue(u.getUserPurchaseHistory().getStorePurchaseLists().isEmpty());
        assertTrue(store1.getStorePurchaseHistory().isEmpty());
        assertEquals(store1.getProductAmount(4), 5);
        assertNotNull(u.getShoppingCart().getStoreProductsIds().get(store1.getId()));
        assertEquals((int) u.getShoppingCart().getStoreProductsIds().get(store1.getId()).get(4), 4);
    }

    @Test
    public void testSavePurchaseHistory() {
        int sessionId = test.startSession().getId();
        Store store1 = new Store();
        ProductInfo info = new ProductInfo(4, "lambda", "snacks");
        store1.addProduct(info, 5);
        User u = test.getUser(sessionId);
        u.addProductToCart(store1, info, 4);

        u.setState(new Subscriber());  // so it should indeed be saved
        u.saveCurrentCartAsPurchase();
        Map<Store, List<PurchaseDetails>> map = u.getUserPurchaseHistory().getStorePurchaseLists();
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
    public void testUpdateStoreSupplies() {
        int sessionId = test.startSession().getId();
        Store store1 = new Store();
        ProductInfo info = new ProductInfo(4, "lambda", "snacks");
        store1.addProduct(info, 5);
        User u = test.getUser(sessionId);
        u.addProductToCart(store1, info, 4);

        test.updateStoreSupplies(sessionId);
        // after this the store should have 4 less lambda

        assertEquals(store1.getProductAmount(4), 1);
    }

    @Test
    public void testSaveOngoingPurchaseForUser() {
        int sessionId = test.startSession().getId();
        Store store1 = new Store();
        ProductInfo info = new ProductInfo(4, "lambda", "snacks");
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
    public void testRequestSupply() {
        int sessionId = test.startSession().getId();
        Store store1 = new Store();
        ProductInfo info = new ProductInfo(4, "lambda", "snacks");
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

        supplyHandler.setProxySupplySuccess(false);
        assertFalse(test.requestSupply(sessionId));

        supplyHandler.setProxySupplySuccess(true);
        assertTrue(test.requestSupply(sessionId));
    }

    @Test
    public void testRestoreSupplies() {
        int sessionId = test.startSession().getId();
        int id = test.addStore();
        Store store1 = test.getStores().get(id);
        ProductInfo info = new ProductInfo(4, "lambda", "snacks");
        store1.addProduct(info, 5);
        User u = test.getUser(sessionId);
        u.addProductToCart(store1, info, 4);
        test.saveOngoingPurchaseForUser(sessionId);
        test.updateStoreSupplies(sessionId); // this REMOVES the items. now we wanna return them

        test.restoreSupplies(sessionId);
        assertEquals(store1.getProductAmount(4), 5);
    }

    @Test
    public void testRestoreHistories() {
        int sessionId = test.startSession().getId();
        int id = test.addStore();
        Store store1 = test.getStores().get(id);
        ProductInfo info = new ProductInfo(4, "lambda", "snacks");
        store1.addProduct(info, 5);
        User u = test.getUser(sessionId);
        u.setState(new Subscriber());
        u.addProductToCart(store1, info, 4);
        test.saveOngoingPurchaseForUser(sessionId);

        test.savePurchaseHistory(sessionId); // this saves history, now we wanna see it gone

        test.restoreHistories(sessionId);
        assertTrue(u.getUserPurchaseHistory().getStorePurchaseLists().isEmpty());
    }

    @Test
    public void testRestoreCart() {
        int sessionId = test.startSession().getId();
        int id =test.addStore();
        Store store1 = test.getStores().get(id);
        test.addProductInfo(4, "lambda", "snacks");
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
    public void testRemoveOngoingPurchase() {
        int sessionId = test.startSession().getId();
        int id = test.addStore();
        Store store1 = test.getStores().get(id);
        ProductInfo info = new ProductInfo(4, "lambda", "snacks");
        store1.addProduct(info, 5);
        User u = test.getUser(sessionId);
        u.setState(new Subscriber());
        u.addProductToCart(store1, info, 4);
        test.saveOngoingPurchaseForUser(sessionId); // now remove it

        test.removeOngoingPurchase(sessionId);
        assertNull(test.getOngoingPurchases().get(sessionId));
    }

    private int sessionId, id;
    private Store store1;
    private ProductInfo info;
    private User u;
    private PaymentHandler paymentHandler = null;
    private SupplyHandler supplyHandler = null;

    private void setUpPurchase() {
        sessionId = test.startSession().getId();
        id = test.addStore();
        store1 = test.getStores().get(id);
        test.addProductInfo(4, "lambda", "snacks");
        info = test.getProductInfoById(4);
        store1.addProduct(info, 5);
        u = test.getUser(sessionId);
        u.setState(new Subscriber());
        u.addProductToCart(store1, info, 4);

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
    public void testPurchaseFailBuyingPolicy() {
        setUpPurchase();
        store1.setBuyingPolicy(new BuyingPolicy("No one is allowed"));
        assertNotSame(test.checkBuyingPolicy(sessionId).getResultCode(), ResultCode.SUCCESS);
        assertTrue(checkPurchaseProcessNoChanges(u, store1));
    }

    @Test
    public void testPurhcaseFailMissingSupplies() {
        setUpPurchase();

        store1.setBuyingPolicy(new BuyingPolicy("None"));
        u.editCartProductAmount(store1, info, 6);
        double price = test.checkSuppliesAndGetPrice(sessionId);
        assertEquals(price, -1.0);
        u.editCartProductAmount(store1, info, 4);
        assertTrue(checkPurchaseProcessNoChanges(u, store1));
    }

    @Test
    public void testPurchaseFailPaymentSystem() {
        setUpPurchase();
        paymentHandler.setProxyPurchaseSuccess(false);
        confirmPurchase(sessionId, false);
        assertTrue(checkPurchaseProcessNoChanges(u, store1));
    }

    @Test
    public void testPurchaseFailSupplySystem() {
        setUpPurchase();
        supplyHandler.setProxySupplySuccess(false);
        confirmPurchase(sessionId, false);
        assertTrue(checkPurchaseProcessNoChanges(u, store1));
    }

    @Test
    public void testPurchaseFailSyncProblem() {
        setUpPurchase();
        confirmPurchase(sessionId, true);
        assertTrue(checkPurchaseProcessNoChanges(u, store1));

    }

    private void confirmPurchase(int sessionId, boolean syncProblem) {
        if (test.makePayment(sessionId, "details").getResultCode() == ResultCode.SUCCESS) {
            test.savePurchaseHistory(sessionId);
            test.saveOngoingPurchaseForUser(sessionId);

            // updateStoreSupplies would fail only if there is a sync problem
            if (!syncProblem) {
                test.updateStoreSupplies(sessionId);
                test.emptyCart(sessionId);
            } else {
                test.requestRefund(sessionId);
                test.restoreHistories(sessionId);
                test.removeOngoingPurchase(sessionId);
                return;
            }
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
            store.getStorePurchaseHistory().isEmpty() &&
            store.getProductAmount(4) == 5 &&
            u.getShoppingCart().getStoreProductsIds().get(store.getId()).get(4) == 4 &&
            (int) u.getShoppingCart().getStoreProductsIds().get(store.getId()).get(4) == 4;
    }

    @Test
    public void testAddOwnerSucess() {
        int openerSessionId = test.startSession().getId();
        test.register(openerSessionId, "Amir", "1234");
        test.login(openerSessionId, "Amir", "1234");
        int storeid = test.openStore(openerSessionId).getId();

        int newOwnerSessionId = test.startSession().getId();
        int newOwnerSubId = test.register(newOwnerSessionId, "Bob", "1234").getId();

        assertSame(test.addStoreOwner(openerSessionId, storeid, newOwnerSubId).getResultCode(), ResultCode.SUCCESS);

    }

    @Test
    public void testAddManager() {
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
    public void testDeleteManager() {
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
    public void testSetManagerDetails() {
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
    public void testGetHistory() {
        int sessionId = test.startSession().getId();
        int id = test.addStore();
        Store store1 = test.getStores().get(id);
        ProductInfo info = new ProductInfo(4, "lambda", "snacks");
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

        String history = "Basket Purchase for store ID: "+store1.getId()+"\nlambda\nAmount: 4\nPrice: 0.0\n\n";

        store1.setBuyingPolicy(new BuyingPolicy("Any"));
        u.saveCurrentCartAsPurchase();

        assertEquals(history, u.getHistory().toString());

    }

    @Test
    public void testBuyingPoliciesSimple() {
        int sessionId = test.startSession().getId();
        int storeId = test.addStore();
        Store store1 = test.getStores().get(storeId);
        ProductInfo info = new ProductInfo(4, "lambda", "snacks");
        store1.addProduct(info, 10);
        User u = test.getUser(sessionId);
        u.setState(new Subscriber());
        u.addProductToCart(store1, info, 4);

        // bad
        BuyingPolicy policy = new BuyingPolicy("No one is allowed");
        store1.setBuyingPolicy(policy);
        assertNotSame(test.checkBuyingPolicy(sessionId).getResultCode(), ResultCode.SUCCESS);

        // good
        policy.setDetails("blah");
        assertSame(test.checkBuyingPolicy(sessionId).getResultCode(), ResultCode.SUCCESS);

        // bad
        policy.addBuyingType(new BasketBuyingConstraint.MinAmountForProductConstraint(info, 5));
        store1.setBuyingPolicy(policy);
        assertNotSame(test.checkBuyingPolicy(sessionId).getResultCode(), ResultCode.SUCCESS);

        policy.clearBuyingTypes();
        // good
        policy.addBuyingType(new BasketBuyingConstraint.MinAmountForProductConstraint(info, 2));
        assertSame(test.checkBuyingPolicy(sessionId).getResultCode(), ResultCode.SUCCESS);

        policy.clearBuyingTypes();
        // bad
        policy.addBuyingType(new BasketBuyingConstraint.MaxAmountForProductConstraint(info, 3));
        store1.setBuyingPolicy(policy);
        assertNotSame(test.checkBuyingPolicy(sessionId).getResultCode(), ResultCode.SUCCESS);

        policy.clearBuyingTypes();
        // good
        policy.addBuyingType(new BasketBuyingConstraint.MaxAmountForProductConstraint(info, 4));
        store1.setBuyingPolicy(policy);
        assertSame(test.checkBuyingPolicy(sessionId).getResultCode(), ResultCode.SUCCESS);

        policy.clearBuyingTypes();
        // bad
        policy.addBuyingType(new BasketBuyingConstraint.MaxProductAmountConstraint(40));
        u.addProductToCart(store1, info, 39);
        assertNotSame(test.checkBuyingPolicy(sessionId).getResultCode(), ResultCode.SUCCESS);

        policy.clearBuyingTypes();
        // good
        u.editCartProductAmount(store1, info, 39);
        assertSame(test.checkBuyingPolicy(sessionId).getResultCode(), ResultCode.SUCCESS);

        policy.clearBuyingTypes();
        // bad
        int today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        policy.addBuyingType(new SystemBuyingConstraint.NotOnDayConstraint(today));
        assertNotSame(test.checkBuyingPolicy(sessionId).getResultCode(), ResultCode.SUCCESS);

        policy.clearBuyingTypes();
        // good
        policy.addBuyingType(new SystemBuyingConstraint.NotOnDayConstraint(today + 1));
        assertSame(test.checkBuyingPolicy(sessionId).getResultCode(), ResultCode.SUCCESS);

        policy.clearBuyingTypes();
        // bad
        policy.addBuyingType(new UserBuyingConstraint.NotOutsideCountryConstraint("Israel"));
        u.setCountry("Brazil");
        assertNotSame(test.checkBuyingPolicy(sessionId).getResultCode(), ResultCode.SUCCESS);

        policy.clearBuyingTypes();
        // good
        u.setCountry("Israel");
        assertSame(test.checkBuyingPolicy(sessionId).getResultCode(), ResultCode.SUCCESS);
    }

    @Test
    public void testBuyingPoliciesComplex() {
        int sessionId = test.startSession().getId();
        int storeId = test.addStore();
        Store store1 = test.getStores().get(storeId);
        ProductInfo info = new ProductInfo(4, "lambda", "snacks");
        store1.addProduct(info, 10);
        User u = test.getUser(sessionId);
        u.setState(new Subscriber());
        u.addProductToCart(store1, info, 4);

        BuyingPolicy policy = new BuyingPolicy("blah");
        store1.setBuyingPolicy(policy);
        List<BuyingType> buyingConstraints = new ArrayList<>();
        buyingConstraints.add(new BasketBuyingConstraint.MinAmountForProductConstraint(info, 5));
        buyingConstraints.add(new BasketBuyingConstraint.MaxAmountForProductConstraint(info, 10));
        policy.addBuyingType(new AdvancedBuying.LogicalBuying(buyingConstraints, AdvancedBuying.LogicalOperation.AND));

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
        policy.addBuyingType(new AdvancedBuying.LogicalBuying(buyingConstraints, AdvancedBuying.LogicalOperation.IMPLIES));

        // its today and user wants more than 20 - ok
        u.editCartProductAmount(store1, info, 30);
        assertSame(test.checkBuyingPolicy(sessionId).getResultCode(), ResultCode.SUCCESS);

        // its not today (huh?) and user wants more than 20 - bad!

        buyingConstraints.clear();
        buyingConstraints.add(new SystemBuyingConstraint.NotOnDayConstraint(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) + 1));
        buyingConstraints.add(new BasketBuyingConstraint.MaxAmountForProductConstraint(info, 20));
        assertNotSame(test.checkBuyingPolicy(sessionId).getResultCode(), ResultCode.SUCCESS);
    }

    @Test
    public void testDiscountPoliciesSimple() {
        int sessionId = test.startSession().getId();
        int store1Id = test.addStore();
        Store store1 = test.getStores().get(store1Id);
        test.openStore(sessionId);

        test.addProductInfo(4, "bamba", "snacks");
        test.addProductInfo(5, "apple", "fruits");
        ProductInfo infoBamba = test.getProductInfoById(4);
        ProductInfo infoApple = test.getProductInfoById(5);

        store1.addProduct(infoBamba, 10);
        store1.setProductPrice(4, 10);
        store1.addProduct(infoApple, 5);
        store1.setProductPrice(5, 20);

        User u = test.getUser(sessionId);
        u.setState(new Subscriber());

        DiscountPolicy policy = new DiscountPolicy();
        policy.addDiscount(new ProductDiscount.ProductSaleDiscount(4, 0.5));
        store1.setDiscountPolicy(policy);
        Map<ProductInfo, Integer> productsAmount = new HashMap<>();
        productsAmount.put(infoBamba, 1);
        assertEquals(5.0, store1.getPrice(u, productsAmount).getPrice());
        policy.clearDiscounts();


        productsAmount = new HashMap<>();
        productsAmount.put(infoBamba, 5);
        assertEquals(5 * 10.0, store1.getPrice(u, productsAmount).getPrice());



        policy = new DiscountPolicy();
        policy.addDiscount(new ProductDiscount.ProductSaleDiscount(4, 0.5));
        store1.setDiscountPolicy(policy);
        productsAmount = new HashMap<>();
        productsAmount.put(infoBamba, 5);
        assertEquals(5 * 5.0, store1.getPrice(u, productsAmount).getPrice());
        policy.clearDiscounts();


        policy = new DiscountPolicy();
        policy.addDiscount(new ProductDiscount.ProductSaleDiscount(4, 0.5));
        policy.addDiscount(new ProductDiscount.ProductSaleDiscount(5, 0.75));
        store1.setDiscountPolicy(policy);
        productsAmount = new HashMap<>();
        productsAmount.put(infoBamba, 1);
        assertEquals(5.0, store1.getPrice(u, productsAmount).getPrice());
        policy.clearDiscounts();


        policy = new DiscountPolicy();
        policy.addDiscount(new ProductDiscount.ProductSaleDiscount(4, 0.5));
        policy.addDiscount(new ProductDiscount.ProductSaleDiscount(5, 0.75));
        store1.setDiscountPolicy(policy);

        productsAmount = new HashMap<>();
        productsAmount.put(infoBamba, 5);
        productsAmount.put(infoApple, 10);

        assertEquals(0.5 * 5 * 10 + 0.75 * 10 * 20, store1.getPrice(u, productsAmount).getPrice());
        policy.clearDiscounts();


        policy = new DiscountPolicy();
        policy.addDiscount(new ProductDiscount.CategorySaleDiscount("fruits", 0.75));

        store1.setDiscountPolicy(policy);

        productsAmount = new HashMap<>();
        productsAmount.put(infoApple, 10);

        assertEquals(0.75*10*20, store1.getPrice(u, productsAmount).getPrice());
        policy.clearDiscounts();


        policy = new DiscountPolicy();
        policy.addDiscount(new ProductDiscount.CategorySaleDiscount("fruits", 0.75));

        store1.setDiscountPolicy(policy);

        productsAmount = new HashMap<>();
        productsAmount.put(infoBamba, 5);
        productsAmount.put(infoApple, 10);
        assertEquals(5*10 + 0.75*10*20, store1.getPrice(u, productsAmount).getPrice());
        policy.clearDiscounts();


        policy = new DiscountPolicy();
        policy.addDiscount(new ProductDiscount.ProductSaleDiscount(4, 0.5));
        policy.addDiscount(new ProductDiscount.ProductSaleDiscount(5, 0.75));
        policy.addDiscount(new ProductDiscount.CategorySaleDiscount("fruits", 0.5));

        store1.setDiscountPolicy(policy);

        productsAmount = new HashMap<>();
        productsAmount.put(infoBamba, 5);
        productsAmount.put(infoApple, 10);

        assertEquals(0.5 * 5 * 10 + 0.75 * 10 * 20 * 0.5, store1.getPrice(u, productsAmount).getPrice());
        policy.clearDiscounts();
        test.removeProduct(4);
        test.removeProduct(5);

    }

    @Test
    public void testDiscountPoliciesComplex() {
        int sessionId = test.startSession().getId();
        int store1Id = test.addStore();
        Store store1 = test.getStores().get(store1Id);
        test.openStore(sessionId);

        test.addProductInfo(4, "bamba", "snacks");
        test.addProductInfo(5, "apple", "fruits");
        ProductInfo infoBamba = test.getProductInfoById(4);
        ProductInfo infoApple = test.getProductInfoById(5);

        store1.addProduct(infoBamba, 10);
        store1.setProductPrice(4, 10);
        store1.addProduct(infoApple, 30);
        store1.setProductPrice(5, 20);

        User u = test.getUser(sessionId);
        u.setState(new Subscriber());


        DiscountPolicy policy = new DiscountPolicy();

        List<DiscountType> discounts = new ArrayList<>();
        discounts.add(new ProductDiscount.ProductSaleDiscount(4, 0.5));
        discounts.add(new ProductDiscount.CategorySaleDiscount("fruits", 0.75));


        policy.addDiscount(new AdvancedDiscount.LogicalDiscount(discounts, AdvancedDiscount.LogicalOperation.AND));
        store1.setDiscountPolicy(policy);
        Map<ProductInfo, Integer> productsAmount = new HashMap<>();
        productsAmount.put(infoBamba, 5);
        productsAmount.put(infoApple, 10);


        assertEquals(0.5 * 5 * 10 + 0.75 * 10 * 20, store1.getPrice(u, productsAmount).getPrice());
        policy.clearDiscounts();


        discounts = new ArrayList<>();
        discounts.add(new ProductDiscount.CategorySaleDiscount("fruits", 0.75));
        discounts.add(new ProductDiscount.ProductSaleDiscount(4, 0.5));

        policy = new DiscountPolicy();
        policy.addDiscount(new AdvancedDiscount.LogicalDiscount(discounts, AdvancedDiscount.LogicalOperation.OR));
        store1.setDiscountPolicy(policy);
        productsAmount = new HashMap<>();
        productsAmount.put(infoBamba, 5);
        productsAmount.put(infoApple, 10);


        assertEquals(5*10 + 0.75*20*10, store1.getPrice(u, productsAmount).getPrice());
        policy.clearDiscounts();


        discounts = new ArrayList<>();
        discounts.add(new ProductDiscount.CategorySaleDiscount("fruits", 0.75));
        discounts.add(new ProductDiscount.ProductSaleDiscount(4, 0.5));
        policy = new DiscountPolicy();
        policy.addDiscount(new AdvancedDiscount.LogicalDiscount(discounts, AdvancedDiscount.LogicalOperation.XOR));
        store1.setDiscountPolicy(policy);
        productsAmount = new HashMap<>();
        productsAmount.put(infoBamba, 5);
        productsAmount.put(infoApple, 10);


        assertEquals(0.5*5*10 + 10*20, store1.getPrice(u, productsAmount).getPrice());
        policy.clearDiscounts();
        test.removeProduct(4);
        test.removeProduct(5);

    }

    @Test
    public void testAddProductUpdateNotification(){
        int counter;
        Publisher publisherMock = new Publisher(new MessageBroker() {

            @Override
            public List<Integer> sendTo(List<Integer> subscribers, Object message) {

                return null;
            }
        });
        test.setPublisher(publisherMock);
        int openerSessionId = test.startSession().getId();
        int subId = test.register(openerSessionId,"Amir","1234").getId();
        test.login(openerSessionId,"Amir","1234");
        int storeid = test.openStore(openerSessionId).getId();
        test.addProductInfo(4, "bamba", "snacks");
        test.addProductInfo(5, "apple", "fruits");
        ProductInfo infoBamba = test.getProductInfoById(4);
        ProductInfo infoApple = test.getProductInfoById(5);

        test.addProductToStore(openerSessionId,storeid,4,5);
        Queue<Notification> noties = test.getUserHandler().getSubscriber(subId).getAllNotification();
        assertEquals(1,noties.size());



    }
}