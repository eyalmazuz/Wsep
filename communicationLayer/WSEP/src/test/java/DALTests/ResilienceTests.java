package DALTests;

import DataAccess.DAOManager;
import DataAccess.DatabaseFetchException;
import Domain.TradingSystem.*;
import Domain.TradingSystem.System;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ResilienceTests extends TestCase {

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
    public void testOneActionRes() {
        // disconnect
        DAOManager.close();
        test.addProductInfo(1, "what", "whatcategory", 40);

        try {
            DAOManager.loadAllProductInfos().get(0);
            fail();
        } catch (Exception e) {
            assertTrue(true);
        }

        // connect
        DAOManager.open();
        assertFalse(DAOManager.executeTodos()); // it has already run in the "open"

        try {
            DAOManager.loadAllProductInfos().get(0);
            assertTrue(true);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testMultipleActionRes() throws DatabaseFetchException {

        // first we add a product, then a discount policy. We close the connection and check if nothing happened, then open and check all has been done.
        int storeId = test.addStore();
        BuyingPolicy policy = test.getStoreById(storeId).getBuyingPolicy();

        // disconnect
        DAOManager.close();

        test.addProductInfo(1, "what", "whatcategory", 50);
        ProductInfo info = test.getProductInfoById(1);

        int buyingTypeId = policy.addSimpleBuyingType(new BasketBuyingConstraint.MinAmountForProductConstraint(info, 5));

        try {
            DAOManager.loadAllProductInfos().get(0);
            fail();
        } catch (Exception e) {
            assertTrue(true);
        }

        try {
            DAOManager.loadAllBuyingPolicies().get(0);
            fail();
        } catch (Exception e) {
            assertTrue(true);
        }

        // connect
        DAOManager.open();
        assertFalse(DAOManager.executeTodos()); // it has already run in the "open"


        BuyingPolicy savedPolicy = null;
        try {
            savedPolicy = DAOManager.loadAllBuyingPolicies().get(0);
            assertTrue(true);
        } catch (Exception e) {
            fail();
        }

        assertEquals(savedPolicy.getBuyingTypes().size(), 1);

        BuyingType constraint = savedPolicy.getBuyingTypes().get(buyingTypeId);
        assertTrue(constraint instanceof BasketBuyingConstraint.MinAmountForProductConstraint);
        assertTrue(((BasketBuyingConstraint) constraint).getMaxAmount() == -1 && ((BasketBuyingConstraint) constraint).getMinAmount() == 5 &&
                ((BasketBuyingConstraint)constraint).getProductInfo().getId() == 1);

        try {
            DAOManager.loadAllProductInfos().get(0);
            assertTrue(true);
        } catch (Exception e) {
            fail();
        }

    }


}
