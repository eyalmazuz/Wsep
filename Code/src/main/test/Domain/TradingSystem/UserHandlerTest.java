package Domain.TradingSystem;

import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserHandlerTest {

    UserHandler uh = new UserHandler();

    @Before
    public void setUp() {


    }

    public void registerSubs(){
        uh.register("test","123");
        uh.register("1test","123");
        uh.register("2test","123");
        uh.register("3test","123");
    }


    private List<Subscriber> prepareSubsList() {
        List<Subscriber> output = new LinkedList<>();
        for(int i = 1; i<4 ; i++){
            int id = uh.register("own_test"+i,"1234");
            output.add(uh.getSubscriber(id));
        }
        return output;
    }

    @After
    public void tearDown(){
        uh.deleteUsers();
    }

    @Test
    public void setNoAdminTest() {
        uh.setAdmin();
        assertEquals(1,uh.subscribers.size());
    }

    @Test
    public void setExsistAdminTest() {
        Subscriber s = new Subscriber("a","a",true);
        uh.subscribers.put(s.getId(),s);
        uh.setAdmin();
        assertEquals(1, uh.subscribers.size());
    }

    @Test
    void register() {
        int id1 = uh.register("Bob","123");
        int id2 = uh.register("Moshe","234");
        assertTrue(id1>=0);
        assertTrue(id1 != id2);
    }

    @Test
    void registerBadValues() {
        assertEquals(-1,uh.register(null,null));

        uh.register("bob","123");
        assertEquals(-1,uh.register("bob","123"));
    }

    @Test
    void getUser() {
        int id = uh.createSession();
        assertNotNull(uh.getUser(id));
    }

    @Test
    void getUserBadValues() {
        assertNull(uh.getUser(5));
        assertNull(uh.getUser(-6));
    }

    @Test
    void getSubscriber() {
        int id = uh.register("a","a");
        assertNotNull(uh.getSubscriber(id));
    }

    @Test
    void getSubscriberBadValues() {
        assertNull(uh.getSubscriber(5));
        assertNull(uh.getSubscriber(-6));
    }

    @Test
    void getSubscriberUser() {
        registerSubs();
        assertNotNull(uh.getSubscriberUser("test","123"));
    }

    @Test
    void getSubscriberUserBadValues() {
        registerSubs();
        assertNull(uh.getSubscriberUser(null,null));
        assertNull(uh.getSubscriberUser("test","blabla"));

    }


    @Test
    void getAvailableUsersToOwn() {

        List<Subscriber> list = prepareSubsList();
        registerSubs();
        List<Integer> result = uh.getAvailableUsersToOwn(list);
        for(Subscriber s : list){
            assertTrue(!result.contains(s.getId()));
        }

    }

    @Test
    void getAvailableUsersToOwnBad() {
    registerSubs();
    List<Integer> list1 = uh.getAvailableUsersToOwn(null);
    List<Integer> list2 = uh.getAvailableUsersToOwn(new LinkedList<>());
    assertTrue(list1.isEmpty());
    assertTrue(list2.isEmpty());
    }



    @Test
    void setState() {
        int id = uh.createSession();
        registerSubs();
        Subscriber s = uh.getSubscriberUser("test","123");
        uh.setState(id,s.getId());
        User u = uh.getUser(id);
        assertTrue(!u.isGuest());
        Subscriber userState = (Subscriber)u.getState();
        assertEquals(s.getId(),userState.getId());
    }

    @Test
    void createSessionDiffIds() {
        int id1 = uh.createSession();
        int id2 = uh.createSession();
        assertTrue(id1 != id2);
    }
}