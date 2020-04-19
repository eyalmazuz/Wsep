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
        assertFalse(register("hanamaru", "123456") > 0);
        assertFalse(register("chika", "12345") > 0);
        assertFalse(register("kanan", "654321") > 0);
        assertFalse(register("ruby", "54321") > 0);
    }

    @Test
    public void testLoginRegistrationBreakingSystem(){
        assertFalse(register("", null) > 0);
        assertFalse(register(null, "") > 0);
        assertFalse(register(null, null) > 0);
    }
}

