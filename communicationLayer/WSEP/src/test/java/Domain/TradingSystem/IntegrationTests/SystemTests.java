package Domain.TradingSystem.IntegrationTests;

import DTOs.ActionResultDTO;
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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class SystemTests extends TestCase {

    //System Unitesting
    System test;

    @Before
    public void setUp() {
        test = new System();
        Publisher publisherMock = new Publisher(new MessageBroker() {

            @Override
            public List<Integer> sendTo(List<Integer> subscribers, Object message) {

                return null;
            }
        });
        test.setPublisher(publisherMock);

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
        test.addProductInfo(1, "bamba", "hatif", 10);
        assertSame(test.addProductToStore(sessionId, storeId, 1, 5).getResultCode(), ResultCode.SUCCESS);
        assertNotSame(test.addProductToStore(sessionId, storeId, 3, 5).getResultCode(), ResultCode.SUCCESS); //productid does not exist

    }

    @Test
    public void testEditProductInStore() {
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
    public void testDeleteProductFromStore() {
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
    public void testSavePurchaseHistory() {
        int sessionId = test.startSession().getId();
        Store store1 = new Store();
        ProductInfo info = new ProductInfo(4, "lambda", "snacks", 10);
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
    public void testSaveOngoingPurchaseForUser() {
        int sessionId = test.startSession().getId();
        Store store1 = new Store();
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
    public void testRequestSupply() {
        int sessionId = test.startSession().getId();
        Store store1 = new Store();
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
        ProductInfo info = new ProductInfo(4, "lambda", "snacks", 10);
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
        ProductInfo info = new ProductInfo(4, "lambda", "snacks", 10);
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
    public void testRemoveOngoingPurchase() {
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
        test.addProductInfo(4, "lambda", "snacks", 10);
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
    public void testPurchaseSuccess() {
        setUpPurchase();

        assertFalse(test.isCartEmpty(sessionId));
        assertSame(test.checkBuyingPolicy(sessionId).getResultCode(), ResultCode.SUCCESS);
        double price = test.checkSuppliesAndGetPrice(sessionId);
        assertFalse(price < 0);

        assertSame(test.makePayment(sessionId, "details").getResultCode(), ResultCode.SUCCESS);
        test.savePurchaseHistory(sessionId);
        test.saveOngoingPurchaseForUser(sessionId);

        assertTrue(test.updateStoreSupplies(sessionId));
        test.emptyCart(sessionId);
        assertTrue(test.requestSupply(sessionId));
        test.removeOngoingPurchase(sessionId);

        // check state
        assertTrue(!u.getUserPurchaseHistory().getStorePurchaseLists().isEmpty() &&
                !store1.getStorePurchaseHistory().isEmpty() &&
                store1.getProductAmount(4) == 1 &&
                 u.getShoppingCart().isEmpty() && !u.getUserPurchaseHistory().getStorePurchaseLists().isEmpty());
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
            u.getShoppingCart().getStoreProductsIds().get(store.getId()).get(4) == 4;
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

    @Test
    public void testBuyingPoliciesSimple() {
        int sessionId = test.startSession().getId();
        int storeId = test.addStore();
        Store store1 = test.getStores().get(storeId);
        ProductInfo info = new ProductInfo(4, "lambda", "snacks", 10);
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

        DiscountPolicy policy = new DiscountPolicy("test");
        policy.addDiscountType(new ProductDiscount.ProductSaleDiscount(4, 0.5));
        store1.setDiscountPolicy(policy);
        Map<ProductInfo, Integer> productsAmount = new HashMap<>();
        productsAmount.put(infoBamba, 1);
        assertEquals(5.0, store1.getPrice(u, productsAmount).getPrice());
        policy.clearDiscountTypes();


        productsAmount = new HashMap<>();
        productsAmount.put(infoBamba, 5);
        assertEquals(5 * 10.0, store1.getPrice(u, productsAmount).getPrice());



        policy = new DiscountPolicy("test");
        policy.addDiscountType(new ProductDiscount.ProductSaleDiscount(4, 0.5));
        store1.setDiscountPolicy(policy);
        productsAmount = new HashMap<>();
        productsAmount.put(infoBamba, 5);
        assertEquals(5 * 5.0, store1.getPrice(u, productsAmount).getPrice());
        policy.clearDiscountTypes();


        policy = new DiscountPolicy("test");
        policy.addDiscountType(new ProductDiscount.ProductSaleDiscount(4, 0.5));
        policy.addDiscountType(new ProductDiscount.ProductSaleDiscount(5, 0.75));
        store1.setDiscountPolicy(policy);
        productsAmount = new HashMap<>();
        productsAmount.put(infoBamba, 1);
        assertEquals(5.0, store1.getPrice(u, productsAmount).getPrice());
        policy.clearDiscountTypes();


        policy = new DiscountPolicy("test");
        policy.addDiscountType(new ProductDiscount.ProductSaleDiscount(4, 0.5));
        policy.addDiscountType(new ProductDiscount.ProductSaleDiscount(5, 0.75));
        store1.setDiscountPolicy(policy);

        productsAmount = new HashMap<>();
        productsAmount.put(infoBamba, 5);
        productsAmount.put(infoApple, 10);

        assertEquals((1-0.5) * 5 * 10 + (1-0.75) * 10 * 20, store1.getPrice(u, productsAmount).getPrice());
        policy.clearDiscountTypes();


        policy = new DiscountPolicy("test");
        policy.addDiscountType(new ProductDiscount.CategorySaleDiscount("fruits", 0.75));

        store1.setDiscountPolicy(policy);

        productsAmount = new HashMap<>();
        productsAmount.put(infoApple, 10);

        assertEquals((1-0.75)*10*20, store1.getPrice(u, productsAmount).getPrice());
        policy.clearDiscountTypes();


        policy = new DiscountPolicy("test");
        policy.addDiscountType(new ProductDiscount.CategorySaleDiscount("fruits", 0.75));

        store1.setDiscountPolicy(policy);

        productsAmount = new HashMap<>();
        productsAmount.put(infoBamba, 5);
        productsAmount.put(infoApple, 10);
        assertEquals(5*10 + (1-0.75)*10*20, store1.getPrice(u, productsAmount).getPrice());
        policy.clearDiscountTypes();


        policy = new DiscountPolicy("test");
        policy.addDiscountType(new ProductDiscount.ProductSaleDiscount(4, 0.5));
        policy.addDiscountType(new ProductDiscount.ProductSaleDiscount(5, 0.75));
        policy.addDiscountType(new ProductDiscount.CategorySaleDiscount("fruits", 0.5));

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
    public void testDiscountPoliciesComplex() {
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


        policy.addDiscountType(new AdvancedDiscount.LogicalDiscount(discounts, AdvancedDiscount.LogicalOperation.AND));
        store1.setDiscountPolicy(policy);
        Map<ProductInfo, Integer> productsAmount = new HashMap<>();
        productsAmount.put(infoBamba, 5);
        productsAmount.put(infoApple, 10);


        assertEquals((1-0.5) * 5 * 10 + (1-0.75) * 10 * 20, store1.getPrice(u, productsAmount).getPrice());
        policy.clearDiscountTypes();


        discounts = new ArrayList<>();
        discounts.add(new ProductDiscount.CategorySaleDiscount("fruits", 0.75));
        discounts.add(new ProductDiscount.ProductSaleDiscount(4, 0.5));

        policy = new DiscountPolicy("test");

        AdvancedDiscount.LogicalDiscount hi = new AdvancedDiscount.LogicalDiscount(discounts, AdvancedDiscount.LogicalOperation.OR);

        policy.addDiscountType(new AdvancedDiscount.LogicalDiscount(discounts, AdvancedDiscount.LogicalOperation.OR));

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
        policy.addDiscountType(new AdvancedDiscount.LogicalDiscount(discounts, AdvancedDiscount.LogicalOperation.XOR));
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
    public void testAddProductUpdateNotification(){
        int counter;

        int openerSessionId = test.startSession().getId();
        int subId = test.register(openerSessionId,"Amir","1234").getId();
        test.login(openerSessionId,"Amir","1234");
        int storeid = test.openStore(openerSessionId).getId();
        test.addProductInfo(4, "bamba", "snacks", 10);
        test.addProductInfo(5, "apple", "fruits", 10);
        ProductInfo infoBamba = test.getProductInfoById(4);
        ProductInfo infoApple = test.getProductInfoById(5);

        test.addProductToStore(openerSessionId,storeid,4,5);
        Queue<Notification> noties = test.getUserHandler().getSubscriber(subId).getAllNotification();
        assertEquals(1,noties.size());

    }

    /**
     * Simple owner deletion.
     */
    @Test
    public void testDeleteOwnerSingleManager(){
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
    public void testDeleteOwnerMultyManager(){
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
    public void testDeleteOwnerNotExisting(){
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
    public void testDeleteOwnerNotification(){
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
    public void testDeleteOwnerNotGrantedBy(){
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


    @Test
    public void testSetupConfigFileOpenStore(){
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

    /*
    a. Register users u1,u2,u3,u4,u5,u6.
    b. Make u1 admin.
    c. u2 open store s1.
    d. u2 add item “diapers” to store s1 with cost 30 and quantity 20.
    e. u2 appoint u3 to a store manager with permission to manage inventory.
    f. u3 appoint u5 and u5 appoint u6 to a store manager.
     */
    @Test
    public void testSetupRequirmentsFile(){
        try {
            FileWriter file = new FileWriter("initFile.txt",false);
            file.write("register(u1);\nregister(u2);\nregister(u3);\nregister(u4);\nregister(u5);\nregister(u6);\n"+
                    "set-admin(u1);\nopen-store(u2,s1);\nadd-product(u2,s1,diapers,20,30);\n"+
                    "appoint-manager(u2,s1,u3,manage-inventory);\nappoint-manager(u3,s1,u5,manager);\nappoint-manager(u5,s1,u6,manager);\n");
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        test.setup("123","123","initFile.txt");
        assertEquals(4,test.getStoreById(test.getStoreByName("s1")).getAllManagers().size());
    }
}