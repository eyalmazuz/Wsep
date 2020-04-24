package Domain.TradingSystem;

import org.junit.Before;
import org.junit.Test;
import org.testng.Assert;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.*;

class SubscriberTest {

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
    }

    @Test
    void getUsername() {
        Assert.assertEquals(subscriber.getUsername(),"hava nagila");

    }

    @Test
    void isAdmin() {
        Assert.assertFalse(subscriber.isAdmin());
        subscriber = new Subscriber("hava nagila", "1234", true);
        Assert.assertTrue(subscriber.isAdmin());

    }

    @Test
    void setAdmin() {
        subscriber.setAdmin();
        Assert.assertTrue(subscriber.isAdmin());

    }

    @Test
    void setUserName() {
        subscriber.setUserName("haha");
        Assert.assertEquals(subscriber.getUsername(),"haha");
    }

    @Test
    void logout() {
        Assert.assertTrue(subscriber.logout());
    }

    @Test
    void saveCart() {
        ???
    }

    @Test
    void getHistory() {
        storePurchaseDetails = new HashMap<>();
        user = subscriber.getUser();
        store = new Store();
        int storeId = store.getId();
        products = new HashMap<>();
        products.put(3,2);
        productInfo = new ProductInfo(3);
        details = new PurchaseDetails(3,user,products,13.8);
        storePurchaseDetails.put(storeId,details);
        subscriber.addPurchase(storePurchaseDetails);
        Assert.assertTrue(subscriber.getHistory().length()>1);

    }

    @Test
    void setUser() {
        user = new User();
        subscriber.setUser(user);
        Assert.assertEquals(subscriber.getUser(),user);
    }

    @Test
    void openStore() {
        subscriber.openStore();
        subscriber.eichBodkim?
    }

    @Test
    void addProductToStore() {
        store = new Store ();
        product = new ProductInfo(1);
        Assert.assertTrue(subscriber.addProductToStore(store,1,5));
        Assert.assertFalse(subscriber.addProductToStore(store,3,5));//productid does not exist

    }

    @Test
    void checkPrivilage() {
        ??
    }

    @Test
    void editProductInStore() {
        ???

    }

    @Test
    void deleteProductFromStore() {
        store = new Store ();
        product = new ProductInfo(1);
        subscriber.addProductToStore(store,1,5);
        Assert.assertTrue(subscriber.deleteProductFromStore(store,1));
        Assert.assertFalse(subscriber.deleteProductFromStore(store,2));

    }

    @Test
    void addOwner() {
        store = new Store ();
        Subscriber newSubscriber = new Subscriber("hava neranena", "4321", false);
        Assert.assertTrue(subscriber.addOwner(store,newSubscriber));
        Assert.assertEquals(newSubscriber.getGrantor("Owner",store),subscriber);
    }

    @Test
    void addManager() {
        store = new Store ();
        Subscriber newSubscriber = new Subscriber("hava neranena", "4321", false);
        Assert.assertTrue(subscriber.addManager(store,newSubscriber));
        Assert.assertEquals(newSubscriber.getGrantor("Manager",store),subscriber);
    }

    @Test
    void deleteManager() {
        store = new Store ();
        Subscriber newSubscriber = new Subscriber("hava neranena", "4321", false);
        subscriber.addManager(store,newSubscriber);
        Assert.assertFalse(newSubscriber.deleteManager(store,newSubscriber));
        Assert.assertTrue(subscriber.deleteManager(store,newSubscriber));
        Assert.assertFalse(newSubscriber.deleteManager(store,subscriber));
    }

    @Test
    void hasOwnerPermission() {
        store = new Store();
        Subscriber newSubscriber = new Subscriber("hava neranena", "4321", false);
        subscriber.addOwner(store,newSubscriber);
        subscriber.editPermission(newSubscriber,store,"New Owner");
        Assert.assertTrue(newSubscriber.hasOwnerPermission(store.getId()));
        Assert.assertFalse(subscriber.hasOwnerPermission(store.getId()));
        Assert.assertFalse(subscriber.hasOwnerPermission(-1));

    }

    @Test
    void hasPermission() {
        ein li koah
    }

    @Test
    void addPermission() {
        ein li koah
    }

    @Test
    void getId() {
    }

    @Test
    void hasManagerPermission() {
        store = new Store();
        Subscriber newSubscriber = new Subscriber("hava neranena", "4321", false);
        subscriber.addManager(store,newSubscriber);
        subscriber.editPermission(newSubscriber,store,"New Manager");
        Assert.assertTrue(newSubscriber.hasManagerPermission(store.getId()));
        Assert.assertFalse(subscriber.hasManagerPermission(store.getId()));
        Assert.assertFalse(subscriber.hasManagerPermission(-1));
    }

    @Test
    void isGrantedBy() {
        ??
    }

    @Test
    void getManagerDetails() {
        store = new Store();
        Subscriber newSubscriber = new Subscriber("hava neranena", "4321", false);
        subscriber.addManager(store,newSubscriber);
        subscriber.editPermission(newSubscriber,store,"New Manager");
        Assert.assertEquals(newSubscriber.getManagerDetails(store.getId()),"New Manager");
        Assert.assertEquals(newSubscriber.getManagerDetails(-1),null);
        Assert.assertEquals(subscriber.getManagerDetails(store.getId()),"");
    }

    @Test
    void editPermission() {
        store = new Store ();
        Subscriber newSubscriber = new Subscriber("hava neranena", "4321", false);
        subscriber.addManager(store,newSubscriber);
        Assert.assertTrue(subscriber.editPermission(newSubscriber,store,"New Manager"));
        Assert.assertFalse(newSubscriber.editPermission(subscriber,store,""));
    }

    @Test
    void addPurchase() {
        ??
    }

    @Test
    void removePurchase() {
        ??
    }

    @Test
    void getHashedPassword() {
        Assert.assertEquals("1234",subscriber.getHashedPassword());
    }

    @Test
    void getPurchaseHistory() {
        storePurchaseDetails = new HashMap<>();
        user = subscriber.getUser();
        store = new Store();
        int storeId = store.getId();
        products = new HashMap<>();
        products.put(3,2);
        productInfo = new ProductInfo(3);
        details = new PurchaseDetails(3,user,products,13.8);
        storePurchaseDetails.put(storeId,details);
        subscriber.addPurchase(storePurchaseDetails);
        //Assert.assertEquals(subscriber.getPurchaseHistory());
        Assert.HATSILU ;
    }
}