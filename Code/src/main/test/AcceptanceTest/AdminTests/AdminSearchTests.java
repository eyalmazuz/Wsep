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
    }

    //USE CASES 6.4.1
    @Test
    public void testSearchUserHistorySuccessful(){
        assertEquals(searchUserHistory("bob"), "Bob, Register Date: 3.3.3, Purchases: 30000");
    }

    @Test
    public void testSearchUserHistoryFailure(){
        assertEquals(searchUserHistory("danny"), "Invalid user");
    }


    //USE CASES 6.4.2
    @Test
    public void testSearchStoreHistorySuccessful(){
        assertEquals(searchStoreHistory("Amazon"), "Amazon, Register Date: 3.3.3, items: 30000");
    }

    @Test
    public void testSearchStoreHistoryFailure(){
        assertEquals(searchStoreHistory("ie taigaaa"), "Invalid Store");
    }


}
