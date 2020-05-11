package Domain.TradingSystem;

import junit.framework.TestCase;
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
    public void testHasOwnerPermission() {
        store = new Store();
        Subscriber newSubscriber = new Subscriber("hava neranena", "4321", false);
        //subscriber.addOwner(store,newSubscriber);
        newSubscriber.addPermission(store,subscriber, "Owner");
        newSubscriber.overridePermission("Manager",store,"New Owner");

        assertTrue(newSubscriber.hasOwnerPermission(store.getId()));
        assertFalse(subscriber.hasOwnerPermission(store.getId()));
        assertFalse(subscriber.hasOwnerPermission(-1));

    }


    @Test
    void hasPermission() {
        store=subscriber.openStore();
        Subscriber newSubscriber = new Subscriber("hava neranena", "4321", false);
        newSubscriber.addPermission(store,subscriber,"Manager");
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
        newSubscriber.addPermission(store,subscriber,"Manager");
        newSubscriber.overridePermission("Manager",store,"New Manager");
       assertTrue(newSubscriber.hasManagerPermission(store.getId()));
        assertFalse(subscriber.hasManagerPermission(store.getId()));
        assertFalse(subscriber.hasManagerPermission(-1));
    }


    @Test
    public void testGetManagerDetails() {
        store = new Store();
        Subscriber newSubscriber = new Subscriber("hava neranena", "4321", false);
        newSubscriber.addPermission(store,subscriber,"Manager");
        newSubscriber.overridePermission("Manager",store,"any");
        assertEquals(newSubscriber.getManagerDetails(store.getId()),"any");
        assertEquals(newSubscriber.getManagerDetails(-1),null);
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
}