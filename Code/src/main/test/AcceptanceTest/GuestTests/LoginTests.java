package AcceptanceTest.GuestTests;

import AcceptanceTest.Data.Database;
import AcceptanceTest.ServiceTest;
import org.junit.After;
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
        logout(Database.sessionId);
    }

    @After
    public void tearDown(){
        Database.userToId.clear();
        Database.userToStore.clear();
    }


    @Test
    public void testLoginSuccessful(){
        assertTrue(login(Database.sessionId, "hanamaru", "12345"));
        logout(Database.sessionId);
        assertTrue(login(Database.sessionId, "chika", "12345"));
        logout(Database.sessionId);
        assertTrue(login(Database.sessionId, "kanan", "12345"));
        logout(Database.sessionId);
        assertTrue(login(Database.sessionId, "ruby", "12345"));
        logout(Database.sessionId);

    }

    @Test
    public void testLoginFailureWrongPassword(){
        assertFalse(login(Database.sessionId, "hanamaru", "12345657"));
        assertFalse(login(Database.sessionId, "chika", "bobIsKing1223"));
        assertFalse(login(Database.sessionId, "chika", "password"));
        assertFalse(login(Database.sessionId, "ruby", "hardPassword"));
    }

    @Test
    public void testLoginFailureNonExistingUser(){
        assertFalse(login(Database.sessionId, "yohariko", "1234"));
        assertFalse(login(Database.sessionId, "yoahne", "4515"));
        assertFalse(login(Database.sessionId, "yoshiko", "123121231"));
    }

    @Test
    public void testLoginFailureBreakingSystem(){
        assertFalse(login(Database.sessionId, "", ""));
        assertFalse(login(Database.sessionId, "", null));
        assertFalse(login(Database.sessionId, null, ""));
        assertFalse(login(Database.sessionId, null, null));
    }
}
