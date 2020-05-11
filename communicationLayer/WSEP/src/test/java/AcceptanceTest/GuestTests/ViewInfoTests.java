package AcceptanceTest.GuestTests;

import AcceptanceTest.Data.Database;
import AcceptanceTest.ServiceTest;
import org.junit.After;
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

        login(Database.sessionId, "chika", "12345");
        int sid_1 = openStore(Database.sessionId);
        Database.userToStore.put("chika", sid_1);
        addProdcut(true,Database.sessionId, 1, sid_1, 5);
        addProdcut(true,Database.sessionId, 2, sid_1, 5);
        logout(Database.sessionId);

        login(Database.sessionId, "hanamaru", "12345");
        int sid_2 = openStore(Database.sessionId);
        Database.userToStore.put("hanamaru", sid_2);
        addProdcut(true,Database.sessionId, 2, sid_2, 10);
        logout(Database.sessionId);

        Database.Stores = "Store ID: " + String.valueOf(Database.userToStore.get("chika")) + "\n" +
                "Buying policy: \n" +
                "Discount policy: \n" +
                "Products:\n" +
                "\n" +
                "Store ID: " + String.valueOf(Database.userToStore.get("chika")) + ", product ID: 1, amount: 5, info: null\n" +
                "Store ID: " + String.valueOf(Database.userToStore.get("chika")) + ", product ID: 2, amount: 5, info: null\n" +
                "\n" +
                "--------------------------\n" +
                "Store ID: " + String.valueOf(Database.userToStore.get("hanamaru")) + "\n" +
                "Buying policy: \n" +
                "Discount policy: \n" +
                "Products:\n" +
                "\n" +
                "Store ID: " + String.valueOf(Database.userToStore.get("hanamaru")) + ", product ID: 2, amount: 10, info: null\n" +
                "\n" +
                "--------------------------\n";


    }

    @After
    public void tearDown(){
//        Database.userToId.clear();
//        Database.userToStore.clear();
        Database.Stores = null;
    }

    @Test
    public void testGetAllInfoSuccessful(){
        String stores = getAllInfo(Database.sessionId);
        assertEquals(stores, Database.Stores);
    }
}
