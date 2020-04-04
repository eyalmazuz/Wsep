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
    }

    @Test
    public void testLoginFailureExistingUsername() {
        assertFalse(login("bob", "123"));
    }
}

