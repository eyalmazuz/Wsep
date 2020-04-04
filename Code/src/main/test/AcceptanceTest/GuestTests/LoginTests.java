package AcceptanceTest.GuestTests;

import AcceptanceTest.ServiceTest;
import org.junit.Before;
import org.junit.Test;

public class LoginTests extends ServiceTest {
    /*
    * USE CASES 2.3
    *
    * */
    @Before
    public void setUp(){
        super.setUp();
    }

    @Test
    public void testLoginSuccessful(){
        assertTrue(login("bob", "1234"));
        assertTrue(login("danny", "password"));

    }

    @Test
    public void testLoginFailureWrongPassword(){
        assertFalse(login("bob", "123"));
        assertFalse(login("bob", "bobIsKing1223"));
        assertFalse(login("bob", "password"));
        assertFalse(login("bob", "hardPassword"));
    }

    @Test
    public void testLoginFailureNonExistingUser(){
        assertFalse(login("bob1234", "1234"));
        assertFalse(login("dasfaf", "4515"));
        assertFalse(login("sadasd", "123121231"));
    }
}
