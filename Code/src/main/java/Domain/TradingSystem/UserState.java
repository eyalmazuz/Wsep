package Domain.TradingSystem;

 interface UserState {

     boolean addProductToStore(int storeId, int productId, int ammount);

     boolean editProductInStore(int currStore, int productId, String newInfo);

     boolean deleteProductFromStore(int currStore, int productId);

     boolean hasOwnerPermission(int storeId);

     boolean addPermission(Store store, Subscriber grantor, String type );


     boolean logout(ShoppingCart cart);

     Store openStore();

     boolean addOwner(Store store, Subscriber newOwner);
 }
