package AcceptanceTest.RegisteredUserTest;

import AcceptanceTest.ServiceTest;
import org.junit.Before;
import org.junit.Test;

public class UserActionsTests extends ServiceTest {
    /*
     * USE CASES 3.1-3.2,3.7
     *
     * */
    @Before
    public void setUp(){
        super.setUp();
        login("hanamaru", "123456");
        openStore();
        addProdcut(1, 1, 5);
        addProdcut(2, 1, 5);
        editProduct(1, 40, "Food");
        editProduct(2, 50, "Food");
        logout();

        login("chika", "12345");
        addToCart(1, 5);
        addToCart(2, 5);
        buyCart(viewCart());

    }

    //USE CASES 3.1
    @Test
    public void testLogout(){
        assertTrue(logout());
    }


    //USE CASES 3.2
    @Test
    public void testOpenStoreSuccessful(){
        assertTrue(openStore());
        assertTrue(openStore());
    }

    //USE CASES 3.7
    @Test
    public void testViewPurchaseHistory(){

        assertNotNull(viewPurchaseHistory());
    }

}
