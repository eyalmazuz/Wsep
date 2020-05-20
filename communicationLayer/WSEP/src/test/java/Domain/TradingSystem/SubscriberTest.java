package Domain.TradingSystem;

import DTOs.Notification;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;


public class SubscriberTest extends TestCase {

    private Subscriber subscriber;
    Map<Store, PurchaseDetails> storePurchaseDetails ;
    User user ;
    Store store ;
    Map<ProductInfo,Integer> products ;
    ProductInfo productInfo;
    PurchaseDetails details;

    @Before
    public void setUp() {

        subscriber = new Subscriber("hava nagila", "1234", false);
        subscriber.setUser(new User());
    }

    @After
    public void tearDown(){
        subscriber.getAllNotification().clear();
    }

    @Test
    public void testGetUsername() {
        assertEquals(subscriber.getUsername(),"hava nagila");

    }

    @Test
    public void testIsAdminFailure() {
        assertFalse(subscriber.isAdmin());
    }

    @Test
    public void testIsAdminSuccess() {
        subscriber = new Subscriber("hava nagila", "1234", true);
        assertTrue(subscriber.isAdmin());
    }

    @Test
    public void testSetAdmin() {
        subscriber.setAdmin();
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
    public void testSetUserNotNull(){
        assertThrows(Exception.class,()->subscriber.setUser(null));

    }





    @Test
    public void testHasOwnerPermissionSuccess() {
        store = new Store();
        Subscriber newSubscriber = new Subscriber("hava neranena", "4321", false);
        newSubscriber.addPermission(store,subscriber, "Owner");
        newSubscriber.overridePermission("Manager",store,"New Owner");
        assertTrue(newSubscriber.hasOwnerPermission(store.getId()));

    }


    @Test
    public void testHasOwnerPermissionFailureNotOwner() {
        store = new Store();
        Subscriber newSubscriber = new Subscriber("hava neranena", "4321", false);
        newSubscriber.addPermission(store,subscriber, "Owner");
        newSubscriber.overridePermission("Manager",store,"New Owner");
        assertFalse(subscriber.hasOwnerPermission(store.getId()));
        assertFalse(subscriber.hasOwnerPermission(-1));

    }

    @Test
    public void testHasOwnerPermissionFailureNegativeId() {
        store = new Store();
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
        assertEquals(newSubscriber.hasPermission(-3,"Manager"),null);
    }


    @Test
    void hasPermissionFailure() {
        store=subscriber.openStore();
        Subscriber newSubscriber = new Subscriber("hava neranena", "4321", false);
        newSubscriber.addPermission(store,subscriber,"Manager");
        assertEquals(newSubscriber.hasPermission(-3,"Manager"),null);
    }

    @Test
    void addPermissionSuccess() {
        store=subscriber.openStore();
        Subscriber newSubscriber = new Subscriber("hava neranena", "4321", false);
        assertTrue(newSubscriber.addPermission(store,subscriber,"Manager"));
        store = new Store();
        assertFalse(newSubscriber.addPermission(store,subscriber,"Manager"));//the grantor is not the store owner
    }

    @Test
    void addPermissionFailure() {
        store=subscriber.openStore();
        Subscriber newSubscriber = new Subscriber("hava neranena", "4321", false);
        store = new Store();
        assertFalse(newSubscriber.addPermission(store,subscriber,"Manager"));//the grantor is not the store owner
    }



    @Test
    public void testHasManagerPermissionSuccess() {
        store = subscriber.openStore();
        Subscriber newSubscriber = new Subscriber("hava neranena", "4321", false);
        newSubscriber.addPermission(store,subscriber,"Manager");
        newSubscriber.overridePermission("Manager",store,"New Manager");
        assertTrue(newSubscriber.hasManagerPermission(store.getId()));

    }


    @Test
    public void testHasManagerPermissionFailureNotManager() {
        store = subscriber.openStore();
        Subscriber newSubscriber = new Subscriber("hava neranena", "4321", false);
        newSubscriber.addPermission(store,subscriber,"Manager");
        newSubscriber.overridePermission("Manager",store,"New Manager");
        assertFalse(subscriber.hasManagerPermission(store.getId()));

    }


    @Test
    public void testHasManagerPermissionFailureNegativeId() {
        store = subscriber.openStore();
        Subscriber newSubscriber = new Subscriber("hava neranena", "4321", false);
        newSubscriber.addPermission(store,subscriber,"Manager");
        newSubscriber.overridePermission("Manager",store,"New Manager");
        assertFalse(subscriber.hasManagerPermission(-1));
    }




    @Test
    public void testGetManagerDetailsFailureNegativeIdSuccess() {
        store = new Store();
        Subscriber newSubscriber = new Subscriber("hava neranena", "4321", false);
        newSubscriber.addPermission(store,subscriber,"Manager");
        newSubscriber.overridePermission("Manager",store,"any");
        assertEquals(newSubscriber.getManagerDetails(store.getId()),"any");
     }

    @Test
    public void testGetManagerDetailsFailureNegativeIdFailure() {
        store = new Store();
        Subscriber newSubscriber = new Subscriber("hava neranena", "4321", false);
        newSubscriber.addPermission(store,subscriber,"Manager");
        newSubscriber.overridePermission("Manager",store,"any");
        assertEquals(newSubscriber.getManagerDetails(-1),null);
    }

    @Test
    public void testGetManagerDetailsFailureNotManagerSuccess() {
        store = new Store();
        Subscriber newSubscriber = new Subscriber("hava neranena", "4321", false);
        newSubscriber.addPermission(store,subscriber,"Manager");
        newSubscriber.overridePermission("Manager",store,"any");
        assertEquals(newSubscriber.getManagerDetails(store.getId()),"any");

    }

    @Test
    public void testGetManagerDetailsFailureNotManagerFailure() {
        store = new Store();
        Subscriber newSubscriber = new Subscriber("hava neranena", "4321", false);
        newSubscriber.addPermission(store,subscriber,"Manager");
        newSubscriber.overridePermission("Manager",store,"any");
        assertEquals(subscriber.getManagerDetails(store.getId()),null);
    }



    /*
    @Test
    void addPurchase() {
        ??
    }

    @Test
    void removePurchase() {
        ??
    }
*/
    @Test
    public void testGetHashedPassword() {
       assertEquals("1234",subscriber.getHashedPassword());
    }

    @Test
    public void testGetPurchaseHistory() {
        storePurchaseDetails = new HashMap<>();
        user = subscriber.getUser();
        store = new Store();
        int storeId = store.getId();
        products = new HashMap<>();
        productInfo = new ProductInfo(3,"bamba","hatif");
        products.put(productInfo,2);
        details = new PurchaseDetails(3,user,store,products,13.8);
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
    public void testRemoveNotificationWrondId(){
        Notification notification = new Notification(42,"test");
        subscriber.setNotification(notification);
        subscriber.removeNotification(10);
        assertEquals(1,subscriber.getAllNotification().size());
    }
}