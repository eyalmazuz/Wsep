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



}
