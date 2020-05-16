package Domain.TradingSystem;


import DTOs.ResultCode;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;


public class ShoppingCartTest extends TestCase {

    private User user;
    private ShoppingCart shoppingCart;
    private ShoppingCart otherShoppingCart;
    private Store store1, store2, store3;
    private ProductInfo productInfo4, productInfo2,productInfo40, productInfo5,productInfo10;

    @Before
    public void setUp() {
        user = new User();

        shoppingCart = new ShoppingCart(user);
        otherShoppingCart = new ShoppingCart(user);

        store1 = new Store();
        store2 = new Store();
        store3 = new Store();

        productInfo2 = new ProductInfo(2,"two","two");
        productInfo4 = new ProductInfo(4,"four","four");
        productInfo5 = new ProductInfo(5,"five","five");
        productInfo10 = new ProductInfo(10,"ten","ten");
        productInfo40 = new ProductInfo(40,"fourty","fourty");
    }

    @Test
    public void testAddProduct() {
        assertSame(shoppingCart.addProduct(store1, productInfo4, 40).getResultCode(), ResultCode.SUCCESS);
        boolean found = false;
        for (ShoppingBasket basket : shoppingCart.getBaskets()) {
            if (basket.getStoreId() == store1.getId()) {
                if (basket.getProducts().containsKey(productInfo4) && basket.getProducts().get(productInfo4) == 40) found = true;
            }
        }
        assertTrue(found);

        found = false;
        assertSame(shoppingCart.addProduct(store1, productInfo4, 50).getResultCode(), ResultCode.SUCCESS);
        for (ShoppingBasket basket : shoppingCart.getBaskets()) {
            if (basket.getStoreId() == store1.getId()) {
                if (basket.getProducts().containsKey(productInfo4) && basket.getProducts().get(productInfo4) == 90) found = true;
            }
        }
        assertTrue(found);

        found = false;
        assertSame(shoppingCart.addProduct(store2, productInfo2, 10).getResultCode(), ResultCode.SUCCESS);
        for (ShoppingBasket basket : shoppingCart.getBaskets()) {
            if (basket.getStoreId() == store2.getId()) {
                if (basket.getProducts().containsKey(productInfo2) && basket.getProducts().get(productInfo2) == 10) found = true;
            }
        }
        assertTrue(found);


        assertNotSame(shoppingCart.addProduct(store2, productInfo40, 0).getResultCode(), ResultCode.SUCCESS);
        found = false;
        for (ShoppingBasket basket : shoppingCart.getBaskets()) {
            if (basket.getStoreId() == store2.getId()) {
                if (basket.getProducts().containsKey(productInfo40)) found = true;
            }
        }
        assertFalse(found);

        assertNotSame(shoppingCart.addProduct(store2, productInfo40, -5).getResultCode(), ResultCode.SUCCESS);
        found = false;
        for (ShoppingBasket basket : shoppingCart.getBaskets()) {
            if (basket.getStoreId() == store2.getId()) {
                if (basket.getProducts().containsKey(productInfo40)) found = true;
            }
        }
        assertFalse(found);

        assertNotSame(shoppingCart.addProduct(null, productInfo2, 40).getResultCode(), ResultCode.SUCCESS);

        assertNotSame(shoppingCart.addProduct(store3, null, 40).getResultCode(), ResultCode.SUCCESS);
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
        assertNotSame(shoppingCart.editProduct(store1, productInfo40, 50).getResultCode(), ResultCode.SUCCESS);
        assertNotSame(shoppingCart.editProduct(null, productInfo40, 50).getResultCode(), ResultCode.SUCCESS);

        boolean found = false;

        shoppingCart.addProduct(store1, productInfo40, 50);
        assertNotSame(shoppingCart.editProduct(store1, null, 50).getResultCode(), ResultCode.SUCCESS);
        assertNotSame(shoppingCart.editProduct(store1, productInfo40, -1).getResultCode(), ResultCode.SUCCESS);
        assertNotSame(shoppingCart.editProduct(store1, productInfo40, 0).getResultCode(), ResultCode.SUCCESS);
        assertNotSame(shoppingCart.editProduct(store1, productInfo40, -5).getResultCode(), ResultCode.SUCCESS);

        shoppingCart.editProduct(store1, productInfo40, 20);
        for (ShoppingBasket basket : shoppingCart.getBaskets()) {
            if (basket.getStoreId() == store1.getId()) {
                if(basket.getProducts().containsKey(productInfo40) && basket.getProducts().get(productInfo40) == 20) {
                    found = true;
                    break;
                }
            }
        }
        assertTrue(found);
    }

    @Test
    public void testRemoveProduct() {
        assertNotSame(shoppingCart.removeProductFromCart(store1, productInfo40).getResultCode(), ResultCode.SUCCESS);
        assertNotSame(shoppingCart.removeProductFromCart(null, productInfo40).getResultCode(), ResultCode.SUCCESS);

        shoppingCart.addProduct(store1, productInfo2, 40);
        assertSame(shoppingCart.removeProductFromCart(store1, productInfo2).getResultCode(), ResultCode.SUCCESS);
        boolean found = false;
        for (ShoppingBasket basket : shoppingCart.getBaskets()) {
            if (basket.getStoreId() == store1.getId()) {
                if (basket.getProducts().containsKey(productInfo2)) found = true;
            }
        }
        assertFalse(found);
    }

    @Test
    public void testRemoveAllProducts() {
        shoppingCart.addProduct(store1, productInfo4, 40);
        shoppingCart.removeAllProducts();
        assertEquals(0, shoppingCart.getBaskets().size());
    }

    @Test
    public void testMerge() {
        shoppingCart.addProduct(store1, productInfo4, 50);
        shoppingCart.addProduct(store2, productInfo4, 40);
        shoppingCart.addProduct(store2, productInfo5, 60);
        otherShoppingCart.addProduct(store1, productInfo5, 50);
        otherShoppingCart.addProduct(store1, productInfo4, 5);
        otherShoppingCart.addProduct(store2, productInfo5, 10);
        otherShoppingCart.addProduct(store3, productInfo10, 50);
        shoppingCart.merge(otherShoppingCart);
        boolean wrong = false;
        boolean store3Found = false;
        for (ShoppingBasket basket : shoppingCart.getBaskets()) {
            if (basket.getStoreId() == store1.getId()) {
                if (!basket.getProducts().containsKey(productInfo5) || basket.getProducts().get(productInfo5) != 50
                    || !basket.getProducts().containsKey(productInfo4) || basket.getProducts().get(productInfo4) != 55) {
                    wrong = true;
                    break;
                }
            } else if (basket.getStoreId() == store2.getId()) {
                if (!basket.getProducts().containsKey(productInfo4) || basket.getProducts().get(productInfo4) != 40 || !basket.getProducts().containsKey(productInfo5)
                    || basket.getProducts().get(productInfo5) != 70) {
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
    public void testIsEmpty() {
        assertTrue(shoppingCart.isEmpty());
        shoppingCart.addProduct(store1, productInfo4, 4);
        assertFalse(shoppingCart.isEmpty());
        shoppingCart.removeAllProducts();
        assertTrue(shoppingCart.isEmpty());
    }

    @Test
    public void testCheckBuyingPolicy() {
        store1.setBuyingPolicy(new BuyingPolicy("No one is allowed"));
        shoppingCart.addProduct(store1, productInfo4, 4);
        assertNotSame(shoppingCart.checkBuyingPolicy().getResultCode(), ResultCode.SUCCESS);

        store1.setBuyingPolicy(new BuyingPolicy("None"));
        assertSame(shoppingCart.checkBuyingPolicy().getResultCode(), ResultCode.SUCCESS);
    }

    @Test
    public void testCheckStoreSupplies() {
        store1.addProduct(new ProductInfo(4, "lambda", "snacks"), 4);
        shoppingCart.addProduct(store1, productInfo4, 2);
        assertTrue(shoppingCart.checkStoreSupplies());
        shoppingCart.addProduct(store1, productInfo5, 50);
        assertFalse(shoppingCart.checkStoreSupplies());
    }

    @Test
    public void testSaveAndGetStorePurchaseDetails() {
        ProductInfo info = new ProductInfo(4, "lambda", "snacks");
        store1.addProduct(info, 6);
        shoppingCart.addProduct(store1, info, 5);
        Map<Store, PurchaseDetails> storePurchaseDetailsMap = shoppingCart.saveAndGetStorePurchaseDetails();
        assertEquals(store1.getStorePurchaseHistory().size(), 1);
        assertEquals((int)store1.getStorePurchaseHistory().get(0).getProducts().get(info), 5);
        assertNotNull(storePurchaseDetailsMap.get(store1));
        assertEquals((int)storePurchaseDetailsMap.get(store1).getProducts().get(info), 5);
    }
}