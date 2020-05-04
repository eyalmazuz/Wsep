package Domain.TradingSystem;

import DTOs.ResultCode;
import Domain.Logger.SystemLogger;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class SystemTest extends TestCase {
    //System Unitesting
    System test = new System();
    UserHandler mockHandler = new userHandlerMock();

    @Before
    public void setUp(){
        mockHandler.setSubscribers(new HashMap<Integer, Subscriber>() {{
            put(1, new Subscriber("Yaron", "abc123", false));
        }});
        mockHandler.createSession();
        test.setUserHandler(mockHandler);
        test.setLogger(new LoggerMock());
        List<Store> stores = new LinkedList<>();

        stores.add(new StoreMock(1));
        stores.add(new StoreMock(2));
        test.setStores(stores);
    }

    @After
    public void tearDown(){
        test.deleteStores();
    }

    @Test
     public void testSetUp(){
      assertFalse(test.setup("Error","Error"));
      assertTrue(test.setup("123","123"));
    }


    @Test
    public void testAddStoreTest() {
        int size = test.getStores().size();
        test.addStore();
        assertEquals(size+1,test.getStores().size());
    }

    @Test
    public void testIsSubscriberTest() {

        assertTrue(test.isSubscriber(2));
        assertFalse(test.isSubscriber(-1));
        assertFalse(test.isSubscriber(1));
    }

    @Test
    public void testIsAdminTest() {
        assertTrue(test.isAdmin(2));
        assertFalse(test.isAdmin(-1));
        assertFalse(test.isAdmin(1));
    }

    @Test
    public void testIsGuestTest() {
        assertFalse(test.isGuest(2));
        assertFalse(test.isGuest(-1));
        assertTrue(test.isGuest(1));
    }



    @Test
    public void testOpenStoreTest() {
        int size = test.getStores().size();
        assertEquals(-1,test.openStore(-1));
        assertEquals(-1,test.openStore(1));
        assertTrue(test.openStore(2)>=0);
        assertEquals(size+1,test.getStores().size());
    }

    @Test
    public void testGetHistoryTest() {
        assertNull(test.getHistory(-1));
        assertNull(test.getHistory(1));
        assertEquals("Mock History",test.getHistory(2));
    }

    @Test
    public void testGetStoreByIdTest(){
        Store s1 = test.getStoreById(1);
        Store s2 = test.getStoreById(5);
        assertEquals("1",s1.toString());
        assertNull(s2);

    }


    @Test
    public void testSearchProducts() {
        List<Store> stores = test.getStores();

        stores.get(0).setRating(3);
        stores.get(1).setRating(4);

        String res0 = test.searchProducts(1, "apple", null, null, -1 ,-1);
        assertTrue(res0.contains("product ID: 5"));

        // spelling checker
        String resSpeller = test.searchProducts(1, "appple", null, null, -1 ,-1);
        assertTrue(resSpeller.contains("product ID: 5"));

        // spelling checker
        String resSpeller2 = test.searchProducts(1, "applee", null, null, -1 ,-1);
        assertTrue(resSpeller2.contains("product ID: 5"));

        String res1 = test.searchProducts(1, "bamba", null, null, -1 ,-1);
        assertTrue(res1.contains("product ID: 4"));

        String res15 = test.searchProducts(1, "bamba", null, null, -1 ,3);
        assertTrue(res15.contains("Store ID: 1"));
        assertTrue(res15.contains("Store ID: 2"));

        String res16 = test.searchProducts(1, "bamba", null, null, -1 ,4);
        assertFalse(res16.contains("Store ID: 1"));
        assertTrue(res16.contains("Store ID: 2"));

        String res2 = test.searchProducts(1, null, "fruit", null, -1 ,-1);
        assertTrue(res2.contains("product ID: 5"));

        // apple's descrition is "very ripe apple"
        String[] keywords = {"ripe"};
        String res3 = test.searchProducts(1, null, null, keywords, -1 ,-1);
        assertTrue(res3.contains("product ID: 5"));

        String res4 = test.searchProducts(1, null, null, null, 2 ,-1);
        assertTrue(res4.contains("product ID: 5"));
        assertTrue(res4.contains("product ID: 4"));

        String res5 = test.searchProducts(1, null, null, null, 3 ,-1);
        assertTrue(res5.contains("product ID: 4"));
        assertFalse(res5.contains("product ID: 5"));

        String res6 = test.searchProducts(1, null, null, null, -1 ,3);
        //out.println(res6);
        assertFalse(res6.contains("product ID: 4"));
        assertFalse(res6.contains("product ID: 5"));

        String res8 = test.searchProducts(1, "bruh product", null, null, -1 ,-1);
        assertFalse(res8.contains("product ID: 4"));
        assertFalse(res8.contains("product ID: 5"));

        String res9 = test.searchProducts(1, null, "bruh category", null, -1 ,-1);
        assertFalse(res9.contains("product ID: 4"));
        assertFalse(res9.contains("product ID: 5"));
    }

    @Test
    public void testViewStoreProductInfo() {
        assertNotNull(test.viewStoreProductInfo());
    }


    @Test
    public void testGetUserHistory() {
        assertNull(test.getHistory(-1));
        assertNotNull(test.getHistory(3));
    }


    // Usecase 2.6, 2.7
    // HERE WE TEST ONLY VALIDITY OF PRODUCT AND STORE EXISTENCE IN SYSTEM
    // LOGICAL DOMAIN TESTS ARE IN ShoppingCartTest
    @Test
    public void testAddProductToCart() {
        List<Integer> userSessionIDs = new ArrayList<>();
        int sessionId = mockHandler.users.keySet().iterator().next();
        List<Store> stores = test.getStores();
        List<Integer> storeIds = new ArrayList<>();
        for (Store s : stores) {
            storeIds.add(s.getId());
        }
        int maxId = Collections.max(storeIds);
        int badStore = maxId + 1;
        assertNotSame(test.addToCart(sessionId, badStore, 4, 40).getResultCode(), ResultCode.SUCCESS);

        List<Integer> productIds = new ArrayList<>();
        List<ProductInfo> products = test.getProducts();
        if (products.isEmpty()) products.add(new ProductInfo(1, "gloves", "hygiene"));

        for (ProductInfo info : products) {
            productIds.add(info.getId());
        }
        maxId = Collections.max(productIds);
        int badProduct = maxId + 1;
        assertNotSame(test.addToCart(sessionId, stores.get(0).getId(), badProduct, 40).getResultCode(), ResultCode.SUCCESS);

        assertNotSame(test.addToCart(sessionId, stores.get(0).getId(), products.get(0).getId(), 0).getResultCode(), ResultCode.SUCCESS);
        assertNotSame(test.addToCart(sessionId, stores.get(0).getId(), products.get(0).getId(), -1).getResultCode(), ResultCode.SUCCESS);
        assertNotSame(test.addToCart(-1, stores.get(0).getId(), products.get(0).getId(), 4).getResultCode(), ResultCode.SUCCESS);
    }

    @Test
    public void testEditProductInCart() {
        int sessionId = mockHandler.users.keySet().iterator().next();
        List<Store> stores = test.getStores();
        List<Integer> storeIds = new ArrayList<>();
        for (Store s : stores) {
            storeIds.add(s.getId());
        }
        int maxId = Collections.max(storeIds);
        int badStore = maxId + 1;
        assertNotSame(test.updateAmount(sessionId, badStore, 4, 40).getResultCode(), ResultCode.SUCCESS);

        List<Integer> productIds = new ArrayList<>();
        List<ProductInfo> products = test.getProducts();
        if (products.isEmpty()) products.add(new ProductInfo(1, "gloves", "hygiene"));

        for (ProductInfo info : products) {
            productIds.add(info.getId());
        }
        maxId = Collections.max(productIds);
        int badProduct = maxId + 1;
        assertNotSame(test.updateAmount(sessionId, stores.get(0).getId(), badProduct, 40).getResultCode(), ResultCode.SUCCESS);

        assertNotSame(test.updateAmount(sessionId, stores.get(0).getId(), products.get(0).getId(), 0).getResultCode(), ResultCode.SUCCESS);
        assertNotSame(test.updateAmount(sessionId, stores.get(0).getId(), products.get(0).getId(), -1).getResultCode(), ResultCode.SUCCESS);

        test.addToCart(sessionId, stores.get(0).getId(), products.get(0).getId(), 40);

        assertNotSame(test.updateAmount(-1, stores.get(0).getId(), products.get(0).getId(), 4).getResultCode(), ResultCode.SUCCESS);
    }

    @Test
    public void testRemoveProductFromCart() {
        int sessionId = mockHandler.users.keySet().iterator().next();
        List<Store> stores = test.getStores();
        List<Integer> storeIds = new ArrayList<>();
        for (Store s : stores) {
            storeIds.add(s.getId());
        }
        int maxId = Collections.max(storeIds);
        int badStore = maxId + 1;
        assertNotSame(test.deleteItemInCart(sessionId, badStore, 4).getResultCode(), ResultCode.SUCCESS);

        List<Integer> productIds = new ArrayList<>();
        List<ProductInfo> products = test.getProducts();
        if (products.isEmpty()) products.add(new ProductInfo(1, "gloves", "hygiene"));

        for (ProductInfo info : products) {
            productIds.add(info.getId());
        }
        maxId = Collections.max(productIds);
        int badProduct = maxId + 1;
        assertNotSame(test.deleteItemInCart(sessionId, stores.get(0).getId(), badProduct).getResultCode(), ResultCode.SUCCESS);

        assertNotSame(test.deleteItemInCart(-1, stores.get(0).getId(), products.get(0).getId()).getResultCode(), ResultCode.SUCCESS);
    }

    // usecase 2.8.4
    @Test
    public void testUpdateStoreProductSupplies() {
        List<Store> stores = test.getStores();
        Store store1 = stores.get(0);
        // this store contains 10 bamba (id 4), 2 apple (id 5)
        Map<Integer, Integer> productAmounts = new HashMap<>();
        productAmounts.put(4, 7);
        productAmounts.put(5, 2);
        test.removeStoreProductSupplies(store1.getId(), productAmounts);

        boolean problem = false;
        for (ProductInStore pis : store1.getProducts()) {
            if (pis.getId() == 4 && pis.getAmount() != 3) {
                problem = true;
                break;
            }
            if (pis.getId() == 5) {
                problem = true;
                break;
            }
        }
        assertFalse(problem);
    }


}


class StoreMock extends Store{
    int id;

    List<ProductInStore> mockProducts = new ArrayList<>();

    public StoreMock(int i) {
        id = i;
        ProductInfo bamba = new ProductInfo(4, "bamba", "snack");
        bamba.setRating(3);
        ProductInfo apple = new ProductInfo(5, "apple", "fruit");
        apple.setRating(2);

        if (id == 1) {
            ProductInStore appleInStore = new ProductInStore(apple, 2, this);
            appleInStore.editInfo("very ripe apple");
            mockProducts.add(appleInStore);
            mockProducts.add(new ProductInStore(bamba, 10, this));
        }
        else mockProducts.add(new ProductInStore(bamba, 3, this));
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("%d",id);
    }

    @Override
    public List<ProductInStore> getProducts() {
        return mockProducts;
    }

    @Override
    public int getProductAmount(Integer productId) {
        for (ProductInStore product : getProducts()) {
            if (productId == product.getId()) {
                return product.getAmount();
            }
        }
        return 0;
    }

    @Override
    public void removeProductAmount(Integer productId, Integer amount) {
        for (ProductInStore product : getProducts()) {
            int id = product.getId();
            if (productId == id) {
                int newAmount = product.getAmount() - amount;
                if (newAmount == 0) {
                    getProducts().remove(product);
                } else {
                    product.setAmount(newAmount);
                }
            }
        }
    }
}

class userHandlerMock extends UserHandler {

    @Override
    public void setAdmin() {

    }

    @Override
    public User getUser(int sessionId) {
        if (sessionId < 0) {
            return null;
        } else {
            if (sessionId == 1)
                return new UserMock("Guest");
            else if (sessionId == 2) {
                return new UserMock("Admin");
            } else if (sessionId == 3) {
                return new UserMock("Owner");
            }
        }
        return new UserMock("Manager");
    }



    @Override
    public Subscriber getSubscriber(int subId) {
        if(subId>0)
            return new SubscriberMock("Owner");
        return null;
    }
}

class UserMock extends User{
    private String type;

    @Override
    public UserState getState() {
        if(type.equals("Manager"))
            return new SubscriberMock("Manger");
        if(type.equals("Owner"))
            return new SubscriberMock("Owner");
        else
            return new GuestMock();
    }

    public UserMock(String type) {
        this.type = type;
    }


    public UserMock() {

    }

    @Override
    public Store openStore() {
        if (type.equals("Guest"))
            return null;
        return new StoreMock(4);
    }

    @Override
    public boolean isGuest() {
        return type.equals("Guest");
    }

    @Override
    public String getHistory() {
        if (type.equals("Guest"))
            return null;
        return "Mock History";
    }

    @Override
    public boolean isAdmin() {
        return type.equals("Admin");
    }


}

class LoggerMock extends SystemLogger{

    @Override
    public void info(String msg) {

    }

    @Override
    public void error(String msg) {

    }
}

class SubscriberMock extends Subscriber{
    String type;

    public SubscriberMock(String type){
        this.type = type;
    }
    @Override
    public boolean checkPrivilage(int store_id, String type) {
        return true;
    }

    @Override
    public boolean hasManagerPermission(int storeId) {
        return type.equals("Manager");
    }

    @Override
    public boolean hasOwnerPermission(int storeId) {
        return type.equals("Owner");
    }

    @Override
    public String getHistory() {
        return "Mock";
    }
}

class GuestMock extends Guest{}