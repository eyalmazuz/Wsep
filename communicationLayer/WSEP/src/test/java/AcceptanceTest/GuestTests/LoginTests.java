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
    }

    @After
    public void tearDown(){
        super.tearDown();

//        Database.userToId.clear();
//        Database.userToStore.clear();
    }


    @Test
    public void testLoginSuccessful(){
        int sessionId = startSession();
        assertTrue(login(sessionId, "hanamaru", "12345"));
        logout(sessionId);

    }

    @Test
    public void testLoginFailureWrongPassword(){
        int sessionId = startSession();
        assertFalse(login(sessionId, "hanamaru", "12345657"));

    }

    @Test
    public void testLoginFailureNonExistingUser(){
        int sessionId = startSession();
        assertFalse(login(sessionId, "yohariko", "1234"));

    }

    @Test
    public void testLoginFailureBreakingSystem(){
        int sessionId = startSession();
        assertFalse(login(sessionId, "", ""));
        assertFalse(login(sessionId, "", null));
        assertFalse(login(sessionId, null, ""));
        assertFalse(login(sessionId, null, null));
    }

    @Test
    public void testLoginFailureAlreadyLoggedin(){
        int sessionId = startSession();
        login(sessionId, "chika", "12345");
        assertFalse(login(sessionId, "chika", "12345"));
    }

}
