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
        login("hanamru", "123456");
        login("chika", "12345");
        openStore();
        appointOwner(1,"hanamaru");
        appointManager(1, "dia");
    }

    //USECASES 4.6.1
    @Test
    public void testEditManagerOptionSuccessful(){
        assertTrue(editManagerOptions(1,1, 4));
        assertTrue(editManagerOptions(1,1, 2));
        assertTrue(editManagerOptions(1,1, 3));
    }

    @Test
    public void testEditManagerOptionFailureInvalidOption(){
        assertFalse(editManagerOptions(1,1, 16));
        assertFalse(editManagerOptions(1, 32));
        assertFalse(editManagerOptions(1, 10));
        assertFalse(editManagerOptions(1, 15));
        assertFalse(editManagerOptions(1, 14));
    }


}
