package Domain.TradingSystem;


import DTOs.ResultCode;
import DataAccess.DAOManager;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;


public class ShoppingCartTest extends TestCase {

    private User user;
    private ShoppingCart shoppingCart;
    private ShoppingCart otherShoppingCart;
    private Store store1 , store2 , store3 ;
    private ProductInfo productInfo4, productInfo2,productInfo40, productInfo5,productInfo10;
    private Map<Store, PurchaseDetails> storePurchaseDetailsMap;
    private boolean found ;
    private ProductInfo info;

    @Before
    public void setUp() {
        System.testing = true;

        user = new User();

        shoppingCart = new ShoppingCart(user);
        otherShoppingCart = new ShoppingCart(user);

        store1 = new Store();
        store2 = new Store();
        store3 = new Store();
        productInfo2 = new ProductInfo(2,"two","two", 10);
        productInfo4 = new ProductInfo(4,"four","four", 10);
        productInfo5 = new ProductInfo(5,"five","five", 10);
        productInfo10 = new ProductInfo(10,"ten","ten", 10);
        productInfo40 = new ProductInfo(40,"fourty","fourty", 10);
        found = false;
    }

    @After
    public void tearDown() {
        DAOManager.clearDatabase();
    }


    @Test
    public void testAddProductSuccess1() {
        assertSame(shoppingCart.addProduct(store1, productInfo4, 40).getResultCode(), ResultCode.SUCCESS);
    }
    @Test
    public void testAddProductSuccessFound1() {
        shoppingCart.addProduct(store1, productInfo4, 40).getResultCode();
        for (ShoppingBasket basket : shoppingCart.getBaskets()) {
            if (basket.getStoreId() == store1.getId()) {
                if (basket.getProducts().containsKey(productInfo4) && basket.getProducts().get(productInfo4) == 40)
                    found = true;
            }
        }
        assertTrue(found);
    }

    @Test
    public void testAddProductSuccess2() {
        assertSame(shoppingCart.addProduct(store1, productInfo4, 50).getResultCode(), ResultCode.SUCCESS);
    }

    @Test
    public void testAddProductSuccessFound2() {
        shoppingCart.addProduct(store1, productInfo4, 40).getResultCode();
        shoppingCart.addProduct(store1, productInfo4, 50).getResultCode();
        for (ShoppingBasket basket : shoppingCart.getBaskets()) {
            if (basket.getStoreId() == store1.getId()) {
                if (basket.getProducts().containsKey(productInfo4) && basket.getProducts().get(productInfo4) == 90)
                    found = true;
            }
        }
        assertTrue(found);
    }

    @Test
    public void testAddProductSuccess3() {
        assertSame(shoppingCart.addProduct(store2, productInfo2, 10).getResultCode(), ResultCode.SUCCESS);
    }

    @Test
    public void testAddProductSuccessFound3() {
        shoppingCart.addProduct(store2, productInfo2, 10).getResultCode();
        for (ShoppingBasket basket : shoppingCart.getBaskets()) {
            if (basket.getStoreId() == store2.getId()) {
                if (basket.getProducts().containsKey(productInfo2) && basket.getProducts().get(productInfo2) == 10)
                    found = true;
            }
        }
        assertTrue(found);
    }

    @Test
    public void testAddProductFailure4() {
        assertNotSame(shoppingCart.addProduct(store2, productInfo40, 0).getResultCode(), ResultCode.SUCCESS);
    }



    @Test
    public void testAddProductFailureNotFound4() {
        for (ShoppingBasket basket : shoppingCart.getBaskets()) {
            if (basket.getStoreId() == store2.getId()) {
                if (basket.getProducts().containsKey(productInfo40)) found = true;
            }
        }
        assertFalse(found);
    }


    @Test
    public void testAddProductFailure5() {
        assertNotSame(shoppingCart.addProduct(store2, productInfo40, -5).getResultCode(), ResultCode.SUCCESS);
    }
    @Test
    public void testAddProductFailureNotFound5() {
        for (ShoppingBasket basket : shoppingCart.getBaskets()) {
            if (basket.getStoreId() == store2.getId()) {
                if (basket.getProducts().containsKey(productInfo40)) found = true;
            }
        }
        assertFalse(found);
    }

    @Test
    public void testAddProductFailure6() {
        assertNotSame(shoppingCart.addProduct(null, productInfo2, 40).getResultCode(), ResultCode.SUCCESS);
        }

    @Test
    public void testAddProductFailure7() {
       assertNotSame(shoppingCart.addProduct(store3, null, 40).getResultCode(), ResultCode.SUCCESS);
    }


    @Test
    public void testAddProductFailureNotFound6() {

        for (ShoppingBasket basket : shoppingCart.getBaskets()) {
            if (basket.getStoreId() == store3.getId()) {
                found = true;
            }
        }
        assertFalse(found);
    }

    @Test
    public void testEditProductFailure8() {
        assertNotSame(shoppingCart.editProduct(store1, productInfo40, 50).getResultCode(), ResultCode.SUCCESS);
    }

    @Test
    public void testEditProductFailure9() {
        assertNotSame(shoppingCart.editProduct(null, productInfo40, 50).getResultCode(), ResultCode.SUCCESS);
    }

    @Test
    public void testEditProductFailureNotFound7() {

        shoppingCart.addProduct(store1, productInfo40, 50);
        assertNotSame(shoppingCart.editProduct(store1, null, 50).getResultCode(), ResultCode.SUCCESS);
   }

    @Test
    public void testEditProductFailureNotFound8() {
        assertNotSame(shoppingCart.editProduct(store1, productInfo40, -1).getResultCode(), ResultCode.SUCCESS);
    }

    @Test
    public void testEditProductFailureNotFound9() {
        assertNotSame(shoppingCart.editProduct(store1, productInfo40, 0).getResultCode(), ResultCode.SUCCESS);
    }

    @Test
    public void testEditProductFailureNotFound10() {
        assertNotSame(shoppingCart.editProduct(store1, productInfo40, -5).getResultCode(), ResultCode.SUCCESS);
    }


    @Test
    public void testEditProductSuccessFound7() {
        shoppingCart.addProduct(store1, productInfo40, 50);
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
    public void testEditProductFailure10() {
        assertNotSame(shoppingCart.removeProductFromCart(store1, productInfo40).getResultCode(), ResultCode.SUCCESS);
    }

    @Test
    public void testEditProductFailure11() {
        assertNotSame(shoppingCart.removeProductFromCart(null, productInfo40).getResultCode(), ResultCode.SUCCESS);
    }

    @Test
    public void testRemoveProductSuccess() {
        shoppingCart.addProduct(store1, productInfo2, 40);
        assertSame(shoppingCart.removeProductFromCart(store1, productInfo2).getResultCode(), ResultCode.SUCCESS);
    }
    @Test
    public void testRemoveProductFailure() {

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



    // TESTS FOR USECASE 2.8

    @Test
    public void testIsEmptySuccess() {
        assertTrue(shoppingCart.isEmpty());

    }

    @Test
    public void testIsEmptyFailureNotEmpty() {
        shoppingCart.addProduct(store1, productInfo4, 4);
        assertFalse(shoppingCart.isEmpty());

    }

    @Test
    public void testIsEmptySuccessAfterRemove() {
        shoppingCart.removeAllProducts();
        assertTrue(shoppingCart.isEmpty());
    }



    @Test
    public void testCheckBuyingPolicyFailureNotAllowed() {
        store1.setBuyingPolicy(new BuyingPolicy("No one is allowed"));
        shoppingCart.addProduct(store1, productInfo4, 4);
        assertNotSame(shoppingCart.checkBuyingPolicy().getResultCode(), ResultCode.SUCCESS);
       }

    @Test
    public void testCheckBuyingPolicySuccessAllowed() {
        store1.setBuyingPolicy(new BuyingPolicy("None"));
        assertSame(shoppingCart.checkBuyingPolicy().getResultCode(), ResultCode.SUCCESS);
    }

    @Test
    public void testCheckStoreSuppliesSuccess() {
        store1.addProduct(new ProductInfo(4, "lambda", "snacks", 10), 4);
        shoppingCart.addProduct(store1, productInfo4, 2);
        assertTrue(shoppingCart.checkStoreSupplies());

    }


    @Test
    public void testCheckStoreSuppliesFailure() {
        shoppingCart.addProduct(store1, productInfo5, 50);
        assertFalse(shoppingCart.checkStoreSupplies());
    }

    @Test
    public void testSaveAndGetStorePurchaseDetailsSuccessInfoSize() {
        info = new ProductInfo(4, "lambda", "snacks", 10);
        store1.addProduct(info, 6);
        shoppingCart.addProduct(store1, info, 5);
        assertEquals(store1.getStorePurchaseHistory().size(), 1);
    }

    @Test
    public void testSaveAndGetStorePurchaseDetailsSuccessInfoContent() {
        info = new ProductInfo(4, "lambda", "snacks", 10);
        store1.addProduct(info, 6);
        shoppingCart.addProduct(store1, info, 5);
        assertEquals((int)store1.getStorePurchaseHistory().get(0).getProducts().get(info), 5);
    }


    @Test
    public void testSaveAndGetStorePurchaseDetailsSuccessGetStore() {
        info = new ProductInfo(4, "lambda", "snacks", 10);
        store1.addProduct(info, 6);
        shoppingCart.addProduct(store1, info, 5);
        Map<Store, PurchaseDetails> storePurchaseDetailsMap = shoppingCart.saveAndGetStorePurchaseDetails();
        assertNotNull(storePurchaseDetailsMap.get(store1));
       }

    @Test
    public void testSaveAndGetStorePurchaseDetailsSuccessSaveAndGet() {
        info = new ProductInfo(4, "lambda", "snacks", 10);
        store1.addProduct(info, 6);
        shoppingCart.addProduct(store1, info, 5);
        Map<Store, PurchaseDetails> storePurchaseDetailsMap = shoppingCart.saveAndGetStorePurchaseDetails();
        assertEquals((int)storePurchaseDetailsMap.get(store1).getProducts().get(info), 5);
    }

}