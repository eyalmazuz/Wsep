package AcceptanceTest.GuestTests;

import AcceptanceTest.Data.Database;
import AcceptanceTest.ServiceTest;
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
//        Database.userToId.clear();
//        Database.userToStore.clear();
    }


    @Test
    public void testRegisterSuccessful() {
        assertTrue(register(Database.sessionId, "yohane", "1234") > 0);
        assertTrue(register(Database.sessionId, "sarah", "12345") > 0);
    }

    @Test
    public void testRegisterFailureExistingUsername() {
        assertFalse(register(Database.sessionId, "hanamaru", "123456") > 0);
        assertFalse(register(Database.sessionId, "chika", "12345") > 0);
        assertFalse(register(Database.sessionId,"kanan", "654321") > 0);
        assertFalse(register(Database.sessionId, "ruby", "54321") > 0);
    }

    @Test
    public void testLoginRegistrationBreakingSystem(){
        assertFalse(register(Database.sessionId, "", null) > 0);
        assertFalse(register(Database.sessionId, null, "") > 0);
        assertFalse(register(Database.sessionId, null, null) > 0);
    }
}

