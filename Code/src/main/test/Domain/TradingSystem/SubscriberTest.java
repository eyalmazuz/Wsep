package Domain.TradingSystem;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;



import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;


public class SubscriberTest extends TestCase {

    private Subscriber subscriber;
    Map<Integer, PurchaseDetails> storePurchaseDetails ;
    User user ;
    Store store ;
    Map<Integer,Integer> products ;
    ProductInfo productInfo;
    PurchaseDetails details;

    @Before
    public void setUp() {

        subscriber = new Subscriber("hava nagila", "1234", false);
        subscriber.setUser(new User());
    }

    @Test
    public void testGetUsername() {
        assertEquals(subscriber.getUsername(),"hava nagila");

    }

    @Test
    public void testIsAdmin() {
       assertFalse(subscriber.isAdmin());
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
    public void testGetHistory() {
        storePurchaseDetails = new HashMap<>();
        user = subscriber.getUser();
        store = new Store();
        int storeId = store.getId();
        products = new HashMap<>();
        products.put(3,2);
        productInfo = new ProductInfo(3,"bamba","hatif");
        details = new PurchaseDetails(3,user,products,13.8);
        storePurchaseDetails.put(storeId,details);
        subscriber.addPurchase(storePurchaseDetails);
        assertTrue(subscriber.getHistory().length()>1);

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
    public void testAddOwner() {
        store = new Store ();
        Subscriber newSubscriber = new Subscriber("hava neranena", "4321", false);
        assertTrue(subscriber.addOwner(store,newSubscriber));
        assertEquals(newSubscriber.getGrantor("Owner",store),subscriber);
    }

    @Test
    public void testAddManager() {
        store = new Store ();
        Subscriber newSubscriber = new Subscriber("hava neranena", "4321", false);
        assertTrue(subscriber.addManager(store,newSubscriber));
        assertEquals(newSubscriber.getGrantor("Manager",store),subscriber);
        assertFalse(subscriber.addManager(store,newSubscriber));//already manager
    }

    @Test
    public void testDeleteManager() {
        store = new Store ();
        Subscriber newSubscriber = new Subscriber("hava neranena", "4321", false);
        subscriber.addManager(store,newSubscriber);
        assertFalse(newSubscriber.deleteManager(store,newSubscriber));
        assertTrue(subscriber.deleteManager(store,newSubscriber));
        assertFalse(newSubscriber.deleteManager(store,subscriber));
    }

    @Test
    public void testHasOwnerPermission() {
        store = new Store();
        Subscriber newSubscriber = new Subscriber("hava neranena", "4321", false);
        subscriber.addOwner(store,newSubscriber);
        subscriber.editPermission(newSubscriber,store,"New Owner");
        assertTrue(newSubscriber.hasOwnerPermission(store.getId()));
        assertFalse(subscriber.hasOwnerPermission(store.getId()));
        assertFalse(subscriber.hasOwnerPermission(-1));

    }


    @Test
    void hasPermission() {
        store=subscriber.openStore();
        Subscriber newSubscriber = new Subscriber("hava neranena", "4321", false);
        subscriber.addManager(store,newSubscriber);
        assertEquals(newSubscriber.hasPermission(store.getId(),"Manager"),store);
        assertEquals(newSubscriber.hasPermission(-3,"Manager"),null);
    }

    @Test
    void addPermission() {
        store=subscriber.openStore();
        Subscriber newSubscriber = new Subscriber("hava neranena", "4321", false);
        assertTrue(newSubscriber.addPermission(store,subscriber,"Manager"));
        store = new Store();
        assertFalse(newSubscriber.addPermission(store,subscriber,"Manager"));//the grantor is not the store owner
    }



    @Test
    public void testHasManagerPermission() {
        store = subscriber.openStore();
        Subscriber newSubscriber = new Subscriber("hava neranena", "4321", false);
        subscriber.addManager(store,newSubscriber);
        subscriber.editPermission(newSubscriber,store,"New Manager");
       assertTrue(newSubscriber.hasManagerPermission(store.getId()));
        assertFalse(subscriber.hasManagerPermission(store.getId()));
        assertFalse(subscriber.hasManagerPermission(-1));
    }


    @Test
    public void testGetManagerDetails() {
        store = new Store();
        Subscriber newSubscriber = new Subscriber("hava neranena", "4321", false);
        subscriber.addManager(store,newSubscriber);
        subscriber.editPermission(newSubscriber,store,"New Manager");
        assertEquals(newSubscriber.getManagerDetails(store.getId()),"New Manager");
        assertEquals(newSubscriber.getManagerDetails(-1),null);
        assertEquals(subscriber.getManagerDetails(store.getId()),"");
    }

    @Test
    public void testEditPermission() {
        store = new Store ();
        Subscriber newSubscriber = new Subscriber("hava neranena", "4321", false);
        subscriber.addManager(store,newSubscriber);
        assertTrue(subscriber.editPermission(newSubscriber,store,"New Manager"));
        assertFalse(newSubscriber.editPermission(subscriber,store,""));
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
        products.put(3,2);
        productInfo = new ProductInfo(3,"bamba","hatif");
        details = new PurchaseDetails(3,user,products,13.8);
        storePurchaseDetails.put(storeId,details);
        subscriber.addPurchase(storePurchaseDetails);
        //Assert.assertEquals(subscriber.getPurchaseHistory());

    }
}