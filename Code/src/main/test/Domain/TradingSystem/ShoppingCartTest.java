package Domain.TradingSystem;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.*;


public class ShoppingCartTest {

    private User user;
    private ShoppingCart shoppingCart;
    private ShoppingCart otherShoppingCart;
    private Store store1, store2, store3;

    @Before
    public void setUp() {
        user = new User();
        shoppingCart = new ShoppingCart(user);
        otherShoppingCart = new ShoppingCart(user);
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

    @Test
    public void merge() {
        shoppingCart.addProduct(store1, 4, 50);
        shoppingCart.addProduct(store2, 4, 40);
        shoppingCart.addProduct(store2, 5, 60);
        otherShoppingCart.addProduct(store1, 5, 50);
        otherShoppingCart.addProduct(store1, 4, 5);
        otherShoppingCart.addProduct(store2, 5, 10);
        otherShoppingCart.addProduct(store3, 10, 50);
        shoppingCart.merge(otherShoppingCart);
        boolean wrong = false;
        boolean store3Found = false;
        for (ShoppingBasket basket : shoppingCart.getBaskets()) {
            if (basket.getStoreId() == store1.getId()) {
                if (!basket.getProducts().containsKey(5) || basket.getProducts().get(5) != 50
                    || !basket.getProducts().containsKey(4) || basket.getProducts().get(4) != 55) {
                    wrong = true;
                    break;
                }
            } else if (basket.getStoreId() == store2.getId()) {
                if (!basket.getProducts().containsKey(4) || basket.getProducts().get(4) != 40 || !basket.getProducts().containsKey(5)
                    || basket.getProducts().get(5) != 70) {
                    wrong = true;
                    break;
                }
            } else if (basket.getStoreId() == store3.getId()) store3Found = true;
        }
        assertFalse(wrong);
        assertTrue(store3Found);
    }

    @Test
    public void savePurchase() {
        user.setShoppingCart(shoppingCart);
        shoppingCart.addProduct(store1, 0, 15);
        shoppingCart.savePurchase();
        History history = store1.getHistory();
        boolean found = false;
        for (PurchaseDetails details : history.getPurchaseHistory()) {
            if (details.getProducts().containsKey(0) && details.getProducts().get(0) == 15 && details.getUser().equals(user)) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @Test
    public void cancelPurchase() {
        user.setShoppingCart(shoppingCart);
        shoppingCart.addProduct(store1, 0, 15);
        Map<Integer, PurchaseDetails> storePurchaseDetails = shoppingCart.savePurchase();
        shoppingCart.cancelPurchase(storePurchaseDetails);

        History history = store1.getHistory();
        assertTrue(history.getPurchaseHistory().isEmpty());
    }

    @Test
    public void attemptPurchase() {
        user.setShoppingCart(shoppingCart);
        System.getInstance().setPayment("Mock Config");
        System.getInstance().setSupply("Mock Config");
        assertFalse(shoppingCart.attemptPurchase()); // cant purchase empty cart
        shoppingCart.addProduct(store1, 0, 15);
        assertTrue(shoppingCart.attemptPurchase());
    }
}