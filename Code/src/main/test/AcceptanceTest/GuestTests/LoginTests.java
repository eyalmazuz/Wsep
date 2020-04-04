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
        assertTrue(login("moshe", "12345"));
        assertTrue(login("danny", "123456"));

    }

    @Test
    public void testLoginFailureWrongPassword(){
        assertFalse(login("danny", "12345657"));
        assertFalse(login("moshe", "bobIsKing1223"));
        assertFalse(login("danny", "password"));
        assertFalse(login("danny", "hardPassword"));
    }

    @Test
    public void testLoginFailureNonExistingUser(){
        assertFalse(login("bob1234", "1234"));
        assertFalse(login("dasfaf", "4515"));
        assertFalse(login("sadasd", "123121231"));
    }
}
