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
}
