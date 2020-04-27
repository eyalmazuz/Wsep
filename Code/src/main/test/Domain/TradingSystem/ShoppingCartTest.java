package Domain.TradingSystem;


import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;


import java.util.Map;




public class ShoppingCartTest extends TestCase {

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
    public void testAddProduct() {
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
    public void testEditProduct() {
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
    public void testRemoveProduct() {
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
    public void testRemoveAllProducts() {
        shoppingCart.addProduct(store1, 4, 40);
        shoppingCart.removeAllProducts();
        assertEquals(0, shoppingCart.getBaskets().size());
    }

    @Test
    public void testMerge() {
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


    // TESTS FOR USECASE 2.8

    @Test
    public void testSavePurchase() {
        user.setShoppingCart(shoppingCart);
        shoppingCart.addProduct(store1, 0, 15);
        shoppingCart.savePurchase();
        StorePurchaseHistory storePurchaseHistory = store1.getStorePurchaseHistory();
        boolean found = false;
        for (PurchaseDetails details : storePurchaseHistory.getPurchaseHistory()) {
            if (details.getProductAmounts().containsKey(0) && details.getProductAmounts().get(0) == 15 && details.getUser().equals(user)) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @Test
    public void testCancelPurchase() {
        user.setShoppingCart(shoppingCart);
        shoppingCart.addProduct(store1, 0, 15);
        Map<Integer, PurchaseDetails> storePurchaseDetails = shoppingCart.savePurchase();
        shoppingCart.cancelPurchase(storePurchaseDetails);

        StorePurchaseHistory storePurchaseHistory = store1.getStorePurchaseHistory();
        assertTrue(storePurchaseHistory.getPurchaseHistory().isEmpty());
    }

    @Test
    public void testAttemptPurchase() {
        user.setShoppingCart(shoppingCart);
        System.getInstance().setup("Mock Config", "Mock Config");
        assertFalse(shoppingCart.attemptPurchase() > -1); // cant purchase empty cart
        shoppingCart.addProduct(store1, 0, 15);
        store1.setBuyingPolicy(new BuyingPolicy("No one is allowed"));
        assertFalse(shoppingCart.attemptPurchase() > -1);
        store1.setBuyingPolicy(new BuyingPolicy("None"));
        assertTrue(shoppingCart.attemptPurchase() > -1);
    }
}