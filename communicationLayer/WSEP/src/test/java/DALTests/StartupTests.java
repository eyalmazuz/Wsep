package DALTests;

import DataAccess.DAOManager;
import DataAccess.DatabaseFetchException;
import Domain.TradingSystem.*;
import Domain.TradingSystem.System;
import junit.framework.TestCase;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

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
    public void testProductInfoStartup() throws DatabaseFetchException {
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
    public void testStoreProductsStartup() throws DatabaseFetchException {
        int sessionId = test.startSession().getId();
        test.register(sessionId, "user", "passw0rd");
        test.login(sessionId, "user", "passw0rd");
        int storeId = test.openStore(sessionId).getId();
        Store store = test.getStoreById(storeId);
        store.setName("store name");
        test.addProductInfo(1, "lambda", "snacks", 30);
        test.addProductToStore(sessionId, storeId, 1, 15);

        test = new System();

        assertEquals(test.getStores().size(), 1);
        assertNotNull(test.getStoreById(storeId));
        Store savedStore = test.getStoreById(storeId);
        assertEquals(savedStore.getName(), "store name");
        ProductInStore pis = savedStore.getProductInStoreById(1);
        assertEquals(pis.getProductInfoId(), 1);
        assertEquals(pis.getAmount(), 15);
    }


    @Test
    public void testStoreOwnerStartup() throws DatabaseFetchException {
        int sessionId = test.startSession().getId();
        int grantorId = test.register(sessionId, "user", "passw0rd").getId();
        test.login(sessionId, "user", "passw0rd");
        int storeId = test.openStore(sessionId).getId();
        Store store = test.getStoreById(storeId);
        store.setName("store name");

        int sessionId2 = test.startSession().getId();
        int newOwnerId = test.register(sessionId2, "user2", "passw0rd").getId();
        test.login(sessionId2, "user2", "passw0rd");

        test.addStoreOwner(sessionId, storeId, newOwnerId);

        test = new System();

        Store savedStore = test.getStoreById(storeId);

        assertEquals(savedStore.getOwners().size(),2);
        assertEquals(savedStore.getOwners().get(0).getId(), grantorId);
        assertEquals(savedStore.getOwners().get(1).getId(), newOwnerId);
    }


    @Test
    public void testStorePoliciesStartup() throws DatabaseFetchException {
        int sessionId = test.startSession().getId();
        test.register(sessionId, "user", "passw0rd");
        test.login(sessionId, "user", "passw0rd");
        int storeId = test.openStore(sessionId).getId();
        Store store = test.getStoreById(storeId);

        BuyingPolicy buyingPolicy = new BuyingPolicy("yes");
        buyingPolicy.addSimpleBuyingType(new BasketBuyingConstraint.MaxProductAmountConstraint(5));
        store.setBuyingPolicy(buyingPolicy);

        DiscountPolicy discountPolicy = new DiscountPolicy("yes");
        discountPolicy.addSimpleDiscountType(new ProductDiscount.ProductSaleDiscount.CategorySaleDiscount("amazing products", 0.4));
        store.setDiscountPolicy(discountPolicy);

        test = new System();

        Store savedStore = test.getStoreById(storeId);
        BuyingPolicy savedBuyingPolicy = savedStore.getBuyingPolicy();
        assertEquals(savedBuyingPolicy.getBuyingTypes().size(), 1);
        BuyingType savedType = savedBuyingPolicy.getBuyingTypes().values().iterator().next();
        assertTrue(savedType instanceof BasketBuyingConstraint.MaxProductAmountConstraint);
        assertNull(((BasketBuyingConstraint) savedType).getProductInfo());
        assertEquals(((BasketBuyingConstraint) savedType).getMinAmount(), -1);
        assertEquals(((BasketBuyingConstraint) savedType).getMaxAmount(), 5);

        DiscountPolicy savedDiscountPolicy = savedStore.getDiscountPolicy();
        assertEquals(savedDiscountPolicy.getDiscountTypes().size(), 1);
        DiscountType savedDiscountType = savedDiscountPolicy.getDiscountTypes().values().iterator().next();
        assertTrue(savedDiscountType instanceof ProductDiscount.CategorySaleDiscount);
        assertEquals(((ProductDiscount) savedDiscountType).getCategoryName(), "amazing products");
        assertEquals(((ProductDiscount) savedDiscountType).getProductId(), -1);
        assertEquals(((ProductDiscount) savedDiscountType).getSalePercentage(), 0.4);
    }

    @Test
    public void testStorePurchaseHistoryStartup() throws DatabaseFetchException {
        int sessionId = test.startSession().getId();
        test.register(sessionId, "user", "passw0rd");
        test.login(sessionId, "user", "passw0rd");
        int storeId = test.openStore(sessionId).getId();

        test.addProductInfo(1, "lambda", "snacks", 30);
        test.addProductToStore(sessionId, storeId, 1, 5);
        test.addToCart(sessionId, storeId, 1, 3);
        test.saveOngoingPurchaseForUser(sessionId);
        test.savePurchaseHistory(sessionId);

        test = new System();
        Store savedStore = test.getStoreById(storeId);
        assertEquals(savedStore.getStorePurchaseHistory().size(), 1);
        PurchaseDetails details = savedStore.getStorePurchaseHistory().get(0);
        assertEquals(details.getPrice(), 90.0);
        assertEquals((int) details.getProducts().get(test.getProductInfoById(1)), 3);
    }

    @Test
    public void testStoreGrantingAgreementStartup() throws DatabaseFetchException {
        int sessionId = test.startSession().getId();
        int grantorID = test.register(sessionId, "user", "passw0rd").getId();
        test.login(sessionId, "user", "passw0rd");
        int storeId = test.openStore(sessionId).getId();

        int otherSessionId = test.startSession().getId();
        int firstownerId =test.register(otherSessionId, "user2", "passw0rd").getId();
        test.login(otherSessionId, "user2", "passw0rd");

        test.addStoreOwner(sessionId, storeId, firstownerId);

        int secondOwner = otherSessionId;

        otherSessionId = test.startSession().getId();
        int secondOwnerId = test.register(otherSessionId, "user3", "passw0rd").getId();
        test.login(otherSessionId, "user3", "passw0rd");

        test.addStoreOwner(sessionId, storeId, secondOwnerId);

        test = new System();

        assertEquals(test.getStoreById(storeId).getAllGrantingAgreements().size(), 1);
        GrantingAgreement agreement = test.getStoreById(storeId).getAllGrantingAgreements().iterator().next();
        assertEquals(agreement.getStoreId(), storeId);
        assertEquals(agreement.getGrantorId(), grantorID);
        assertEquals(agreement.getMalshabId(), secondOwnerId);
        assertFalse(agreement.getOwner2approve().get(firstownerId));
    }

    @Test
    public void testSubscriberShoppingCartStartup() throws DatabaseFetchException {
        int sessionId = test.startSession().getId();
        test.register(sessionId, "user", "passw0rd");
        test.login(sessionId, "user", "passw0rd");
        int storeId = test.openStore(sessionId).getId();

        test.addProductInfo(1, "lambda", "snacks", 20.0);
        test.addProductToStore(sessionId, storeId, 1, 30);

        test.addToCart(sessionId, storeId, 1, 10);

        test = new System();

        sessionId = test.startSession().getId();
        test.login(sessionId, "user", "passw0rd");

        ShoppingCart savedCart = test.getUser(sessionId).getShoppingCart();
        assertEquals(savedCart.getBaskets().size(), 1);
        ShoppingBasket basket = savedCart.getBaskets().get(0);
        assertEquals((int) basket.getProducts().get(test.getProductInfoById(1)), 10);
    }

    @Test
    public void testSubscriberPermissionsStartup() throws DatabaseFetchException {
        int sessionId = test.startSession().getId();
        test.register(sessionId, "user", "passw0rd");
        test.login(sessionId, "user", "passw0rd");
        int storeId = test.openStore(sessionId).getId();

        int newManagerSessionId = test.startSession().getId();
        int newManagerId = test.register(newManagerSessionId, "user2", "passw0rd").getId();

        test.addStoreManager(sessionId, storeId, newManagerId);

        test = new System();

        Store store = test.getStoreById(storeId);
        List<Subscriber> managers = store.getManagers();

        assertEquals(managers.size(), 1);
        Subscriber savedNewManager = managers.get(0);
        assertEquals(savedNewManager.getUsername(), "user2");

        List<Subscriber> owners = store.getOwners();
        Subscriber savedNewOwner = owners.get(0);
        assertEquals(savedNewOwner.getUsername(), "user");
    }

    @Test
    public void testSubscriberPurchaseHistoryStartup() throws DatabaseFetchException {
        int sessionId = test.startSession().getId();
        test.register(sessionId, "user", "passw0rd");
        test.login(sessionId, "user", "passw0rd");
        int storeId = test.openStore(sessionId).getId();

        test.addProductInfo(1, "lambda", "snacks", 30);
        test.addProductToStore(sessionId, storeId, 1, 5);
        test.addToCart(sessionId, storeId, 1, 3);
        test.saveOngoingPurchaseForUser(sessionId);
        test.savePurchaseHistory(sessionId);

        test = new System();

        sessionId = test.startSession().getId();
        test.register(sessionId, "user", "passw0rd");
        test.login(sessionId, "user", "passw0rd");

        Subscriber subscriber = (Subscriber) test.getUser(sessionId).getState();

        PurchaseDetails details = subscriber.getStorePurchaseLists().get(test.getStoreById(storeId)).get(0);
        assertEquals(details.getPrice(), 90.0);
        assertEquals((int) details.getProducts().get(test.getProductInfoById(1)), 3);
    }


    @Test
    public void testRemovePersistentProductInfo() throws DatabaseFetchException {
        test.addProductInfo(1, "lambda", "snacks", 30.0);

        test = new System();
        test.removeProduct(1);
        assertEquals(test.getProducts().size(), 0);
    }

    @Test
    public void testUpdatePersistentStore() throws DatabaseFetchException {
        int sessionId = test.startSession().getId();
        test.register(sessionId, "user", "passw0rd");
        test.login(sessionId, "user", "passw0rd");
        int storeId = test.openStore(sessionId).getId();
        test.addProductInfo(1, "lambda", "snacks", 30);
        test.addProductToStore(sessionId, storeId, 1, 40);

        test = new System();

        sessionId = test.startSession().getId();
        test.login(sessionId, "user", "passw0rd");
        test.addProductToStore(sessionId, storeId, 1, 10);

        assertEquals(test.getStoreById(storeId).getProducts().get(0).getAmount(), 50);

        test = new System();

        sessionId = test.startSession().getId();
        test.login(sessionId, "user", "passw0rd");
        test.addProductToStore(sessionId, storeId, 1, 10);
        test.addSimpleBuyingTypeBasketConstraint(storeId, 1, "min", 20);

        assertEquals(test.getStoreById(storeId).getProducts().get(0).getAmount(), 60);
        assertEquals(((BasketBuyingConstraint.MinAmountForProductConstraint) test.getStoreById(storeId).getBuyingPolicy().getBuyingTypes().values().iterator().next()).getMinAmount(), 20);

    }

    @Test
    public void testStoreProductInfoStartup() throws DatabaseFetchException {
        int sessionId = test.startSession().getId();
        test.register(sessionId, "user", "passw0rd");
        test.login(sessionId, "user", "passw0rd");
        int storeId = test.openStore(sessionId).getId();
        test.addProductInfo(1, "lambda", "snacks", 30);
        test.addProductToStore(sessionId, storeId, 1, 40);

        test = new System();

        Store store = test.getStoreById(storeId);
        ProductInfo savedProductInfo = store.getProducts().get(0).getProductInfo();
        assertEquals(savedProductInfo.getCategory(), "snacks");
        assertEquals(savedProductInfo.getName(), "lambda");
        assertEquals(savedProductInfo.getDefaultPrice(), 30.0);
    }
}
