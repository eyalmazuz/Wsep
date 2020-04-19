package Domain.TradingSystem;

import org.junit.Before;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShoppingCartTest {

    private ShoppingCart shoppingCart;

    @Before
    private void setUp(){
        shoppingCart = new ShoppingCart();
    }


    @Test
    void cleanCart() {
        shoppingCart.removeAllProducts();
        assertTrue(shoppingCart.getBaskets().size() == 0);

    }
}