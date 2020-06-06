package AcceptanceTest.GuestTests;

import AcceptanceTest.Data.Database;
import AcceptanceTest.ServiceTest;
import DataAccess.DAOManager;
import org.junit.After;
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

    @After
    public void tearDown(){
        super.tearDown();

//        Database.userToId.clear();
//        Database.userToStore.clear();
    }


    @Test
    public void testRegisterSuccessful() {
        int sessionId = startSession();
        assertTrue(register(sessionId, "yohane", "1234") > 0);
        assertTrue(register(sessionId, "sarah", "12345") > 0);
    }

    @Test
    public void testRegisterFailureExistingUsername() {
        int sessionId = startSession();
        assertFalse(register(sessionId, "hanamaru", "123456") > 0);
        assertFalse(register(sessionId, "chika", "12345") > 0);
        assertFalse(register(sessionId,"kanan", "654321") > 0);
        assertFalse(register(sessionId, "ruby", "54321") > 0);
    }

    @Test
    public void testLoginRegistrationBreakingSystem(){
        int sessionId = startSession();
        assertFalse(register(sessionId, "", null) > 0);
        assertFalse(register(sessionId, null, "") > 0);
        assertFalse(register(sessionId, null, null) > 0);
    }
}

