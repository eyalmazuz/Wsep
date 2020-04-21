package Domain.TradingSystem;//    @Test
//    public void testAddProductFailureNonExisting(){
//        assertFalse(addProdcut(12313, Database.userToStore.get("chika"),4));
//        assertFalse(addProdcut(14252, Database.userToStore.get("chika"),4));
//    }


import java.util.Map;

interface UserState {

     boolean addProductToStore(int storeId, int productId, int ammount);

     boolean editProductInStore(int currStore, int productId, String newInfo);

     boolean deleteProductFromStore(int currStore, int productId);

     boolean hasOwnerPermission(int storeId);

     boolean addPermission(Store store, Subscriber grantor, String type );


     boolean logout(ShoppingCart cart);

     String getHistory();

     void setUser(User user);

     Store openStore();

     boolean addOwner(Store store, Subscriber newOwner);

     boolean addManager(Store store, Subscriber newManager);

     boolean deleteManager(Store store, Subscriber managerToDelete);


     boolean editPermission(Subscriber manager, Store store, String details);

     String getStoreHistory(int storeId);

     void addPurchase(Map<Integer, PurchaseDetails> storePurchaseDetails);

     void removePurchase(Map<Integer, PurchaseDetails> storePurchaseDetails);

     boolean isAdmin();
}
