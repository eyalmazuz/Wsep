package DALTests;

import DataAccess.DAOManager;
import Domain.TradingSystem.*;
import Domain.TradingSystem.System;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class DatabaseTests extends TestCase {
    System test;

    @Before
    public void setUp() {
        test = new System();
        test.clearDatabase();
    }

    @After
    public void tearDown() {
        test.deleteStores();
        test.deleteUsers();
    }

    @Test
    public void testProductInfoPersistence() {
        test.addProductInfo(1, "what", "whatcategory", 40);
        ProductInfo productInfo = DAOManager.loadAllProductInfos().get(0);

        assertTrue(productInfo.getId() == 1 && productInfo.getName().equals("what") && productInfo.getCategory().equals("whatcategory") &&
                productInfo.getDefaultPrice() == 40);

        test.getProductInfoById(1).setRating(5.0);
        productInfo = DAOManager.loadAllProductInfos().get(0);
        assertEquals(productInfo.getRating(), 5.0);
    }

    @Test
    public void testSimpleBuyingPolicyPersistenceBasket1() {
        int storeId = test.addStore();
        BuyingPolicy policy = test.getStoreById(storeId).getBuyingPolicy();
        test.addProductInfo(1, "what", "whatcategory", 50);
        ProductInfo info = test.getProductInfoById(1);
        int buyingTypeId = policy.addSimpleBuyingType(new BasketBuyingConstraint.MinAmountForProductConstraint(info, 5));

        BuyingPolicy savedPolicy = DAOManager.loadAllBuyingPolicies().get(0);
        assertEquals(savedPolicy.getBuyingTypes().size(), 1);
        BuyingType constraint = savedPolicy.getBuyingTypes().get(buyingTypeId);
        assertTrue(constraint instanceof BasketBuyingConstraint.MinAmountForProductConstraint);
        assertTrue(((BasketBuyingConstraint) constraint).getMaxAmount() == -1 && ((BasketBuyingConstraint) constraint).getMinAmount() == 5 &&
                ((BasketBuyingConstraint)constraint).getProductInfo().getId() == 1);
    }

    @Test
    public void testSimpleBuyingPolicyPersistenceBasket2() {
        int storeId = test.addStore();
        BuyingPolicy policy = test.getStoreById(storeId).getBuyingPolicy();
        test.addProductInfo(1, "what", "whatcategory", 50);
        ProductInfo info = test.getProductInfoById(1);
        int buyingTypeId = policy.addSimpleBuyingType(new BasketBuyingConstraint.MaxAmountForProductConstraint(info, 5));

        BuyingPolicy savedPolicy = DAOManager.loadAllBuyingPolicies().get(0);
        assertEquals(savedPolicy.getBuyingTypes().size(), 1);
        BuyingType constraint = savedPolicy.getBuyingTypes().get(buyingTypeId);
        assertTrue(constraint instanceof BasketBuyingConstraint.MaxAmountForProductConstraint);
        assertTrue(((BasketBuyingConstraint) constraint).getMinAmount() == -1 && ((BasketBuyingConstraint) constraint).getMaxAmount() == 5 &&
                ((BasketBuyingConstraint)constraint).getProductInfo().getId() == 1);
    }

    @Test
    public void testSimpleBuyingPolicyPersistenceBasket3() {
        int storeId = test.addStore();
        BuyingPolicy policy = test.getStoreById(storeId).getBuyingPolicy();
        int buyingTypeId = policy.addSimpleBuyingType(new BasketBuyingConstraint.MinProductAmountConstraint(5));

        BuyingPolicy savedPolicy = DAOManager.loadAllBuyingPolicies().get(0);
        assertEquals(savedPolicy.getBuyingTypes().size(), 1);
        BuyingType constraint = savedPolicy.getBuyingTypes().get(buyingTypeId);
        assertTrue(constraint instanceof BasketBuyingConstraint.MinProductAmountConstraint);
        assertTrue(((BasketBuyingConstraint) constraint).getMaxAmount() == -1 && ((BasketBuyingConstraint) constraint).getMinAmount() == 5 &&
                ((BasketBuyingConstraint)constraint).getProductInfo() == null);
    }

    @Test
    public void testSimpleBuyingPolicyPersistenceBasket4() {
        int storeId = test.addStore();
        BuyingPolicy policy = test.getStoreById(storeId).getBuyingPolicy();
        int buyingTypeId = policy.addSimpleBuyingType(new BasketBuyingConstraint.MaxProductAmountConstraint(5));

        BuyingPolicy savedPolicy = DAOManager.loadAllBuyingPolicies().get(0);
        assertEquals(savedPolicy.getBuyingTypes().size(), 1);
        BuyingType constraint = savedPolicy.getBuyingTypes().get(buyingTypeId);
        assertTrue(constraint instanceof BasketBuyingConstraint.MaxProductAmountConstraint);
        assertTrue(((BasketBuyingConstraint) constraint).getMinAmount() == -1 && ((BasketBuyingConstraint) constraint).getMaxAmount() == 5 &&
                ((BasketBuyingConstraint)constraint).getProductInfo() == null);
    }

    @Test
    public void testSimpleBuyingPolicyPersistenceUser() {
        int storeId = test.addStore();
        BuyingPolicy policy = test.getStoreById(storeId).getBuyingPolicy();
        int buyingTypeId = policy.addSimpleBuyingType(new UserBuyingConstraint.NotOutsideCountryConstraint("Israel"));

        BuyingPolicy savedPolicy = DAOManager.loadAllBuyingPolicies().get(0);
        assertEquals(savedPolicy.getBuyingTypes().size(), 1);
        BuyingType constraint = savedPolicy.getBuyingTypes().get(buyingTypeId);
        assertTrue(constraint instanceof UserBuyingConstraint.NotOutsideCountryConstraint);
        assertEquals(((UserBuyingConstraint) constraint).getValidCountry(), "Israel");
    }

    @Test
    public void testSimpleBuyingPolicyPersistenceSystem() {
        int storeId = test.addStore();
        BuyingPolicy policy = test.getStoreById(storeId).getBuyingPolicy();
        int buyingTypeId = policy.addSimpleBuyingType(new SystemBuyingConstraint.NotOnDayConstraint(5));

        BuyingPolicy savedPolicy = DAOManager.loadAllBuyingPolicies().get(0);
        assertEquals(savedPolicy.getBuyingTypes().size(), 1);
        BuyingType constraint = savedPolicy.getBuyingTypes().get(buyingTypeId);
        assertTrue(constraint instanceof SystemBuyingConstraint.NotOnDayConstraint);
        assertEquals(((SystemBuyingConstraint) constraint).getForbiddenDay(), 5);
    }

    @Test
    public void testAdvancedBuyingPolicyPersistence1() {
        int storeId = test.addStore();
        BuyingPolicy policy = test.getStoreById(storeId).getBuyingPolicy();
        test.addProductInfo(1, "lambda", "snacks", 50);
        ProductInfo info = test.getProductInfoById(1);

        List<BuyingType> buyingConstraints = new ArrayList<>();
        buyingConstraints.add(new BasketBuyingConstraint.MinAmountForProductConstraint(info, 5));
        buyingConstraints.add(new BasketBuyingConstraint.MaxAmountForProductConstraint(info, 10));
        int buyingTypeId = policy.addAdvancedBuyingType(new AdvancedBuying.LogicalBuying(buyingConstraints, AdvancedBuying.LogicalOperation.AND), false);

        BuyingPolicy savedPolicy = DAOManager.loadAllBuyingPolicies().get(0);
        assertEquals(savedPolicy.getBuyingTypes().size(), 1);
        AdvancedBuying advancedType = (AdvancedBuying) savedPolicy.getBuyingTypes().get(buyingTypeId);
        assertSame(advancedType.getLogicalOperation(), AdvancedBuying.LogicalOperation.AND);
        List<BuyingType> savedConstraints = advancedType.getBuyingConstraints();
        assertTrue(savedConstraints.size() == 2 && savedConstraints.get(0).getClass() == BasketBuyingConstraint.MinAmountForProductConstraint.class &&
                ((BasketBuyingConstraint) savedConstraints.get(0)).getMaxAmount() == -1 && ((BasketBuyingConstraint) savedConstraints.get(0)).getMinAmount() == 5 &&
                ((BasketBuyingConstraint) savedConstraints.get(0)).getProductInfo().getId() == 1 && savedConstraints.get(1).getClass() == BasketBuyingConstraint.MaxAmountForProductConstraint.class &&
                ((BasketBuyingConstraint) savedConstraints.get(1)).getProductInfo().getId() == 1 && ((BasketBuyingConstraint) savedConstraints.get(1)).getMinAmount() == -1 &&
                ((BasketBuyingConstraint) savedConstraints.get(1)).getMaxAmount() == 10);
    }

    @Test
    public void testAdvancedBuyingPolicyPersistence2() {
        int storeId = test.addStore();
        BuyingPolicy policy = test.getStoreById(storeId).getBuyingPolicy();
        test.addProductInfo(1, "lambda", "snacks", 50);
        ProductInfo info = test.getProductInfoById(1);

        List<Integer> buyingConstraintIds = new ArrayList<>();
        buyingConstraintIds.add(policy.addSimpleBuyingType(new BasketBuyingConstraint.MinAmountForProductConstraint(info, 5)));
        buyingConstraintIds.add(policy.addSimpleBuyingType(new BasketBuyingConstraint.MaxAmountForProductConstraint(info, 10)));
        int buyingTypeId = policy.createAdvancedBuyingTypeFromExisting(buyingConstraintIds, "implies").getId();

        BuyingPolicy savedPolicy = DAOManager.loadAllBuyingPolicies().get(0);
        assertEquals(savedPolicy.getBuyingTypes().size(), 1);
        AdvancedBuying advancedType = (AdvancedBuying) savedPolicy.getBuyingTypes().get(buyingTypeId);
        assertTrue(advancedType.getLogicalOperation() == AdvancedBuying.LogicalOperation.IMPLIES);
        List<BuyingType> savedConstraints = advancedType.getBuyingConstraints();
        assertTrue(savedConstraints.size() == 2 && savedConstraints.get(0).getClass() == BasketBuyingConstraint.MinAmountForProductConstraint.class &&
                ((BasketBuyingConstraint) savedConstraints.get(0)).getMaxAmount() == -1 && ((BasketBuyingConstraint) savedConstraints.get(0)).getMinAmount() == 5 &&
                ((BasketBuyingConstraint) savedConstraints.get(0)).getProductInfo().getId() == 1 && savedConstraints.get(1).getClass() == BasketBuyingConstraint.MaxAmountForProductConstraint.class &&
                ((BasketBuyingConstraint) savedConstraints.get(1)).getProductInfo().getId() == 1 && ((BasketBuyingConstraint) savedConstraints.get(1)).getMinAmount() == -1 &&
                ((BasketBuyingConstraint) savedConstraints.get(1)).getMaxAmount() == 10);
    }

    @Test
    public void testAdvancedBuyingPolicyPersistence3() {
        int storeId = test.addStore();
        BuyingPolicy policy = test.getStoreById(storeId).getBuyingPolicy();
        test.addProductInfo(1, "lambda", "snacks", 50);
        ProductInfo info = test.getProductInfoById(1);

        List<BuyingType> buyingConstraints = new ArrayList<>();
        buyingConstraints.add(new BasketBuyingConstraint.MinAmountForProductConstraint(info, 5));
        buyingConstraints.add(new BasketBuyingConstraint.MaxAmountForProductConstraint(info, 10));
        BuyingType innerAdvanced = new AdvancedBuying.LogicalBuying(buyingConstraints, AdvancedBuying.LogicalOperation.AND);

        List<BuyingType> otherBuyingConstraints = new ArrayList<>();
        otherBuyingConstraints.add(new UserBuyingConstraint.NotOutsideCountryConstraint("Israel"));
        otherBuyingConstraints.add(innerAdvanced);
        int buyingTypeId = policy.addAdvancedBuyingType(new AdvancedBuying.LogicalBuying(otherBuyingConstraints, AdvancedBuying.LogicalOperation.IMPLIES), false);

        BuyingPolicy savedPolicy = DAOManager.loadAllBuyingPolicies().get(0);
        assertEquals(savedPolicy.getBuyingTypes().size(), 1);
        AdvancedBuying advancedType = (AdvancedBuying) savedPolicy.getBuyingTypes().get(buyingTypeId);
        assertTrue(advancedType.getLogicalOperation() == AdvancedBuying.LogicalOperation.IMPLIES);
        List<BuyingType> savedConstraints = advancedType.getBuyingConstraints();
        assertEquals(savedConstraints.size(), 2);

        // check that first constraint is simple, user country israel
        BuyingType constraint1 = savedConstraints.get(0);
        assertEquals(constraint1.getClass(), UserBuyingConstraint.NotOutsideCountryConstraint.class);
        assertEquals(((UserBuyingConstraint) constraint1).getValidCountry(), "Israel");

        // check that the second constraint is advanced with all the simple ones
        BuyingType constraint2 = savedConstraints.get(1);
        assertEquals(constraint2.getClass(), AdvancedBuying.LogicalBuying.class);
        assertEquals(((AdvancedBuying) constraint2).getLogicalOperation(), AdvancedBuying.LogicalOperation.AND);
        List<BuyingType> subConstraints = ((AdvancedBuying) savedConstraints.get(1)).getBuyingConstraints();

        assertTrue(subConstraints.size() == 2 && subConstraints.get(0).getClass() == BasketBuyingConstraint.MinAmountForProductConstraint.class &&
                ((BasketBuyingConstraint) subConstraints.get(0)).getMaxAmount() == -1 && ((BasketBuyingConstraint) subConstraints.get(0)).getMinAmount() == 5 &&
                ((BasketBuyingConstraint) subConstraints.get(0)).getProductInfo().getId() == 1 && subConstraints.get(1).getClass() == BasketBuyingConstraint.MaxAmountForProductConstraint.class &&
                ((BasketBuyingConstraint) subConstraints.get(1)).getProductInfo().getId() == 1 && ((BasketBuyingConstraint) subConstraints.get(1)).getMinAmount() == -1 &&
                ((BasketBuyingConstraint) subConstraints.get(1)).getMaxAmount() == 10);
    }

    @Test
    public void testAdvancedBuyingPolicyPersistence4() {
        int storeId = test.addStore();
        BuyingPolicy policy = test.getStoreById(storeId).getBuyingPolicy();
        test.addProductInfo(1, "lambda", "snacks", 50);
        ProductInfo info = test.getProductInfoById(1);

        List<Integer> buyingConstraints = new ArrayList<>();
        buyingConstraints.add(policy.addSimpleBuyingType(new BasketBuyingConstraint.MinAmountForProductConstraint(info, 5)));
        buyingConstraints.add(policy.addSimpleBuyingType(new BasketBuyingConstraint.MaxAmountForProductConstraint(info, 10)));
        int innerId = policy.createAdvancedBuyingTypeFromExisting(buyingConstraints, "and").getId();

        List<Integer> otherBuyingConstraints = new ArrayList<>();
        otherBuyingConstraints.add(policy.addSimpleBuyingType(new UserBuyingConstraint.NotOutsideCountryConstraint("Israel")));
        otherBuyingConstraints.add(innerId);
        int buyingTypeId = policy.createAdvancedBuyingTypeFromExisting(otherBuyingConstraints, "implies").getId();

        BuyingPolicy savedPolicy = DAOManager.loadAllBuyingPolicies().get(0);
        assertEquals(savedPolicy.getBuyingTypes().size(), 1);
        AdvancedBuying advancedType = (AdvancedBuying) savedPolicy.getBuyingTypes().get(buyingTypeId);
        assertTrue(advancedType.getLogicalOperation() == AdvancedBuying.LogicalOperation.IMPLIES);
        List<BuyingType> savedConstraints = advancedType.getBuyingConstraints();
        assertEquals(savedConstraints.size(), 2);

        // check that first constraint is simple, user country israel
        BuyingType constraint1 = savedConstraints.get(0);
        assertEquals(constraint1.getClass(), UserBuyingConstraint.NotOutsideCountryConstraint.class);
        assertEquals(((UserBuyingConstraint) constraint1).getValidCountry(), "Israel");

        // check that the second constraint is advanced with all the simple ones
        BuyingType constraint2 = savedConstraints.get(1);
        assertEquals(constraint2.getClass(), AdvancedBuying.LogicalBuying.class);
        assertEquals(((AdvancedBuying) constraint2).getLogicalOperation(), AdvancedBuying.LogicalOperation.AND);
        List<BuyingType> subConstraints = ((AdvancedBuying) savedConstraints.get(1)).getBuyingConstraints();

        java.lang.System.out.println(subConstraints);

        assertTrue(subConstraints.size() == 2 && subConstraints.get(0).getClass() == BasketBuyingConstraint.MinAmountForProductConstraint.class &&
                ((BasketBuyingConstraint) subConstraints.get(0)).getMaxAmount() == -1 && ((BasketBuyingConstraint) subConstraints.get(0)).getMinAmount() == 5 &&
                ((BasketBuyingConstraint) subConstraints.get(0)).getProductInfo().getId() == 1 && subConstraints.get(1).getClass() == BasketBuyingConstraint.MaxAmountForProductConstraint.class &&
                ((BasketBuyingConstraint) subConstraints.get(1)).getProductInfo().getId() == 1 && ((BasketBuyingConstraint) subConstraints.get(1)).getMinAmount() == -1 &&
                ((BasketBuyingConstraint) subConstraints.get(1)).getMaxAmount() == 10);
    }

    @Test
    public void testStoreProductPersistence1() {
        int storeId = test.addStore();
        test.addProductInfo(1, "lambda", "snacks", 50);
        ProductInfo info = test.getProductInfoById(1);
        Store store = test.getStoreById(storeId);
        store.addProduct(info, 5);

        Store savedStore = DAOManager.loadAllStores().get(0);
        assertEquals(savedStore.getProductAmount(info.getId()), 5);
    }

    @Test
    public void testStoreProductPersistence2() {
        int storeId = test.addStore();
        test.addProductInfo(1, "lambda", "snacks", 50);
        ProductInfo info = test.getProductInfoById(1);
        Store store = test.getStoreById(storeId);
        store.addProduct(info, 5);
        store.removeProductAmount(info.getId(), 4);

        Store savedStore = DAOManager.loadAllStores().get(0);
        assertEquals(savedStore.getProductAmount(info.getId()), 1);
    }

    @Test
    public void testStoreProductPersistence3() {
        int storeId = test.addStore();
        test.addProductInfo(1, "lambda", "snacks", 50);
        ProductInfo info = test.getProductInfoById(1);
        Store store = test.getStoreById(storeId);
        store.addProduct(info, 5);
        store.addProduct(info, 7);

        Store savedStore = DAOManager.loadAllStores().get(0);
        assertEquals(savedStore.getProductAmount(info.getId()), 12);
    }

    @Test
    public void testStoreProductPersistence4() {
        int storeId = test.addStore();
        test.addProductInfo(1, "lambda", "snacks", 50);
        ProductInfo info = test.getProductInfoById(1);
        Store store = test.getStoreById(storeId);
        store.addProduct(info, 5);
        store.removeProductAmount(info.getId(), 5);


        Store savedStore = DAOManager.loadAllStores().get(0);
        assertTrue(savedStore.getProducts().isEmpty());
    }

    @Test
    public void testStoreProductPersistence5() {
        int storeId = test.addStore();
        test.addProductInfo(1, "lambda", "snacks", 50);
        ProductInfo info = test.getProductInfoById(1);
        Store store = test.getStoreById(storeId);
        store.addProduct(info, 5);
        store.setProductPrice(info.getId(), 123);

        Store savedStore = DAOManager.loadAllStores().get(0);
        assertEquals(savedStore.getProductPrice(info.getId()), 123.0);
    }

    @Test
    public void testStoreProductPersistence6() {
        int storeId = test.addStore();
        test.addProductInfo(1, "lambda", "snacks", 50);
        ProductInfo info = test.getProductInfoById(1);
        Store store = test.getStoreById(storeId);
        store.addProduct(info, 5);
        store.editProduct(info.getId(), "this is a great product :)");

        Store savedStore = DAOManager.loadAllStores().get(0);
        assertEquals(savedStore.getProductInStoreInfo(info.getId()), "this is a great product :)");
    }

    @Test
    public void testStoreProductPersistence7() {
        int storeId = test.addStore();
        test.addProductInfo(1, "lambda", "snacks", 50);
        ProductInfo info = test.getProductInfoById(1);
        Store store = test.getStoreById(storeId);
        store.addProduct(info, 5);
        store.deleteProduct(info.getId());

        Store savedStore = DAOManager.loadAllStores().get(0);
        assertTrue(savedStore.getProducts().isEmpty());
    }

    @Test
    public void testUserCartNoPersistence() {
        int storeId = test.addStore();
        test.addProductInfo(1, "lambda", "snacks", 50);
        int sessionId = test.startSession().getId();
        test.getUser(sessionId).addProductToCart(test.getStoreById(storeId), test.getProductInfoById(1), 40);

        // check that there are no carts and no baskets
        assertTrue(DAOManager.loadAllShoppingCarts().isEmpty());
        assertTrue(DAOManager.loadAllShoppingBaskets().isEmpty());
    }

    @Test
    public void testUserCartStateChangePersistence() {
        int storeId = test.addStore();
        test.addProductInfo(1, "lambda", "snacks", 50);
        int sessionId = test.startSession().getId();
        test.getUser(sessionId).addProductToCart(test.getStoreById(storeId), test.getProductInfoById(1), 40);

        test.register(sessionId, "user", "passw0rd");

        ShoppingCart cart = DAOManager.loadAllShoppingCarts().get(0);
        ShoppingBasket basket = cart.getBaskets().get(0);
        assertEquals((int) basket.getProducts().get(test.getProductInfoById(1)), 40);

        // see that we're no longer persisting in guest mode
        test.logout(sessionId);
        test.getUser(sessionId).addProductToCart(test.getStoreById(storeId), test.getProductInfoById(1), 5);
        cart = DAOManager.loadAllShoppingCarts().get(0);
        basket = cart.getBaskets().get(0);
        assertEquals((int) basket.getProducts().get(test.getProductInfoById(1)), 40);

        // make sure its still there
        test.login(sessionId, "user", "passw0rd");
        cart = test.getUser(sessionId).getShoppingCart();
        basket = cart.getBaskets().get(0);
        assertEquals((int) basket.getProducts().get(test.getProductInfoById(1)), 45);
    }

    @Test
    public void testUserCartModificationPersistence1() {
        int storeId = test.addStore();
        test.addProductInfo(1, "lambda", "snacks", 50);
        int sessionId = test.startSession().getId();

        test.getUser(sessionId).setState(new Subscriber());

        test.getUser(sessionId).addProductToCart(test.getStoreById(storeId), test.getProductInfoById(1), 30);
        ShoppingCart cart = DAOManager.loadAllShoppingCarts().get(0);
        ShoppingBasket basket = cart.getBaskets().get(0);
        assertEquals((int)basket.getProducts().get(test.getProductInfoById(1)), 30);

        test.getUser(sessionId).addProductToCart(test.getStoreById(storeId), test.getProductInfoById(1), 20);
        cart = DAOManager.loadAllShoppingCarts().get(0);
        basket = cart.getBaskets().get(0);
        assertEquals((int)basket.getProducts().get(test.getProductInfoById(1)), 50);
    }

    @Test
    public void testUserCartModificationPersistence2() {
        int storeId = test.addStore();
        test.addProductInfo(1, "lambda", "snacks", 50);
        int sessionId = test.startSession().getId();

        test.getUser(sessionId).setState(new Subscriber());

        test.getUser(sessionId).addProductToCart(test.getStoreById(storeId), test.getProductInfoById(1), 30);
        ShoppingCart cart = DAOManager.loadAllShoppingCarts().get(0);
        ShoppingBasket basket = cart.getBaskets().get(0);
        assertEquals((int)basket.getProducts().get(test.getProductInfoById(1)), 30);

        test.getUser(sessionId).editCartProductAmount(test.getStoreById(storeId), test.getProductInfoById(1), 3);
        cart = DAOManager.loadAllShoppingCarts().get(0);
        basket = cart.getBaskets().get(0);
        assertEquals((int)basket.getProducts().get(test.getProductInfoById(1)), 3);
    }

    @Test
    public void testUserCartModificationPersistence3() {
        int storeId = test.addStore();
        test.addProductInfo(1, "lambda", "snacks", 50);
        int sessionId = test.startSession().getId();

        test.getUser(sessionId).setState(new Subscriber());

        test.getUser(sessionId).addProductToCart(test.getStoreById(storeId), test.getProductInfoById(1), 30);
        ShoppingCart cart = DAOManager.loadAllShoppingCarts().get(0);
        ShoppingBasket basket = cart.getBaskets().get(0);
        assertEquals((int)basket.getProducts().get(test.getProductInfoById(1)), 30);

        test.getUser(sessionId).removeProductFromCart(test.getStoreById(storeId), test.getProductInfoById(1));
        cart = DAOManager.loadAllShoppingCarts().get(0);
        basket = cart.getBaskets().get(0);
        assertTrue(basket.getProducts().isEmpty());
    }

    @Test
    public void testUserPurchaseHistoryPersistence() {
        int storeId = test.addStore();
        test.addProductInfo(1, "lambda", "snacks", 30);
        test.getStoreById(storeId).addProduct(test.getProductInfoById(1), 40);
        int sessionId = test.startSession().getId();

        test.register(sessionId, "user", "passw0rd");
        test.login(sessionId, "user", "passw0rd");
        test.getUser(sessionId).addProductToCart(test.getStoreById(storeId), test.getProductInfoById(1), 10);
        test.savePurchaseHistory(sessionId);

        Subscriber subscriber = DAOManager.loadAllSubscribers().get(0);
        Map<Store, List<PurchaseDetails>> history = subscriber.getStorePurchaseLists();
        assertEquals(history.keySet().iterator().next(), test.getStoreById(storeId));
        assertEquals(history.get(test.getStoreById(storeId)).size(), 1);
        PurchaseDetails details = history.get(test.getStoreById(storeId)).get(0);
        assertEquals(details.getProducts().size(), 1);
        assertEquals((int)details.getProducts().get(test.getProductInfoById(1)), 10);
        assertEquals(details.getStore(), test.getStoreById(storeId));
        assertEquals(details.getPrice(), 300.0); // 30 * 10
    }

    @Test
    public void testStorePurchaseHistoryPersistence() {
        int storeId = test.addStore();
        test.addProductInfo(1, "lambda", "snacks", 30);
        test.getStoreById(storeId).addProduct(test.getProductInfoById(1), 40);
        int sessionId = test.startSession().getId();

        test.getUser(sessionId).setState(new Subscriber());
        test.getUser(sessionId).addProductToCart(test.getStoreById(storeId), test.getProductInfoById(1), 10);
        test.savePurchaseHistory(sessionId);

        Store store = DAOManager.loadAllStores().get(0);
        PurchaseDetails details = store.getStorePurchaseHistory().get(0);
        assertEquals(details.getProducts().size(), 1);
        assertEquals((int)details.getProducts().get(test.getProductInfoById(1)), 10);
        assertEquals(details.getStore(), test.getStoreById(storeId));
        assertEquals(details.getPrice(), 300.0); // 30 * 10
    }

    @Test
    public void testStoreBuyingPolicyPersistency() {
        int storeId = test.addStore();
        Store store = test.getStoreById(storeId);
        test.addProductInfo(1, "what", "whatcategory", 50);
        BuyingPolicy buyingPolicy = new BuyingPolicy("None");
        buyingPolicy.addSimpleBuyingType(new BasketBuyingConstraint.MinAmountForProductConstraint(test.getProductInfoById(1), 5));
        store.setBuyingPolicy(buyingPolicy);

        Store savedStore = DAOManager.loadAllStores().get(0);
        BuyingPolicy savedPolicy = savedStore.getBuyingPolicy();
        assertEquals(savedPolicy.getBuyingTypes().size(), 1);
        assertEquals(savedPolicy.getBuyingTypes().values().iterator().next().toString(), "Cannot purchase less than 5 of what (1)");
    }

    @Test
    public void testSimpleDiscountPolicyPersistence1() {
        int storeId = test.addStore();
        Store store = test.getStoreById(storeId);
        test.addProductInfo(1, "what", "whatcategory", 50);
        DiscountPolicy discountPolicy = new DiscountPolicy("None");
        discountPolicy.addSimpleDiscountType(new ProductDiscount.ProductSaleDiscount(1, 0.3));
        store.setDiscountPolicy(discountPolicy);

        Store savedStore = DAOManager.loadAllStores().get(0);
        DiscountPolicy savedPolicy = savedStore.getDiscountPolicy();
        assertEquals(savedPolicy.getDiscountTypes().values().iterator().next().toString(), "30.0% sale on product ID 1");
    }


    @Test
    public void testSimpleDiscountPolicyPersistence2() {
        int storeId = test.addStore();
        Store store = test.getStoreById(storeId);
        test.addProductInfo(1, "what", "whatcategory", 50);
        DiscountPolicy discountPolicy = new DiscountPolicy("None");
        discountPolicy.addSimpleDiscountType(new ProductDiscount.CategorySaleDiscount("whatcategory", 0.3));
        store.setDiscountPolicy(discountPolicy);

        Store savedStore = DAOManager.loadAllStores().get(0);
        DiscountPolicy savedPolicy = savedStore.getDiscountPolicy();
        assertEquals(savedPolicy.getDiscountTypes().values().iterator().next().toString(), "30.0% sale on the whatcategory category");
    }

    @Test
    public void testAdvancedDiscountPolicyPersistence1() {
        int storeId = test.addStore();
        Store store = test.getStoreById(storeId);
        test.addProductInfo(1, "what", "whatcategory", 50);
        DiscountPolicy discountPolicy = new DiscountPolicy("None");
        List<DiscountType> discounts = new ArrayList<>();
        discounts.add(new ProductDiscount.ProductSaleDiscount(1, 0.3));
        discounts.add(new ProductDiscount.CategorySaleDiscount("whatcategory", 0.4));
        AdvancedDiscount advancedDiscount = new AdvancedDiscount.LogicalDiscount(discounts, AdvancedDiscount.LogicalOperation.OR);
        discountPolicy.addAdvancedDiscountType(advancedDiscount, false);
        store.setDiscountPolicy(discountPolicy);

        Store savedStore = DAOManager.loadAllStores().get(0);
        DiscountPolicy savedPolicy = savedStore.getDiscountPolicy();
        assertEquals(savedPolicy.getDiscountTypes().size(), 1);
        DiscountType savedDiscount = savedPolicy.getDiscountTypes().values().iterator().next();
        assertTrue(savedDiscount instanceof AdvancedDiscount.LogicalDiscount);
        AdvancedDiscount.LogicalDiscount discount = (AdvancedDiscount.LogicalDiscount) savedDiscount;
        assertEquals(discount.toString(), "30.0% sale on product ID 1 OR 40.0% sale on the whatcategory category");
    }

    @Test
    public void testAdvancedDiscountPolicyPersistence2() {
        int storeId = test.addStore();
        Store store = test.getStoreById(storeId);
        test.addProductInfo(1, "what", "whatcategory", 50);
        DiscountPolicy discountPolicy = new DiscountPolicy("None");
        List<DiscountType> discounts = new ArrayList<>();
        discounts.add(new ProductDiscount.ProductSaleDiscount(1, 0.3));

        List<DiscountType> innerDiscounts = new ArrayList<>();
        innerDiscounts.add(new ProductDiscount.ProductSaleDiscount(1, 0.2));
        innerDiscounts.add(new ProductDiscount.CategorySaleDiscount("whatcategory", 0.7));
        AdvancedDiscount innerAdvanced = new AdvancedDiscount.LogicalDiscount(innerDiscounts, AdvancedDiscount.LogicalOperation.XOR);
        discounts.add(innerAdvanced);
        AdvancedDiscount advancedDiscount = new AdvancedDiscount.LogicalDiscount(discounts, AdvancedDiscount.LogicalOperation.OR);

        discountPolicy.addAdvancedDiscountType(advancedDiscount, false);
        store.setDiscountPolicy(discountPolicy);

        Store savedStore = DAOManager.loadAllStores().get(0);
        DiscountPolicy savedPolicy = savedStore.getDiscountPolicy();
        assertEquals(savedPolicy.getDiscountTypes().size(), 1);
        DiscountType savedDiscount = savedPolicy.getDiscountTypes().values().iterator().next();
        assertTrue(savedDiscount instanceof AdvancedDiscount.LogicalDiscount);
        AdvancedDiscount discount = (AdvancedDiscount) savedDiscount;
        assertEquals(discount.toString(), "30.0% sale on product ID 1 OR 20.0% sale on product ID 1 XOR 70.0% sale on the whatcategory category");
        assertEquals(discount.getLogicalOperation(), AdvancedDiscount.LogicalOperation.OR);
        List<DiscountType> savedDiscounts = discount.getDiscounts();
        DiscountType simpleSavedDiscount = savedDiscounts.get(0);
        assertTrue(simpleSavedDiscount instanceof ProductDiscount.ProductSaleDiscount);
        DiscountType advancedSavedDiscount = savedDiscounts.get(1);
        assertTrue(advancedSavedDiscount instanceof AdvancedDiscount.LogicalDiscount);
        assertEquals(((AdvancedDiscount) advancedSavedDiscount).getLogicalOperation(), AdvancedDiscount.LogicalOperation.XOR);
        List<DiscountType> savedInnerSimple = ((AdvancedDiscount) advancedSavedDiscount).getDiscounts();
        DiscountType innerSimple1 = savedInnerSimple.get(0);
        assertTrue(innerSimple1 instanceof ProductDiscount.ProductSaleDiscount);
        DiscountType innerSimple2 = savedInnerSimple.get(1);
        assertTrue(innerSimple2 instanceof ProductDiscount.CategorySaleDiscount);
        assertEquals(innerSimple1.toString(), "20.0% sale on product ID 1");
        assertEquals(innerSimple2.toString(), "70.0% sale on the whatcategory category");
    }

    @Test
    public void testAdvancedDiscountPolicyPersistence3() {
        int storeId = test.addStore();
        Store store = test.getStoreById(storeId);
        test.addProductInfo(1, "what", "whatcategory", 50);
        DiscountPolicy discountPolicy = new DiscountPolicy("None");
        List<Integer> discountTypeIds = new ArrayList<>();
        discountTypeIds.add(discountPolicy.addSimpleDiscountType(new ProductDiscount.ProductSaleDiscount(1, 0.3)));
        discountTypeIds.add(discountPolicy.addSimpleDiscountType(new ProductDiscount.CategorySaleDiscount("whatcategory", 0.4)));
        discountPolicy.createAdvancedDiscountTypeFromExisting(discountTypeIds, "or");
        store.setDiscountPolicy(discountPolicy);

        Store savedStore = DAOManager.loadAllStores().get(0);
        DiscountPolicy savedPolicy = savedStore.getDiscountPolicy();
        assertEquals(savedPolicy.getDiscountTypes().size(), 1);
        DiscountType savedDiscount = savedPolicy.getDiscountTypes().values().iterator().next();
        assertTrue(savedDiscount instanceof AdvancedDiscount.LogicalDiscount);
        AdvancedDiscount.LogicalDiscount discount = (AdvancedDiscount.LogicalDiscount) savedDiscount;
        assertEquals(discount.toString(), "30.0% sale on product ID 1 OR 40.0% sale on the whatcategory category");
    }

    @Test
    public void testAdvancedDiscountPolicyPersistence4() {
        int storeId = test.addStore();
        Store store = test.getStoreById(storeId);
        test.addProductInfo(1, "what", "whatcategory", 50);
        DiscountPolicy discountPolicy = new DiscountPolicy("None");
        List<Integer> discountTypeIds = new ArrayList<>();
        discountTypeIds.add(discountPolicy.addSimpleDiscountType(new ProductDiscount.ProductSaleDiscount(1, 0.3)));

        List<Integer> innerDiscountIds = new ArrayList<>();
        innerDiscountIds.add(discountPolicy.addSimpleDiscountType(new ProductDiscount.ProductSaleDiscount(1, 0.2)));
        innerDiscountIds.add(discountPolicy.addSimpleDiscountType(new ProductDiscount.CategorySaleDiscount("whatcategory", 0.7)));
        discountTypeIds.add(discountPolicy.createAdvancedDiscountTypeFromExisting(innerDiscountIds, "xor").getId());

        discountPolicy.createAdvancedDiscountTypeFromExisting(discountTypeIds, "or");
        store.setDiscountPolicy(discountPolicy);

        Store savedStore = DAOManager.loadAllStores().get(0);
        DiscountPolicy savedPolicy = savedStore.getDiscountPolicy();
        assertEquals(savedPolicy.getDiscountTypes().size(), 1);
        DiscountType savedDiscount = savedPolicy.getDiscountTypes().values().iterator().next();
        assertTrue(savedDiscount instanceof AdvancedDiscount.LogicalDiscount);
        AdvancedDiscount discount = (AdvancedDiscount) savedDiscount;
        assertEquals(discount.toString(), "30.0% sale on product ID 1 OR 20.0% sale on product ID 1 XOR 70.0% sale on the whatcategory category");
        assertEquals(discount.getLogicalOperation(), AdvancedDiscount.LogicalOperation.OR);
        List<DiscountType> savedDiscounts = discount.getDiscounts();
        DiscountType simpleSavedDiscount = savedDiscounts.get(0);
        assertTrue(simpleSavedDiscount instanceof ProductDiscount.ProductSaleDiscount);
        DiscountType advancedSavedDiscount = savedDiscounts.get(1);
        assertTrue(advancedSavedDiscount instanceof AdvancedDiscount.LogicalDiscount);
        assertEquals(((AdvancedDiscount) advancedSavedDiscount).getLogicalOperation(), AdvancedDiscount.LogicalOperation.XOR);
        List<DiscountType> savedInnerSimple = ((AdvancedDiscount) advancedSavedDiscount).getDiscounts();
        DiscountType innerSimple1 = savedInnerSimple.get(0);
        assertTrue(innerSimple1 instanceof ProductDiscount.ProductSaleDiscount);
        DiscountType innerSimple2 = savedInnerSimple.get(1);
        assertTrue(innerSimple2 instanceof ProductDiscount.CategorySaleDiscount);
        assertEquals(innerSimple1.toString(), "20.0% sale on product ID 1");
        assertEquals(innerSimple2.toString(), "70.0% sale on the whatcategory category");
    }

    @Test
    public void testSubscriberPermissionPersistence1() {
        int sessionId = test.startSession().getId();
        User u = test.getUser(sessionId);
        u.setState(new Subscriber());
        int storeId = test.openStore(sessionId).getId();
        Store store = test.getStoreById(storeId);

        int sessionId2 = test.startSession().getId();
        User u2 = test.getUser(sessionId2);
        u2.setState(new Subscriber());
        Subscriber subState = (Subscriber) u2.getState();
        subState.addPermission(store, (Subscriber) u.getState(), "Manager");

        List<Subscriber> subscribers = DAOManager.loadAllSubscribers();
        Subscriber storeOwner = subscribers.get(0);
        Subscriber storeManager = subscribers.get(1);

        Permission ownerPermission = storeOwner.getPermission(storeId);
        assertNotNull(ownerPermission);
        assertEquals(ownerPermission.getType(), "Owner");
        assertNull(ownerPermission.getGrantor());
        assertEquals(ownerPermission.getStore().getId(), storeId);
        assertEquals(ownerPermission.getDetails(), "Simple");

        Permission managerPermission = storeManager.getPermission(storeId);
        assertNotNull(managerPermission);
        assertEquals(managerPermission.getType(), "Manager");
        assertEquals(managerPermission.getGrantor(), u.getState());
        assertEquals(managerPermission.getStore().getId(), storeId);
        assertEquals(managerPermission.getDetails(), "Simple");
    }

    @Test
    public void testSubscriberPermissionPersistence2() {
        int sessionId = test.startSession().getId();
        User u = test.getUser(sessionId);
        u.setState(new Subscriber());
        int storeId = test.openStore(sessionId).getId();
        Store store = test.getStoreById(storeId);

        int sessionId2 = test.startSession().getId();
        User u2 = test.getUser(sessionId2);
        u2.setState(new Subscriber());
        Subscriber subState = (Subscriber) u2.getState();
        subState.addPermission(store, (Subscriber) u.getState(), "Manager");
        subState.removePermission(store, "Manager");

        Subscriber storeManager = DAOManager.loadAllSubscribers().get(1);

        Permission managerPermission = storeManager.getPermission(storeId);
        assertNull(managerPermission);
    }
}
