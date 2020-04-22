package AcceptanceTest.GuestTests;

import AcceptanceTest.Data.Database;
import AcceptanceTest.ServiceTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SearchProductTests extends ServiceTest {
    /*
     * USE CASES 2.5
     *
     * */
    @Before
    public void setUp(){
        super.setUp();
        login(Database.sessionId, "chika", "12345");
        int sid_1 = openStore(Database.sessionId);
        Database.userToStore.put("chika", sid_1);
        addProdcut(Database.sessionId, 1, sid_1, 5);
        addProdcut(Database.sessionId, 2, sid_1, 5);
        logout(Database.sessionId);

        login(Database.sessionId, "hanamaru", "12345");
        int sid_2 = openStore(Database.sessionId);
        Database.userToStore.put("hanamaru", sid_2);
        addProdcut(Database.sessionId, 2, sid_2, 10);
        logout(Database.sessionId);
    }

    @After
    public void tearDown(){
        Database.userToId.clear();
        Database.userToStore.clear();
    }


    @Test
    public void testSearchProductSuccessful(){
        String products = searchProducts(Database.sessionId, 1, "KB", null, -1, -1, 0, 0);
        System.out.println(products);
        assertNotNull(products);
    }

    @Test
    public void testSearchProductsFilterSuccessful(){
        assertNotNull(searchProducts(Database.sessionId, 1, "KB", null, -1, -1, 0, 0));

    }

    public void testSearchProductsFailure(){
        assertNull(searchProducts(Database.sessionId, 0, null, null, -1, -1, 2500, 0));

    }


}
