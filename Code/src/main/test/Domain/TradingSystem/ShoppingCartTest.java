package Domain.TradingSystem;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.*;


public class ShoppingCartTest {

    private ShoppingCart shoppingCart;
    private Store store1, store2, store3;

    @Before
    public void setUp() {
        shoppingCart = new ShoppingCart();
        store1 = new Store();
        store2 = new Store();
        store3 = new Store();
    }

    @Test
    public void addProduct() {
        assertTrue(shoppingCart.addProduct(store1, 4, 40));
        boolean found = false;
        for (ShoppingBasket basket : shoppingCart.getBaskets()) {
            if (basket.getStoreId() == store1.getId()) {
                if (basket.getProducts().containsKey(4) && basket.getProducts().get(4) == 40) found = true;
            }
        }
        assertTrue(found);

        found = false;
        assertTrue(shoppingCart.addProduct(store1, 4, 50));
        for (ShoppingBasket basket : shoppingCart.getBaskets()) {
            if (basket.getStoreId() == store1.getId()) {
                if (basket.getProducts().containsKey(4) && basket.getProducts().get(4) == 90) found = true;
            }
        }
        assertTrue(found);

        found = false;
        assertTrue(shoppingCart.addProduct(store2, 2, 10));
        for (ShoppingBasket basket : shoppingCart.getBaskets()) {
            if (basket.getStoreId() == store2.getId()) {
                if (basket.getProducts().containsKey(2) && basket.getProducts().get(2) == 10) found = true;
            }
        }
        assertTrue(found);


        assertFalse(shoppingCart.addProduct(store2, 40, 0));
        found = false;
        for (ShoppingBasket basket : shoppingCart.getBaskets()) {
            if (basket.getStoreId() == store2.getId()) {
                if (basket.getProducts().containsKey(40)) found = true;
            }
        }
        assertFalse(found);

        assertFalse(shoppingCart.addProduct(store2, 40, -5));
        found = false;
        for (ShoppingBasket basket : shoppingCart.getBaskets()) {
            if (basket.getStoreId() == store2.getId()) {
                if (basket.getProducts().containsKey(40)) found = true;
            }
        }
        assertFalse(found);

        assertFalse(shoppingCart.addProduct(null, 0, 40));

        assertFalse(shoppingCart.addProduct(store3, -1, 40));
        found = false;
        for (ShoppingBasket basket : shoppingCart.getBaskets()) {
            if (basket.getStoreId() == store3.getId()) {
                found = true;
            }
        }
        assertFalse(found);
    }

    @Test
    public void editProduct() {
        assertFalse(shoppingCart.editProduct(store1, 40, 50));
        assertFalse(shoppingCart.editProduct(null, 40, 50));

        boolean found = false;

        shoppingCart.addProduct(store1, 40, 50);
        assertFalse(shoppingCart.editProduct(store1, -1, 50));
        assertFalse(shoppingCart.editProduct(store1, 40, -1));
        assertFalse(shoppingCart.editProduct(store1, 40, 0));
        assertFalse(shoppingCart.editProduct(store1, 40, -5));

        shoppingCart.editProduct(store1, 40, 20);
        for (ShoppingBasket basket : shoppingCart.getBaskets()) {
            if (basket.getStoreId() == store1.getId()) {
                if(basket.getProducts().containsKey(40) && basket.getProducts().get(40) == 20) {
                    found = true;
                    break;
                }
            }
        }
        assertTrue(found);
    }

    @Test
    public void removeProduct() {
        assertFalse(shoppingCart.removeProductFromCart(store1, 40));
        assertFalse(shoppingCart.removeProductFromCart(null, 40));

        shoppingCart.addProduct(store1, 0, 40);
        assertTrue(shoppingCart.removeProductFromCart(store1, 0));
        boolean found = false;
        for (ShoppingBasket basket : shoppingCart.getBaskets()) {
            if (basket.getStoreId() == store1.getId()) {
                if (basket.getProducts().containsKey(0)) found = true;
            }
        }
        assertFalse(found);
    }

    @Test
    public void removeAllProducts() {
        shoppingCart.addProduct(store1, 4, 40);
        shoppingCart.removeAllProducts();
        assertEquals(0, shoppingCart.getBaskets().size());
    }
}