package AcceptanceTest.StoreTests;

import AcceptanceTest.ServiceTest;
import org.junit.Before;
import org.junit.Test;

//TODO FIX THIS ENTIRE CLASS
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
        assertTrue(editManagerOptions(1,1,1, "can edit prices"));
        assertTrue(editManagerOptions(1,1,1, "can change amount"));
        assertTrue(editManagerOptions(1,1,1, "can go fuck himself"));
    }

    @Test
    public void testEditManagerOptionFailureInvalidOption(){
        assertFalse(editManagerOptions(1,1,1, "delete store"));
        assertFalse(editManagerOptions(1,1,1, "trololol"));
        assertFalse(editManagerOptions(1,1,1, "hahahah"));
        assertFalse(editManagerOptions(1,1,1,"can break system"));
        assertFalse(editManagerOptions(1,1,1, "can give free money"));
    }


}
