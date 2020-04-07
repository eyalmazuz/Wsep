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
    void copyCart() {
        ShoppingCart cart2 = new ShoppingCart();
        shoppingCart.copyCart(cart2);
        for(ShoppingBasket basket : cart2.getBaskets()){
            assertTrue(shoppingCart.getBaskets().contains(basket));
        }
    }

    @Test
    void cleanCart() {
        shoppingCart.cleanCart();
        assertTrue(shoppingCart.getBaskets().size() == 0);

    }
}