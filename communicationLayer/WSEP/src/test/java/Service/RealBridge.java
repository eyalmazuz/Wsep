package Service;


import DTOs.ResultCode;
import DTOs.StorePurchaseHistoryDTO;
import DTOs.UserPurchaseHistoryDTO;

public class RealBridge implements Bridge {

    public boolean setupSystem(String supplyConfig, String paymentConfig,String path) {
        SessionHandler dc = new SessionHandler();
        return dc.setup(supplyConfig, paymentConfig,path).getResultCode() == ResultCode.SUCCESS;
    }

    public boolean login(int sessionId, String username, String password) {
        GuestUserHandler guh = new GuestUserHandler();
        return guh.login(sessionId, username, password).getResultCode() == ResultCode.SUCCESS;
    }

    public int register(int sessionId, String username, String password) {
        GuestUserHandler guh = new GuestUserHandler();
        return guh.register(sessionId, username, password).getId();
    }

    public String getAllInfo(int sessionId) {
        GuestUserHandler guh = new GuestUserHandler();
        return guh.viewStoreProductInfo().toString();
    }

    public String searchProducts(int sessionId, String productName, String category, String[] keywords, int productRating, int storeRating, int priceFrom, int priceTo) {
        GuestUserHandler guh = new GuestUserHandler();
        return guh.searchProducts(sessionId, productName, category, keywords, productRating, storeRating).toString();


    }

    public boolean addToCart(int sessionId, int storeId, int productId, Integer amount) {
        GuestUserHandler guh = new GuestUserHandler();
        return guh.addProductToCart(sessionId, storeId, productId, amount).getResultCode() == ResultCode.SUCCESS;
    }

    public boolean updateAmount(int sessionId, int storeId, int productId, int amount) {
        GuestUserHandler guh = new GuestUserHandler();
        return guh.editProductInCart(sessionId, storeId, productId, amount).getResultCode() == ResultCode.SUCCESS;
    }

    public boolean deleteItemInCart(int sessionId, int storeId, int productId) {
        GuestUserHandler guh = new GuestUserHandler();
        return guh.removeProductInCart(sessionId, storeId, productId).getResultCode() == ResultCode.SUCCESS;
    }

    public boolean clearCart(int sessionId) {
        GuestUserHandler guh = new GuestUserHandler();
        return guh.clearCart(sessionId).getResultCode() == ResultCode.SUCCESS;
    }

    public boolean buyCart(int sessionId, String cardNumber, String cardMonth, String cardYear, String cardHolder,
                           String cardCcv, String cardId, String buyerName, String address, String city, String country, String zip) {
        GuestUserHandler guh = new GuestUserHandler();
        if (guh.requestPurchase(sessionId).getResultCode() == ResultCode.SUCCESS) {
            return guh.confirmPurchase(sessionId, cardNumber, cardMonth, cardYear, cardHolder, cardCcv,
                    cardId, buyerName, address, city, country, zip).getResultCode() == ResultCode.SUCCESS;
        }

        return false;
    }

    public String viewCart(int sessionId){
        GuestUserHandler guh = new GuestUserHandler();
        return guh.viewCart(sessionId).toString();
    }

    public boolean logout(int sessionId){
        SubscriberStateHandler ssh = new SubscriberStateHandler(sessionId);
        return ssh.logout().getResultCode() == ResultCode.SUCCESS;

    }

    public int openStore(int sessionId) {
        SubscriberStateHandler ssh = new SubscriberStateHandler(sessionId);
        return ssh.openStore().getId();
    }

    public String viewPurchaseHistory(int sessionId){
        SubscriberStateHandler ssh = new SubscriberStateHandler(sessionId);
        return ssh.getHistory().toString();
    }

    public String searchUserHistory(int sessionId, int userId){
        AdminStateHandler ash = new AdminStateHandler(sessionId);
        UserPurchaseHistoryDTO history = ash.getSubscriberHistory(userId);
        if(history.getResultCode().equals(ResultCode.SUCCESS))
            return ash.getSubscriberHistory(userId).toString();
        else
            return null;
    }

    public boolean addProductToStore(boolean flag, int sessionId, int productId, int storeId, int amount) {
        if(flag) {
            OwnerHandler oh = new OwnerHandler(sessionId);
            return oh.addProductToStore(storeId, productId, amount).getResultCode() == ResultCode.SUCCESS;
        }
        else{
            ManagerHandler mh = new ManagerHandler(sessionId);
            return mh.addProductToStore(storeId, productId, amount).getResultCode() == ResultCode.SUCCESS;
        }
    }

    public boolean editProduct(boolean flag, int sessionId, int storeId, int productId, String productInfo) {
        if(flag) {
            OwnerHandler oh = new OwnerHandler(sessionId);
            return oh.editProductToStore(storeId, productId, productInfo).getResultCode() == ResultCode.SUCCESS;
        }
        else{
            ManagerHandler mh = new ManagerHandler(sessionId);
            return mh.editProductToStore(storeId, productId, productInfo).getResultCode() == ResultCode.SUCCESS;
        }
    }

    public boolean deleteProduct(boolean flag, int sessionId, int storeId, int productId) {
        if(flag) {
            OwnerHandler oh = new OwnerHandler(sessionId);
            return oh.deleteProductFromStore(storeId, productId).getResultCode() == ResultCode.SUCCESS;
        }
        else{
            ManagerHandler mh = new ManagerHandler(sessionId);
            return mh.deleteProductFromStore(storeId, productId).getResultCode() == ResultCode.SUCCESS;
        }
    }

    public boolean appointManager(int sessionId, int storeId, int userId) {
        OwnerHandler oh = new OwnerHandler(sessionId);
        return oh.addStoreManager(storeId, userId).getResultCode() == ResultCode.SUCCESS;
    }

    public boolean appointOwner(int sessionId, int storeId, int userId) {
        OwnerHandler oh = new OwnerHandler(sessionId);
        return oh.addStoreOwner(storeId, userId).getResultCode() == ResultCode.SUCCESS;
    }

    public boolean removeManager(int sessionId, int storeId, int userId) {
        OwnerHandler oh = new OwnerHandler(sessionId);
        return oh.deleteManager(storeId, userId).getResultCode() == ResultCode.SUCCESS;
    }

    public boolean editManagerOptions(int sessionId, int storeId, int userId, String option){
        OwnerHandler oh = new OwnerHandler(sessionId);
        return oh.editManageOptions(storeId, userId, option).getResultCode() == ResultCode.SUCCESS;
    }

    public String viewShopHistory(int sessionId, int storeId){
        OwnerHandler oh = new OwnerHandler(sessionId);
        return oh.viewPurchaseHistory(storeId).toString();
    }

    public String getStoreHistory(int sessionId, int storeId) {
        AdminStateHandler ash = new AdminStateHandler(sessionId);
        StorePurchaseHistoryDTO historyDTO = ash.getStoreHistory(storeId);
        if(historyDTO.getResultCode().equals(ResultCode.SUCCESS)){
            return historyDTO.toString();
        }
        return null;
    }

    public void addProductInfo(int sessionId, int id, String name, String category, double basePrice){
        AdminStateHandler ash = new AdminStateHandler(sessionId);
        ash.addProductInfo(id, name, category, basePrice);
    }

    public int startSession() {
        SessionHandler dc = new SessionHandler();
        return dc.startSession().getId();
    }

    public boolean changeBuyingPolicy(int sessionId, boolean flag, int storeId, String newPolicy){

        if(flag) {
            OwnerHandler oh = new OwnerHandler(sessionId);
            return oh.changeBuyingPolicy(storeId, newPolicy).getResultCode() == ResultCode.SUCCESS;
        }
        else{
            ManagerHandler mh = new ManagerHandler(sessionId);
            return mh.changeBuyingPolicy(storeId, newPolicy).getResultCode() == ResultCode.SUCCESS;
        }
    }

    @Override
    public boolean removeOwner(int sessionId, int storeId, int userId) {
        OwnerHandler oh = new OwnerHandler(sessionId);
        return oh.deleteOwner(storeId,userId).getResultCode() == ResultCode.SUCCESS;
    }

}
