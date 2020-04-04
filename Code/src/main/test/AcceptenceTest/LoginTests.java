package AcceptenceTest;

import org.junit.Before;
import org.junit.Test;

public class LoginTests extends ServiceTest {

    @Before
    public void setUp(){
        super.setUp();
    }

    @Test
    public void testLoginSuccessful(){
        assertTrue(login("bob", "1234"));
    }

    @Test
    public void testLoginFailureWrongPassword(){
        assertFalse(login("bob", "123"));
    }

    @Test
    public void testLoginFailureNonExistingUser(){
        assertFalse(login("bob1234", "1234"));
    }
}
