package Service;



public class RealBridge implements Bridge {

    DomainController dc = new DomainController();

    public boolean setupSystem(String supplyConfig, String paymentConfig) {
        return dc.setup(supplyConfig, paymentConfig); }

    public boolean login(int sessionId, String username, String password) {
        GuestUserHandler guh = new GuestUserHandler();
        return guh.login(sessionId, username, password);
    }

    public int register(int sessionId, String username, String password) {
        GuestUserHandler guh = new GuestUserHandler();
        return guh.register(sessionId, username, password);
    }

    public String getAllInfo(int sessionId) {
        GuestUserHandler guh = new GuestUserHandler();
        return guh.viewStoreProductInfo();
    }

    public String searchProducts(int sessionId, String productName, String category, String keyword, int productRating, int storeRating, int priceFrom, int priceTo) {
        GuestUserHandler guh = new GuestUserHandler();
        return guh.searchProducts(sessionId, productName, category, new String[]{keyword}, productRating, storeRating);
    }

    public boolean addToCart(int sessionId, int storeId, int productId, Integer amount) {
        GuestUserHandler guh = new GuestUserHandler();
        return guh.addProductToCart(sessionId, storeId, productId, amount);
    }

    public boolean updateAmount(int sessionId, int storeId, int productId, int amount) {
        GuestUserHandler guh = new GuestUserHandler();
        return guh.editProductInCart(sessionId, storeId, productId, amount);
    }

    public boolean deleteItemInCart(int sessionId, int storeId, int productId) {
        GuestUserHandler guh = new GuestUserHandler();
        return guh.removeProductInCart(sessionId, storeId, productId);
    }

    public boolean clearCart(int sessionId) {
        GuestUserHandler guh = new GuestUserHandler();
        return guh.clearCart(sessionId);
    }

    public boolean buyCart(int sessionId) {
        GuestUserHandler guh = new GuestUserHandler();
        return guh.purchaseCart(sessionId);
    }

    //TODO
    public String viewCart(int sessionId){
        return dc.viewCart(sessionId);
    }

    public boolean logout(int sessionId){
        SubscriberStateHandler ssh = new SubscriberStateHandler(sessionId);
        return ssh.logout();
    }

    public int openStore(int sessionId) {
        SubscriberStateHandler ssh = new SubscriberStateHandler(sessionId);
        return ssh.openStore();
    }

    public String viewPurchaseHistory(int sessionId){
        SubscriberStateHandler ssh = new SubscriberStateHandler(sessionId);
        return ssh.getHistory();
    }

    public String searchUserHistory(int sessionId, int userId){
        AdminStateHandler ash = new AdminStateHandler(sessionId);
        return ash.getSubscriberHistory(userId);
    }

    //TODO FIX THIS
    public String searchStoreHistory(int sessionId, int storeId){
        return "";
    }

    public boolean addProduct(boolean flag, int sessionId, int productId, int storeId, int amount) {
        if(flag) {
            OwnerHandler oh = new OwnerHandler(sessionId);
            return oh.addProductToStore(storeId, productId, amount);
        }
        else{
            ManagerHandler mh = new ManagerHandler(sessionId);
            return mh.addProductToStore(storeId, productId, amount);
        }
    }

    public boolean editProduct(boolean flag, int sessionId, int storeId, int productId, String productInfo) {
        if(flag) {
            OwnerHandler oh = new OwnerHandler(sessionId);
            return oh.editProductToStore(storeId, productId, productInfo);
        }
        else{
            ManagerHandler mh = new ManagerHandler(sessionId);
            return mh.editProductToStore(storeId, productId, productInfo);
        }
    }

    public boolean deleteProduct(boolean flag, int sessionId, int storeId, int productId) {
        if(flag) {
            OwnerHandler oh = new OwnerHandler(sessionId);
            return oh.deleteProductFromStore(storeId, productId);
        }
        else{
            ManagerHandler mh = new ManagerHandler(sessionId);
            return mh.deleteProductFromStore(storeId, productId);
        }
    }

    public boolean appointManager(int sessionId, int storeId, int userId) {
        OwnerHandler oh = new OwnerHandler(sessionId);
        return oh.addStoreManager(storeId, userId) ;
    }

    public boolean appointOwner(int sessionId, int storeId, int userId) {
        OwnerHandler oh = new OwnerHandler(sessionId);
        return oh.addStoreOwner(storeId, userId);
    }

    public boolean removeManager(int sessionId, int storeId, int userId) {
        OwnerHandler oh = new OwnerHandler(sessionId);
        return oh.deleteManager(storeId, userId);
    }

    public boolean editManagerOptions(int sessionId, int storeId, int userId, String option){
        OwnerHandler oh = new OwnerHandler(sessionId);
        return oh.editManageOptions(storeId, userId, option);
    }

    public String viewShopHistory(int sessionId, int storeId){
        OwnerHandler oh = new OwnerHandler(sessionId);
        return oh.viewPurchaseHistory(storeId);
    }

    public String getStoreHistory(int sessionId, int storeId) {
        AdminStateHandler ash = new AdminStateHandler(sessionId);
        return ash.getStoreHistory(storeId);
    }

    public int startSession() { return dc.startSession(); }

}
