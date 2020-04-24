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

    }

    @Test
    void editProductInStore() {
    }

    @Test
    void deleteProductFromStore() {
    }

    @Test
    void addOwner() {
    }

    @Test
    void addManager() {
    }

    @Test
    void deleteManager() {
    }

    @Test
    void hasOwnerPermission() {
    }

    @Test
    void hasPermission() {
    }

    @Test
    void addPermission() {
    }

    @Test
    void getId() {
    }

    @Test
    void hasManagerPermission() {
    }

    @Test
    void isGrantedBy() {
    }

    @Test
    void getManagerDetails() {
    }

    @Test
    void editPermission() {
    }

    @Test
    void addPurchase() {
    }

    @Test
    void removePurchase() {
    }

    @Test
    void getHashedPassword() {
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