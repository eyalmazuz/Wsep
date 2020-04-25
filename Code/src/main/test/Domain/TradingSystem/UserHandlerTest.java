package Domain.TradingSystem;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static junit.framework.TestCase.*;


public class UserHandlerTest extends TestCase {

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
    public void testSetNoAdminTest() {
        uh.setAdmin();
        assertEquals(1,uh.subscribers.size());
    }

    @Test
    public void testSetExsistAdminTest() {
        Subscriber s = new Subscriber("a","a",true);
        uh.subscribers.put(s.getId(),s);
        uh.setAdmin();
        assertEquals(1, uh.subscribers.size());
    }

    @Test
    public void testRegister() {
        int id1 = uh.register("Bob","123");
        int id2 = uh.register("Moshe","234");
        assertTrue(id1>=0);
        assertTrue(id1 != id2);
    }

    @Test
    public void testRegisterBadValues() {
        assertEquals(-1, uh.register(null, "123"));
        assertEquals(-1, uh.register("Yaron", null));
        assertEquals(-1,uh.register(null,null));

        uh.register("bob","123");
        assertEquals(-1,uh.register("bob","456"));
    }

    @Test
    public void testGetUser() {
        int id = uh.createSession();
        assertNotNull(uh.getUser(id));
    }

    @Test
    public void testGetUserBadValues() {
        assertNull(uh.getUser(5));
        assertNull(uh.getUser(-6));
    }

    @Test
    public void testGetSubscriber() {
        int id = uh.register("a","a");
        assertNotNull(uh.getSubscriber(id));
    }

    @Test
    public void testGetSubscriberBadValues() {
        assertNull(uh.getSubscriber(5));
        assertNull(uh.getSubscriber(-6));
    }

    @Test
    public void testGetSubscriberUser() {
        registerSubs();
        assertNotNull(uh.getSubscriberUser("test","123"));
    }

    @Test
    public void testGetSubscriberUserBadValues() {
        registerSubs();
        assertNull(uh.getSubscriberUser(null,null));
        assertNull(uh.getSubscriberUser("test","blabla"));

    }


    @Test
    public void testGetAvailableUsersToOwn() {

        List<Subscriber> list = prepareSubsList();
        registerSubs();
        List<Integer> result = uh.getAvailableUsersToOwn(list);
        for(Subscriber s : list){
            assertTrue(!result.contains(s.getId()));
        }

    }

    @Test
    public void testGetAvailableUsersToOwnBad() {
    registerSubs();
    List<Integer> list1 = uh.getAvailableUsersToOwn(null);
    List<Integer> list2 = uh.getAvailableUsersToOwn(new LinkedList<>());
    assertTrue(list1.isEmpty());
    assertEquals(uh.subscribers.size(),list2.size());
    }



    @Test
    public void testSetState() {
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
    public void testCreateSessionDiffIds() {
        int id1 = uh.createSession();
        int id2 = uh.createSession();
        assertTrue(id1 != id2);
    }
}