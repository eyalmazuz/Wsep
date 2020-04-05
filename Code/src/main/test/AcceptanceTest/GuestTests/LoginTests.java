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
        assertTrue(login("hanamaru", "123456"));
        assertTrue(login("chika", "12345"));
        assertTrue(login("kanan", "654321"));
        assertTrue(login("ruby", "54321"));

    }

    @Test
    public void testLoginFailureWrongPassword(){
        assertFalse(login("hanamaru", "12345657"));
        assertFalse(login("chika", "bobIsKing1223"));
        assertFalse(login("chika", "password"));
        assertFalse(login("runy", "hardPassword"));
    }

    @Test
    public void testLoginFailureNonExistingUser(){
        assertFalse(login("bob1234", "1234"));
        assertFalse(login("dasfaf", "4515"));
        assertFalse(login("sadasd", "123121231"));
    }
}
