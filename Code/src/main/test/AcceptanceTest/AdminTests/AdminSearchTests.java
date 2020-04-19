package AcceptanceTest.AdminTests;

import AcceptanceTest.Data.Database;
import AcceptanceTest.ServiceTest;
import org.junit.Before;
import org.junit.Test;

public class AdminSearchTests extends ServiceTest {
    /*
     * USE CASES 6.4.1-6.4.2
     *
     * */
    @Before
    public void setUp(){
        super.setUp();
        login("hanamaru", "123456");
        addToCart(1,1, 3);
        buyCart(viewCart());
        logout();
        login("admin", "admin");


    }

    //USE CASES 6.4.1
    @Test
    public void testSearchUserHistorySuccessful(){
        assertNotNull(searchUserHistory(Database.userToId.get("hanamaru")));
    }

    @Test
    public void testSearchUserHistoryFailure(){
        assertNull(searchUserHistory(Database.userToId.get("danny")));
    }


    //USE CASES 6.4.2
    @Test
    public void testSearchStoreHistorySuccessful(){
        assertNotNull(searchStoreHistory(1));
    }

    @Test
    public void testSearchStoreHistoryFailure(){
        assertNull(searchStoreHistory(2));
    }


}
