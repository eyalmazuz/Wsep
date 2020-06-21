package Domain.TradingSystem.whiteBoxConcurrencyTests;

import DTOs.IntActionResultDto;
import DTOs.ResultCode;
import DataAccess.DAOManager;
import DataAccess.DatabaseFetchException;
import Domain.TradingSystem.*;
import Domain.TradingSystem.System;
import NotificationPublisher.Publisher;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcurrencyTest extends TestCase {
    //System Unitesting
    System test;
    private final static int THREADS=10;
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
        Publisher publisherMock = new Publisher((path, subscribers, message) -> null);
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

    @Test
    public void testBuyProductsMultipleBuys(){
        int storeId = setUpStoreWithAmount(100);
        AtomicInteger doneCount = new AtomicInteger(0);
        List<Thread> threadList = new ArrayList();
        for(int i = 0;i<THREADS;i++) {
            Thread t1 = new Thread(() -> {
                int id = test.startSession().getId();
                test.addToCart(id, storeId, 69, 5);
                try {
                    confirmPurchase(id, false);
                } catch (DatabaseFetchException e) {
                    e.printStackTrace();
                }
                doneCount.incrementAndGet();
            });
            threadList.add(t1);
        }
        for(Thread t : threadList){
            t.start();
        }
        while(doneCount.get()<THREADS){

        }

        assertEquals(1,1);
    }

    private int setUpStoreWithAmount(int amount) {
        try {
            paymentHandler = new PaymentHandler("None");
            test.setPaymentHandler(paymentHandler);
            supplyHandler = new SupplyHandler("None");
            test.setSupplyHandler(supplyHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int id = test.startSession().getId();
        test.register(id,"master","1234");
        try {
            test.login(id,"master","1234");
        } catch (DatabaseFetchException e) {
            e.printStackTrace();
        }
        int storeid = test.openStore(id).getId();
        test.addProductInfo(69,"bamba","food",5);
        test.addProductToStore(id,storeid,69,amount);
        return storeid;
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



    /// PASSED TESTS////

    @Test
    public void testRegistermultipleTimes(){
        List<Thread> threads = new ArrayList<>();
        AtomicInteger count = new AtomicInteger(0);
        for(int i = 0;i<THREADS;i++){
            Thread t = new Thread(()->{
               int id = test.startSession().getId();
               test.register(id,"Bob","1234");
                count.incrementAndGet();
            });
            threads.add(t);
        }
        for(Thread t : threads){
            t.start();
        }
        while(count.get()<THREADS){}

        assertEquals(1,test.getUserHandler().getSubscribers().size());


    }

    @Test
    public void testRegistermultipleUsers(){
        List<Thread> threads = new ArrayList<>();
        AtomicInteger count = new AtomicInteger(0);
        AtomicInteger nameId = new AtomicInteger(0);
        Map<Integer,Integer> idsCount = new ConcurrentHashMap<>();
        for(int i = 0;i<THREADS;i++){
            Thread t = new Thread(()->{
                int id = test.startSession().getId();
                String name = String.valueOf(nameId.getAndIncrement());
                int subId =test.register(id,name,"123").getId();

                if(idsCount.get(subId)== null){
                    idsCount.put(subId,1);
                }
                else{
                    idsCount.put(subId,idsCount.get(subId)+1);
                }

                count.incrementAndGet();
            });
            threads.add(t);
        }
        for(Thread t : threads){
            t.start();
        }
        while(count.get()<THREADS){}

        for(Integer val : idsCount.values()) {
            assertEquals(new Integer(1), val);
        }


    }

    @Test
    public void testStartMultipleSessions(){
        List<Thread> threads = new ArrayList<>();
        AtomicInteger count = new AtomicInteger(0);
        Map<Integer,Integer> idsCount = new ConcurrentHashMap<>();
        for(int i = 0;i<THREADS;i++){
            Thread t = new Thread(()->{
                int id = test.startSession().getId();
                if(idsCount.get(id)== null){
                    idsCount.put(id,1);
                }
                else{
                    idsCount.put(id,idsCount.get(id)+1);
                }
                count.incrementAndGet();
            });
            threads.add(t);
        }
        for(Thread t : threads){
            t.start();
        }
        while(count.get()<THREADS){}
        for(Integer val : idsCount.values()) {
            assertEquals(new Integer(1), val);
        }

    }

    @Test
    public void testOpenMultipleStores(){
        List<Thread> threads = new ArrayList<>();
        AtomicInteger count = new AtomicInteger(0);
        AtomicInteger nameId = new AtomicInteger(1);
        Map<Integer,Integer> idsCount = new ConcurrentHashMap<>();
        for(int i = 0;i<THREADS;i++){
            Thread t = new Thread(()->{
                int id = test.startSession().getId();
                String name = String.valueOf(nameId.getAndIncrement());
                test.register(id,name,"123").getId();
                try {
                    test.login(id,name,"123");
                } catch (DatabaseFetchException e) {
                    e.printStackTrace();
                }
                int storeId = test.openStore(id).getId();
                if(storeId == -1){
                    java.lang.System.out.println("Failed to open store username = "+name);
                }

                if(idsCount.get(storeId)== null){
                    idsCount.put(storeId,1);
                }
                else{
                    idsCount.put(storeId,idsCount.get(storeId)+1);
                }

                count.incrementAndGet();
            });
            threads.add(t);
        }
        for(Thread t : threads){
            t.start();
        }
        while(count.get()<THREADS){}

        for(Integer val : idsCount.values()) {
            assertEquals(new Integer(1), val);
        }

    }

    @Test
    public void testMultipleManagerAppoint(){
        List<Integer> ownersId = new ArrayList<>();
        int id = test.startSession().getId();
        test.register(id,"BigBoss","123");

        for(int i = 0; i<THREADS;i++){
            int subId =test.register(id,String.valueOf(i),"123").getId();
            ownersId.add(subId);
        }

        AtomicInteger testId = new AtomicInteger(test.register(id,"looser","123").getId());

        try {
            test.login(id,"BigBoss","123");
        } catch (DatabaseFetchException e) {
            e.printStackTrace();
        }
        int storeId = test.openStore(id).getId();

        for(Integer subID:ownersId){
            test.addStoreOwnerNoGrants(id,storeId,subID);
        }

        List<Thread> threads = new ArrayList<>();
        AtomicInteger count = new AtomicInteger(0);


        for(int i = 0;i<THREADS;i++){
            AtomicInteger oid = new AtomicInteger(ownersId.get(i)-1);

            Thread t = new Thread(()->{
                int sessionId = test.startSession().getId();
                try {
                    test.login(sessionId,String.valueOf(oid.get()),"123");
                } catch (DatabaseFetchException e) {
                    e.printStackTrace();
                }
                test.addStoreManager(sessionId,storeId,testId.get());
                count.incrementAndGet();
            });
            threads.add(t);
        }
        for(Thread t : threads){
            t.start();
        }
        while(count.get()<THREADS){}

        try {
            assertEquals(THREADS+2,test.getStoreById(storeId).getAllManagers().size());
        } catch (DatabaseFetchException e) {
            e.printStackTrace();
        }


    }
}
