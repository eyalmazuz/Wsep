package Domain.TradingSystem;

 interface UserState {

     boolean addProductToStore(int storeId, int productId, int ammount);

     boolean editProductInStore(int currStore, int productId, String newInfo);

     boolean deleteProductFromStore(int currStore, int productId);

     boolean hasOwnerPermission();

     void addPermission(Store store,User user, User grantor, String type );


 }
