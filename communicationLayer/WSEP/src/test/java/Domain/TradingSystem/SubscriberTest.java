package Domain.TradingSystem;

import DTOs.Notification;
import DataAccess.DAOManager;
import DataAccess.DatabaseFetchException;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;


public class SubscriberTest extends TestCase {

    private Subscriber subscriber;
    Map<Store, PurchaseDetails> storePurchaseDetails ;
    User user ;
    Store store ;
    HashMap<ProductInfo,Integer> products ;
    ProductInfo productInfo;
    PurchaseDetails details;
    private System test = System.getInstance();

    @Before
    public void setUp() {
        System.testing = true;

        int sessionId = test.startSession().getId();
        test.register(sessionId, "hava nagila", "1234").getId();
        try {
            test.login(sessionId, "hava nagila", "1234");
        } catch (DatabaseFetchException e) {
            e.printStackTrace();
        }
        subscriber = (Subscriber) test.getUser(sessionId).getState();
    }

    @After
    public void tearDown(){
        subscriber.getAllNotification().clear();
        DAOManager.clearDatabase();
    }

    @Test
    public void testGetUsernameSuccess() {
        assertEquals(subscriber.getUsername(),"hava nagila");

    }

    @Test
    public void testIsAdminFailureNotAdmin() {
        assertFalse(subscriber.isAdmin());
    }

    @Test
    public void testIsAdminSuccess() {
        subscriber = new Subscriber("hava nagila", "1234", true);
        assertTrue(subscriber.isAdmin());
    }

    @Test
    public void testSetUserName() {
        subscriber.setUserName("haha");
        assertEquals(subscriber.getUsername(),"haha");
    }

    @Test
    public void testLogout() {
        assertTrue(subscriber.logout());
    }

    @Test
    public void saveCart() {
        //ShoppingCart cart = new ShoppingCart(subscriber.getUser());
        //assertTrue(subscriber.saveCart(cart));

    }



    @Test
    public void testSetUser() {
        user = new User();
        subscriber.setUser(user);
        assertEquals(subscriber.getUser(),user);
    }

    @Test
    public void testSetUserFailureNull(){
        assertThrows(Exception.class,()->subscriber.setUser(null));

    }





    @Test
    public void testHasOwnerPermissionSuccess() throws DatabaseFetchException {
        store = new Store(0);
        int sessionId = test.startSession().getId();
        int subId = test.register(sessionId, "hava neranena", "4321").getId();
        Subscriber newSubscriber = test.getSubscriber(subId);
        newSubscriber.addPermission(store,subscriber, "Owner");
        newSubscriber.overridePermission("Manager",store,"New Owner");
        assertTrue(newSubscriber.hasOwnerPermission(store.getId()));

    }


    @Test
    public void testHasOwnerPermissionFailureNotOwner() {
        store = new Store(0);
        int sessionId = test.startSession().getId();
        test.register(sessionId, "hava neranena", "4321").getId();
        try {
            test.login(sessionId, "hava neranena", "4321");
        } catch (DatabaseFetchException e) {
            e.printStackTrace();
        }
        Subscriber newSubscriber = (Subscriber) test.getUser(sessionId).getState();

        newSubscriber.addPermission(store,subscriber, "Owner");
        newSubscriber.overridePermission("Manager",store,"New Owner");
        assertFalse(subscriber.hasOwnerPermission(store.getId()));
        assertFalse(subscriber.hasOwnerPermission(-1));

    }

    @Test
    public void testHasOwnerPermissionFailureNegativeId() {
        store = new Store(0);
        Subscriber newSubscriber = new Subscriber("hava neranena", "4321", false);
        newSubscriber.addPermission(store,subscriber, "Owner");
        newSubscriber.overridePermission("Manager",store,"New Owner");
        assertFalse(subscriber.hasOwnerPermission(-1));

    }


    @Test
    void hasPermissionSuccess() {
        store=subscriber.openStore();
        Subscriber newSubscriber = new Subscriber("hava neranena", "4321", false);
        newSubscriber.addPermission(store,subscriber,"Manager");
        assertEquals(newSubscriber.hasPermission(store.getId(),"Manager"),store);
    }


    @Test
    void hasPermissionFailure() {
        store=subscriber.openStore();
        Subscriber newSubscriber = new Subscriber("hava neranena", "4321", false);
        newSubscriber.addPermission(store,subscriber,"Manager");
        assertNull(newSubscriber.hasPermission(-3,"Manager"));
    }

    @Test
    void addPermissionSuccess() {
        store=subscriber.openStore();
        Subscriber newSubscriber = new Subscriber("hava neranena", "4321", false);
        assertTrue(newSubscriber.addPermission(store,subscriber,"Manager"));
        store = new Store(0);
        assertFalse(newSubscriber.addPermission(store,subscriber,"Manager"));//the grantor is not the store owner
    }

    @Test
    void addPermissionFailureGrantorNotStoreOwner() {
        store=subscriber.openStore();
        Subscriber newSubscriber = new Subscriber("hava neranena", "4321", false);
        store = new Store(0);
        assertFalse(newSubscriber.addPermission(store,subscriber,"Manager"));//the grantor is not the store owner
    }



    @Test
    public void testHasManagerPermissionSuccess() {
        store = subscriber.openStore();
        int sessionId = test.startSession().getId();
        test.register(sessionId, "hava neranena", "4321").getId();
        try {
            test.login(sessionId, "hava neranena", "4321");
        } catch (DatabaseFetchException e) {
            e.printStackTrace();
        }
        Subscriber newSubscriber = (Subscriber) test.getUser(sessionId).getState();
        newSubscriber.addPermission(store,subscriber,"Manager");
        newSubscriber.overridePermission("Manager",store,"New Manager");
        assertTrue(newSubscriber.hasManagerPermission(store.getId()));

    }


    @Test
    public void testHasManagerPermissionFailureNotManager() {
        store = subscriber.openStore();
        int sessionId = test.startSession().getId();
        test.register(sessionId, "hava neranena", "4321").getId();
        try {
            test.login(sessionId, "hava neranena", "4321");
        } catch (DatabaseFetchException e) {
            e.printStackTrace();
        }
        Subscriber newSubscriber = (Subscriber) test.getUser(sessionId).getState();
        newSubscriber.addPermission(store,subscriber,"Manager");
        newSubscriber.overridePermission("Manager",store,"New Manager");
        assertFalse(subscriber.hasManagerPermission(store.getId()));

    }


    @Test
    public void testHasManagerPermissionFailureNegativeId() {
        store = subscriber.openStore();
        int sessionId = test.startSession().getId();
        test.register(sessionId, "hava neranena", "4321").getId();
        try {
            test.login(sessionId, "hava neranena", "4321");
        } catch (DatabaseFetchException e) {
            e.printStackTrace();
        }
        Subscriber newSubscriber = (Subscriber) test.getUser(sessionId).getState();
        newSubscriber.addPermission(store,subscriber,"Manager");
        newSubscriber.overridePermission("Manager",store,"New Manager");
        assertFalse(subscriber.hasManagerPermission(-1));
    }




    @Test
    public void testGetManagerDetailsFailureNegativeIdSuccess() {
        store = new Store(0);
        int sessionId = test.startSession().getId();
        test.register(sessionId, "hava neranena", "4321").getId();
        try {
            test.login(sessionId, "hava neranena", "4321");
        } catch (DatabaseFetchException e) {
            e.printStackTrace();
        }
        Subscriber newSubscriber = (Subscriber) test.getUser(sessionId).getState();

        newSubscriber.addPermission(store,subscriber,"Manager");
        newSubscriber.overridePermission("Manager",store,"any");
        assertTrue(newSubscriber.getManagerDetails(store.getId()).contains("any"));
     }

    @Test
    public void testGetManagerDetailsFailureNegativeIdFailure() {
        store = new Store(0);
        int sessionId = test.startSession().getId();
        test.register(sessionId, "hava neranena", "4321").getId();
        try {
            test.login(sessionId, "hava neranena", "4321");
        } catch (DatabaseFetchException e) {
            e.printStackTrace();
        }
        Subscriber newSubscriber = (Subscriber) test.getUser(sessionId).getState();

        newSubscriber.addPermission(store,subscriber,"Manager");
        newSubscriber.overridePermission("Manager",store,"any");
        assertEquals(newSubscriber.getManagerDetails(-1),null);
    }

    @Test
    public void testGetManagerDetailsFailureNotManagerSuccess() {
        store = new Store(0);
        int sessionId = test.startSession().getId();
        test.register(sessionId, "hava neranena", "4321").getId();
        try {
            test.login(sessionId, "hava neranena", "4321");
        } catch (DatabaseFetchException e) {
            e.printStackTrace();
        }
        Subscriber newSubscriber = (Subscriber) test.getUser(sessionId).getState();

        newSubscriber.addPermission(store,subscriber,"Manager");
        newSubscriber.overridePermission("Manager",store,"any");
        assertTrue(newSubscriber.getManagerDetails(store.getId()).contains("any"));

    }

    @Test
    public void testGetManagerDetailsFailureNotManagerFailure() {
        store = new Store(0);
        int sessionId = test.startSession().getId();
        test.register(sessionId, "hava neranena", "4321").getId();
        try {
            test.login(sessionId, "hava neranena", "4321");
        } catch (DatabaseFetchException e) {
            e.printStackTrace();
        }
        Subscriber newSubscriber = (Subscriber) test.getUser(sessionId).getState();
        newSubscriber.addPermission(store,subscriber,"Manager");
        newSubscriber.overridePermission("Manager",store,"any");
        assertEquals(subscriber.getManagerDetails(store.getId()),null);
    }


    @Test
    public void testGetPurchaseHistory() {
        storePurchaseDetails = new HashMap<>();
        user = subscriber.getUser();
        store = new Store(0);
        int storeId = store.getId();
        products = new HashMap<>();
        productInfo = new ProductInfo(3,"bamba","hatif", 10);
        products.put(productInfo,2);
        details = new PurchaseDetails(user,store,products,13.8);
        storePurchaseDetails.put(store,details);
        subscriber.addPurchase(storePurchaseDetails);
        //Assert.assertEquals(subscriber.getPurchaseHistory());

    }

    @Test
    public void testSetNotification(){
        Notification notification = new Notification(9,"test");
        subscriber.setNotification(notification);
        assertEquals(1,subscriber.getAllNotification().size());
    }

    @Test
    public void testRemoveNotificationSuccess(){
        Notification notification = new Notification(42,"test");
        subscriber.setNotification(notification);
        subscriber.removeNotification(42);
        assertEquals(0,subscriber.getAllNotification().size());
    }

    @Test
    public void testRemoveNotificationWrongId(){
        Notification notification = new Notification(42,"test");
        subscriber.setNotification(notification);
        subscriber.removeNotification(10);
        assertEquals(1,subscriber.getAllNotification().size());
    }

    @Test
    public void testRemoveOwnershipAllIds(){
        Subscriber s2 = new Subscriber("Bob","123",false);
        Store store = new Store(0);
        subscriber.addPermission(store,null,"Owner");
        s2.addPermission(store,subscriber,"Manager");
        store.addOwner(subscriber);
        store.addOwner(s2);

        List<Integer> result = subscriber.removeOwnership(store.getId());

        assertTrue(result.contains(subscriber.getId()));
        assertTrue(result.contains(s2.getId()));
    }
}