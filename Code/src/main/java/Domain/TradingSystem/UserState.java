package Domain.TradingSystem;

 interface UserState {


     boolean addProductToStore(Store storeId, int productId, int ammount);

     boolean editProductInStore(Store currStore, int productId, String newInfo);

     boolean deleteProductFromStore(Store currStore, int productId);


 }
