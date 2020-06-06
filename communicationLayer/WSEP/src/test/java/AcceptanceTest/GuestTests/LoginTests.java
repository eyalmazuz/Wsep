package AcceptanceTest.GuestTests;

import AcceptanceTest.Data.Database;
import AcceptanceTest.Driver;
import AcceptanceTest.ServiceTest;
import DataAccess.DAOManager;
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
        super.tearDown();

//        Database.userToId.clear();
//        Database.userToStore.clear();
    }


    @Test
    public void testLoginSuccessful(){
        assertTrue(login(Database.sessionId, "hanamaru", "12345"));
        logout(Database.sessionId);

    }

    @Test
    public void testLoginFailureWrongPassword(){
        assertFalse(login(Database.sessionId, "hanamaru", "12345657"));

    }

    @Test
    public void testLoginFailureNonExistingUser(){
        assertFalse(login(Database.sessionId, "yohariko", "1234"));

    }

    @Test
    public void testLoginFailureBreakingSystem(){
        assertFalse(login(Database.sessionId, "", ""));
        assertFalse(login(Database.sessionId, "", null));
        assertFalse(login(Database.sessionId, null, ""));
        assertFalse(login(Database.sessionId, null, null));
    }

    @Test
    public void testLoginFailureAlreadyLoggedin(){
        login(Database.sessionId, "chika", "12345");
        assertFalse(login(Database.sessionId, "chika", "12345"));
    }

}
