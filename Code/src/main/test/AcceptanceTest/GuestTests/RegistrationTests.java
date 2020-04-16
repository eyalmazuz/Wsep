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
        assertTrue(register("yohane", "1234") > 0);
        assertTrue(register("sarah", "12345") > 0);
    }

    @Test
    public void testRegisterFailureExistingUsername() {
        assertFalse(login("hanamaru", "123456"));
        assertFalse(login("chika", "12345"));
        assertFalse(login("kanan", "654321"));
        assertFalse(login("ruby", "54321"));
    }

    @Test
    public void testLoginRegistrationBreakingSystem(){
        assertFalse(login("", ""));
        assertFalse(login("", null));
        assertFalse(login(null, ""));
        assertFalse(login(null, null));
    }
}

