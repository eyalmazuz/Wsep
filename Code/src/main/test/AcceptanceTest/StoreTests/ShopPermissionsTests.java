package AcceptanceTest.StoreTests;

import AcceptanceTest.Data.Database;
import AcceptanceTest.ServiceTest;
import org.junit.Before;
import org.junit.Test;

public class ShopPermissionsTests extends ServiceTest {
    /*
     * USE CASES 4.6.1-4.6.2
     *
     * */
    @Before
    public void setUp(){
        super.setUp();
        login("chika", "12345");
    }

    //USECASES 4.6.1
    @Test
    public void testEditManagerOptionSuccessful(){
        assertTrue(editManagerOptions(Database.userToStore.get("chika"), Database.userToId.get("dia"), "can edit prices"));
        assertTrue(editManagerOptions(Database.userToStore.get("chika"), Database.userToId.get("dia"), "can change amount"));
        assertTrue(editManagerOptions(Database.userToStore.get("chika"), Database.userToId.get("dia"),"can go fuck himself"));
    }

    // TEST HERE SUPPOSE TO FAIL CAUSE NO IMPLEMENTATION YET
    @Test
    public void testEditManagerOptionFailureInvalidOption(){
        assertFalse(editManagerOptions(Database.userToStore.get("chika"), Database.userToId.get("dia"), "delete store"));
        assertFalse(editManagerOptions(Database.userToStore.get("chika"), Database.userToId.get("dia"),"can break system"));
        assertFalse(editManagerOptions(Database.userToStore.get("chika"), Database.userToId.get("dia"), "can give free money"));
    }


}
