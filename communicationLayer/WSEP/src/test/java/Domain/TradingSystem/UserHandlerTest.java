package Domain.TradingSystem;

import DataAccess.DAOManager;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


public class UserHandlerTest extends TestCase {

    UserHandler uh = new UserHandler();

    @Before
    public void setUp() {
        System.testing = true;

        DAOManager.clearDatabase();


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
    public void testRegisterSucsessID() {
        int prev = uh.subscribers.size();
        int id1 = uh.register("Bob","123");
        assertTrue(id1>=0);

    }

    @Test
    public void testRegisterSucsessSize() {
        int prev = uh.subscribers.size();
        int id1 = uh.register("Bob","123");
        assertEquals(prev+1,uh.subscribers.size());

    }

    @Test
    public void testRegisterDifferentIds() {

        int id1 = uh.register("Bob","123");
        int id2 = uh.register("Moshe","234");
        assertTrue(id1 != id2);
    }


    @Test
    public void testRegisterBadValues1() {
        assertEquals(-1, uh.register(null, "123"));
    }

    @Test
    public void testRegisterBadValues2() {
        assertEquals(-1, uh.register("Yaron", null));
    }

    @Test
    public void testRegisterBadValues3() {
        assertEquals(-1,uh.register(null,null));
    }

    @Test
    public void testRegisterSameUsername() {
        uh.register("bob","123");
        assertEquals(-1,uh.register("bob","456"));
    }

    @Test
    public void testGetUserSuccess() {
        int id = uh.createSession();
        assertNotNull(uh.getUser(id));
    }

    @Test
    public void testGetUserSize() {
        int prev = uh.users.size();
        uh.createSession();
        assertEquals(prev+1,uh.users.size());
    }

    @Test
    public void testGetUserBadValues1() {
        assertNull(uh.getUser(5));
    }


    @Test
    public void testGetUserBadValues2() {
        assertNull(uh.getUser(-6));
    }

    @Test
    public void testGetSubscriber() {
        int id = uh.register("a","a");
        assertNotNull(uh.getSubscriber(id));
    }

    @Test
    public void testGetSubscriberBadValues1() {
        assertNull(uh.getSubscriber(5));
    }

    @Test
    public void testGetSubscriberBadValues2() {
        assertNull(uh.getSubscriber(-6));
    }

    @Test
    public void testGetSubscriberUser() {
        registerSubs();
        assertNotNull(uh.getSubscriberUser("test","123"));
    }

    @Test
    public void testGetSubscriberUserBadValues1() {
        registerSubs();
        assertNull(uh.getSubscriberUser(null,null));
    }


    @Test
    public void testGetSubscriberUserBadValues2() {
        registerSubs();
        assertNull(uh.getSubscriberUser("test","blabla"));
    }

    @Test
    public void testGetAvailableUsersToOwn() {
        List<Subscriber> list = prepareSubsList();
        registerSubs();
        List<Subscriber> result = uh.getAvailableUsersToOwn(list);
        List<Integer> ids = result.stream().map(Subscriber::getId).collect(Collectors.toList());
        for(Subscriber s : list){
            assertFalse(ids.contains(s.getId()));
        }

    }

    @Test
    public void testGetAvailableUsersToOwnBadEmpty() {
    registerSubs();
    List<Subscriber> list1 = uh.getAvailableUsersToOwn(null);
    assertTrue(list1.isEmpty());
    }

    @Test
    public void testGetAvailableUsersToOwnBadSize() {
        registerSubs();
        List<Subscriber> list2 = uh.getAvailableUsersToOwn(new LinkedList<>());
        assertEquals(uh.subscribers.size(),list2.size());
    }



    @Test
    public void testSetStateSuccess() {
        int id = uh.createSession();
        registerSubs();
        Subscriber s = uh.getSubscriberUser("test", "123");
        uh.setState(id,s.getId());
        User u = uh.getUser(id);
        assertTrue(!u.isGuest());//the state has changed
    }


    @Test
    public void testSetStateSuccess2() {
        int id = uh.createSession();
        registerSubs();
        Subscriber s = uh.getSubscriberUser("test", "123");
        uh.setState(id,s.getId());
        User u = uh.getUser(id);
        Subscriber userState = (Subscriber)u.getState();
        assertEquals(s.getId(),userState.getId());//the state is the correct one
    }




    @Test
    public void testCreateSessionDiffIds() {
        int id1 = uh.createSession();
        int id2 = uh.createSession();
        assertTrue(id1 != id2);
    }

    public void testCreateSessionSuccessID() {
        int id = uh.createSession();
        assertTrue(id>=0);
    }

    public void testCreateSessionSuccessSize() {
        int prev = uh.users.size();
        uh.createSession();
        assertEquals(prev+1,uh.users.size());
    }
}