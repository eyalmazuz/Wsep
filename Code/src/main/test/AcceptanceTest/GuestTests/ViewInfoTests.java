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
        addProdcut(1,1, 5);
        addProdcut(2,1,  5);
        logout();
        login("chika", "12345");
        openStore();
        addProdcut(1,1, 5);
        addProdcut(3,1, 10);
        logout();
        login("kanan", "654321");

    }

    @Test
    public void testGetAllInfoSuccessful(){
        assertEquals(getAllInfo(), Database.Stores);
    }
}
