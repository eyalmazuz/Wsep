package AcceptanceTest.GuestTests;

import AcceptanceTest.ServiceTest;
import org.junit.Before;
import org.junit.Test;

public class RegistrationTests extends ServiceTest {
    /*
     * USE CASES 2.2
     *
     * */
    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void testLoginSuccessful() {
        assertTrue(register("bobTheKing", "1234"));
        assertTrue(register("bobby", "12131231"));
        assertTrue(register("bobob", "1232f3f23f"));
        assertTrue(register("bob123", "ggg12ggg34"));
    }

    @Test
    public void testLoginFailureExistingUsername() {
        assertFalse(login("danny", "123456"));
        assertFalse(login("moshe", "12345"));
        assertFalse(login("ggwpxdlolrofl", "11123"));
        assertFalse(login("ariel", "121113"));
    }
}

