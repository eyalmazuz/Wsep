package DALTests;

import DataAccess.DAOManager;
import Domain.TradingSystem.*;
import Domain.TradingSystem.System;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


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
    public void testSimpleBuyingPolicyPersistenceBasket1() {
        int storeId = test.addStore();
        BuyingPolicy policy = test.getStoreById(storeId).getBuyingPolicy();
        test.addProductInfo(1, "what", "whatcategory", 50);
        ProductInfo info = test.getProductInfoById(1);
        int buyingTypeId = policy.addBuyingType(new BasketBuyingConstraint.MinAmountForProductConstraint(info, 5));

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
        int buyingTypeId = policy.addBuyingType(new BasketBuyingConstraint.MaxAmountForProductConstraint(info, 5));

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
        int buyingTypeId = policy.addBuyingType(new BasketBuyingConstraint.MinProductAmountConstraint(5));

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
        int buyingTypeId = policy.addBuyingType(new BasketBuyingConstraint.MaxProductAmountConstraint(5));

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
        int buyingTypeId = policy.addBuyingType(new UserBuyingConstraint.NotOutsideCountryConstraint("Israel"));

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
        int buyingTypeId = policy.addBuyingType(new SystemBuyingConstraint.NotOnDayConstraint(5));

        BuyingPolicy savedPolicy = DAOManager.loadAllBuyingPolicies().get(0);
        assertEquals(savedPolicy.getBuyingTypes().size(), 1);
        BuyingType constraint = savedPolicy.getBuyingTypes().get(buyingTypeId);
        assertTrue(constraint instanceof SystemBuyingConstraint.NotOnDayConstraint);
        assertEquals(((SystemBuyingConstraint) constraint).getForbiddenDay(), 5);
    }
}
