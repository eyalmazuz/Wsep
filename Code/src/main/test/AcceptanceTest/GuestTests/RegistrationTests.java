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
    public void testRegisterSuccessful() {
        assertTrue(register("bobTheKing", "1234"));
        assertTrue(register("bobby", "12131231"));
        assertTrue(register("bobob", "1232f3f23f"));
        assertTrue(register("bob123", "ggg12ggg34"));
    }

    @Test
    public void testRegisterFailureExistingUsername() {
        assertFalse(login("hanamaru", "123456"));
        assertFalse(login("chika", "12345"));
        assertFalse(login("kanan", "654321"));
        assertFalse(login("ruby", "54321"));
    }
}

