package AcceptanceTest.GuestTests;

import AcceptanceTest.Data.Database;
import AcceptanceTest.ServiceTest;
import org.junit.Before;
import org.junit.Test;

public class ViewInfoTests extends ServiceTest {
    /*
     * USE CASES 2.4
     *
     * */
    @Before
    public void setUp(){
        super.setUp();
        login("hanamaru", "123456");
        openStore();
        addProdcut(5, 10);
        addProdcut(1, 1);
        logout();
        login("chika", "12345");
        openStore();
        addProdcut(5, 5);
        addProdcut(10, 10);
        logout();
        login("kanan", "654321");

    }

    @Test
    public void testGetAllInfoSuccessful(){
        assertEquals(getAllInfo(), Database.Stores);
    }
}
