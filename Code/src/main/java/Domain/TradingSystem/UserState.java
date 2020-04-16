package Domain.TradingSystem;

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
 }
