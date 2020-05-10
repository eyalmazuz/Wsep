package Domain.TradingSystem;

import DTOs.ResultCode;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class StoreTest extends TestCase {

    Store store = new Store();
    Subscriber s = new Subscriber();
    ProductInfo bamba = new ProductInfo(1,"bamba","sanck");
    ProductInfo bisly = new ProductInfo(2,"bisly","sanck");

    @Before
    public void setUp(){
        store.addProduct(bamba,10);
        store.addOwner(s);
    }

    @After
    public void tearDown(){
        store.clean();
    }

    @Test
    public void testAddProductExistingProduct(){
        int size = store.getProducts().size();
        store.addProduct(bamba,15);
        assertEquals(size,store.getProducts().size());
        assertEquals(25,store.getProductAmount(1));
    }

    @Test
    public void testAddNewProduct(){
        int size = store.getProducts().size();
        store.addProduct(bisly,15);
        assertEquals(size+1,store.getProducts().size());
        assertEquals(15,store.getProductAmount(2));

    }

    @Test
    public void testAddNullProduct(){
        int size = store.getProducts().size();
        store.addProduct(null,15);
        assertEquals(size,store.getProducts().size());
    }

    @Test
    public void testEditExistingProduct(){
        store.editProduct(1,"Ain Kmo Bamba");
        assertEquals("Ain Kmo Bamba",store.getProductInStoreById(1).getInfo());

    }

    @Test
    public void testEditNonExistingProduct(){
        assertNotSame(store.editProduct(-1,"blabla").getResultCode(), ResultCode.SUCCESS);
    }

    @Test
    public void testEditNullDescriptionProduct(){
        assertNotSame(store.editProduct(1,null).getResultCode(), ResultCode.SUCCESS);
    }

    @Test
    public void testDeleteExistProduct(){
        int size = store.getProducts().size();
        store.deleteProduct(1);
        assertEquals(size-1,store.getProducts().size());

    }

    @Test
    public void testDeleteNonExistProduct(){
        int size = store.getProducts().size();
        assertNotSame(store.deleteProduct(-1).getResultCode(), ResultCode.SUCCESS);
        assertEquals(size,store.getProducts().size());

    }

    @Test
    public void testRemoveProductAmountToZero(){
        int size = store.getProducts().size();
        store.removeProductAmount(1,10);
        assertEquals(size-1,store.getProducts().size());

    }

    @Test
    public void testRemoveProductAmountNotEnoughAmount(){
        int size = store.getProducts().size();
        store.removeProductAmount(1,30);
        assertEquals(size,store.getProducts().size());
        assertEquals(10,store.getProductAmount(1));

    }

    @Test
    public void testRemoveProductAmountNgativeAmmount(){

        store.removeProductAmount(1,-10);
        assertEquals(10,store.getProductAmount(1));


    }

    @Test
    public void testRemovAmountSuccess(){
        store.removeProductAmount(1,3);
        assertEquals(7,store.getProductAmount(1));

    }



    @Test
    public void testSetNullBuyingPolicy(){
        BuyingPolicy bp = store.getBuyingPolicy();
        store.setBuyingPolicy(null);
        assertEquals(bp,store.getBuyingPolicy());

    }

    @Test
    public void testSetNullDiscountPolicy(){
        DiscountPolicy dp = store.getDiscountPolicy();
        store.setDiscountPolicy(null);
        assertEquals(dp,store.getDiscountPolicy());

    }

    @Test
    public void testDeleteOnlyManagerFail(){
        int size = store.getAllManagers().size();
        store.removeManger(s);
        assertEquals(size,store.getAllManagers().size());
    }

    @Test
    public void testDeleteNullManager(){
        store.removeManger(null);
        assertEquals(1,store.getAllManagers().size());

    }

    @Test
    public void deleteManagerSucess(){
        Subscriber manager = new Subscriber();
        store.addOwner(manager);
        int size = store.getAllManagers().size();
        store.removeManger(manager);
        assertEquals(size-1,store.getAllManagers().size());

    }

}
