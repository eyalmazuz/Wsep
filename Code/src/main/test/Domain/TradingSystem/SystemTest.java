package Domain.TradingSystem;

import Domain.Logger.SystemLogger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static junit.framework.TestCase.*;

public class SystemTest {
    //System Unitesting
    System test = new System();
    UserHandler mockHandler = new userHandlerMock();

    @Before
    public void setUp(){
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
     public void setUpTest(){
      assertFalse(test.setup("Error","Error"));
      assertTrue(test.setup("123","123"));
    }


    @Test
    public void addStoreTest() {
        int size = test.getStores().size();
        test.addStore();
        assertEquals(size+1,test.getStores().size());
    }

    @Test
    public void isSubscriberTest() {

        assertTrue(test.isSubscriber(2));
        assertFalse(test.isSubscriber(-1));
        assertFalse(test.isSubscriber(1));
    }

    @Test
    public void isAdminTest() {
        assertTrue(test.isAdmin(2));
        assertFalse(test.isAdmin(-1));
        assertFalse(test.isAdmin(1));
    }

    @Test
    public void isGuestTest() {
        assertFalse(test.isGuest(2));
        assertFalse(test.isGuest(-1));
        assertTrue(test.isGuest(1));
    }



    @Test
    public void openStoreTest() {
        int size = test.getStores().size();
        assertEquals(-1,test.openStore(-1));
        assertEquals(-1,test.openStore(1));
        assertTrue(test.openStore(2)>=0);
        assertEquals(size+1,test.getStores().size());
    }

    @Test
    public void getHistoryTest() {
        assertNull(test.getHistory(-1));
        assertNull(test.getHistory(1));
        assertEquals("Mock History",test.getHistory(2));
    }

    @Test
    public void getStoreByIdTest(){
        Store s1 = test.getStoreById(1);
        Store s2 = test.getStoreById(5);
        assertEquals("1",s1.toString());
        assertNull(s2);

    }

    @Test
    public void searchProducts() {
        //#TODO:THIS
    }


    @Test
    public void getUserHistory() {
        assertNull(test.getHistory(-1));
        assertNotNull(test.getHistory(3));
    }


}

class StoreMock extends Store{
    int id;

    public StoreMock(int i) {
        id = i;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("%d",id);
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