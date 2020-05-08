package Domain.TradingSystem;//    @Test
//    public void testAddProductFailureNonExisting(){
//        assertFalse(addProdcut(12313, Database.userToStore.get("chika"),4));
//        assertFalse(addProdcut(14252, Database.userToStore.get("chika"),4));
//    }


import java.util.List;
import java.util.Map;

interface UserState {

     boolean hasOwnerPermission(int storeId);

     boolean addPermission(Store store, Subscriber grantor, String type );


     boolean logout();

     UserPurchaseHistory getHistory();

     void setUser(User user);

     Store openStore();



     void addPurchase(Map<Store, PurchaseDetails> storePurchaseDetails);

     void removeLastHistoryItem(List<Store> stores);

     boolean isAdmin();

     UserPurchaseHistory getUserPurchaseHistory();
}
