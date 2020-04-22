package Service;


import Domain.TradingSystem.Pair;

public class RealBridge implements Bridge {

    DomainController dc = new DomainController();

    public boolean setupSystem(String supplyConfig, String paymentConfig) {
        return dc.setup(supplyConfig, paymentConfig); }

    public boolean login(int sessionId, String username, String password) {

        return dc.login(sessionId, username, password);
    }

    public int register(int sessionId, String username, String password) {
        return dc.register(sessionId, username, password);
    }

    public String getAllInfo(int sessionId) {

        return dc.getAllInfo();
    }

    public String searchProducts(int sessionId, int id, String category, String keyword, int productRating, int storeRating, int priceFrom, int priceTo) {
        Pair<Integer, Integer> priceRange = new Pair<>(priceFrom, priceTo);
        return dc.searchProducts(sessionId, id, category, keyword, priceRange, productRating, storeRating);
    }

    public boolean addToCart(int sessionId, int storeId, int productId, Integer amount) {
        return dc.addToCart(sessionId, storeId, productId, amount);
    }

    public boolean updateAmount(int sessionId, int storeId, int productId, int amount) {
        return dc.updateAmount(sessionId, storeId, productId, amount);
    }

    public boolean deleteItemInCart(int sessionId, int storeId, int productId) {

        return dc.deleteItemInCart(sessionId, storeId, productId);
    }

    public boolean clearCart(int sessionId) {
        return dc.clearCart(sessionId);
    }

    public boolean buyCart(int sessionId) {
        return dc.buyCart(sessionId);
    }

    public String viewCart(int sessionId){
        return dc.viewCart(sessionId);
    }

    public boolean logout(int sessionId){
        return dc.logout(sessionId);
    }

    public int openStore(int sessionId) {
        return dc.openStore(sessionId);
    }

    public String viewPurchaseHistory(int sessionId){
        return dc.getPurchaseHistory(sessionId);
    }

    public String searchUserHistory(int sessionId, int userId){
        return dc.viewUserHistory(sessionId, userId);}

    public String searchStoreHistory(int sessionId, int storeId){ return dc.viewShopHistory(sessionId, storeId);}

    public boolean addProduct(int sessionId, int productId, int storeId, int amount) {
        return dc.addProduct(sessionId, productId,storeId,amount) ;
    }

    public boolean editProduct(int sessionId, int storeId, int productId, String productInfo) {
        return dc.editProduct(sessionId, storeId, productId, productInfo) ;
    }

    public boolean deleteProduct(int sessionId, int storeId, int productId) {

        return dc.deleteProduct(sessionId, storeId,productId) ;
    }

    public boolean appointManager(int sessionId, int storeId, int userId) {
        return dc.appointManager(sessionId, storeId, userId) ;
    }

    public boolean appointOwner(int sessionId, int storeId, int userId) { return dc.appointOwner(sessionId, storeId, userId);}

    public boolean removeManager(int sessionId, int storeId, int userId) { return dc.removeManager(sessionId, storeId, userId);}

    public boolean editManagerOptions(int sessionId, int storeId, int userId, String option){
       return dc.editManagerOptions(sessionId, storeId,userId,option);
    }

    public String viewShopHistory(int sessionId, int storeId){ return dc.viewShopHistory(sessionId, storeId); }

    public String getStoreHistory(int sessionId, int storeId) { return dc.getStoryHistory(sessionId, storeId); }

    public int startSession() { return dc.startSession(); }

}
