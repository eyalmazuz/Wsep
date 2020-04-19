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
        assertTrue(login("hanamaru", "12345"));
        logout();
        assertTrue(login("chika", "12345"));
        logout();
        assertTrue(login("kanan", "12345"));
        logout();
        assertTrue(login("ruby", "12345"));
        logout();

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
        assertFalse(login("yohariko", "1234"));
        assertFalse(login("yoahne", "4515"));
        assertFalse(login("yoshiko", "123121231"));
    }

    @Test
    public void testLoginFailureBreakingSystem(){
        assertFalse(login("", ""));
        assertFalse(login("", null));
        assertFalse(login(null, ""));
        assertFalse(login(null, null));
    }
}
