package AcceptanceTest.AdminTests;

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
        login("chika", "12345");
        openStore();
        addProdcut(1,1, 5);
        addProdcut(2,1, 5);
        logout();
        login("hanamaru", "123456");
        addToCart(1, 3);
        buyCart("hanamaru", viewCart());
        logout();
        login("admin", "admin");


    }

    //USE CASES 6.4.1
    @Test
    public void testSearchUserHistorySuccessful(){
        assertNotNull(searchUserHistory("hanamaru"));
    }

    @Test
    public void testSearchUserHistoryFailure(){
        assertNull(searchUserHistory("danny"));
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
