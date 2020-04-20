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
        assertTrue(editManagerOptions(Database.userToStore.get("chika"),1, "can edit prices"));
        assertTrue(editManagerOptions(Database.userToStore.get("chika"),1, "can change amount"));
        assertTrue(editManagerOptions(Database.userToStore.get("chika"),1,"can go fuck himself"));
    }

    @Test
    public void testEditManagerOptionFailureInvalidOption(){
        assertFalse(editManagerOptions(Database.userToStore.get("chika"),1, "delete store"));
        assertFalse(editManagerOptions(Database.userToStore.get("chika"),1, "trololol"));
        assertFalse(editManagerOptions(Database.userToStore.get("chika"),1, "hahahah"));
        assertFalse(editManagerOptions(Database.userToStore.get("chika"),1,"can break system"));
        assertFalse(editManagerOptions(Database.userToStore.get("chika"),1, "can give free money"));
    }


}
