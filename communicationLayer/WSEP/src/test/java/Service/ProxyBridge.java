package Service;

public class ProxyBridge implements Bridge {
    private RealBridge rb = null;


    public boolean setupSystem(String supplyConfig, String paymentConfig,String path){
        if (rb != null){
            return rb.setupSystem(supplyConfig, paymentConfig,path);
        }
        else{
            return false;
        }
    }

    public boolean login(int sessionId, String username, String password) {
        if (rb != null) {
            return rb.login(sessionId, username, password);
        }
        else {
            return false;
        }
    }

    public int register(int sessionId, String username, String password) {
        if (rb != null) {
            return rb.register(sessionId, username, password);
        }
        else {
            return -1;
        }
    }

    public String getAllInfo(int sessionId){
        if (rb != null) {
            return rb.getAllInfo(sessionId);
        }
        else {
            return null;
        }

    }

    public String searchProducts(int sessionId, String productName, String category, String[] keywords, int productRating, int storeRating, int priceFrom, int priceTo) {
        if (rb != null) {
            return rb.searchProducts(sessionId, productName, category, keywords, productRating, storeRating, priceFrom, priceTo);
        }
        else {
           return null;

        }
    }

    public boolean addToCart(int sessionId, int storeId, int productId, Integer amount) {
        if (rb != null) {
            return rb.addToCart(sessionId, storeId, productId, amount);
        }
        else {
            return false;

        }
    }

    public boolean updateAmount(int sessionId, int storeId, int productId, int amount) {
        if (rb != null) {
            return rb.updateAmount(sessionId, storeId, productId, amount);
        }
        else {
            return false;
        }
    }

    public boolean deleteItemInCart(int sessionId, int storeId, int productId) {
        if (rb != null) {
            return rb.deleteItemInCart(sessionId, storeId, productId);
        }
        else {
            return false;
        }
    }

    public boolean clearCart(int sessionId) {
        if (rb != null) {
            return rb.clearCart(sessionId);
        }
        else {
            return false;
        }
    }

    public boolean buyCart(int sessionId, String cardNumber, String cardMonth, String cardYear, String cardHolder,
                           String cardCcv, String cardId, String buyerName, String address, String city, String country, String zip) {
        if (rb != null) {
            return rb.buyCart(sessionId, cardNumber, cardMonth, cardYear, cardHolder, cardCcv, cardId, buyerName, address, city, country, zip);
        }
        else {
            return false;
        }
    }

    public String viewCart(int sessionId){
        if (rb != null) {
            return rb.viewCart(sessionId);
        }
        else {
            return null;
        }

    }

    public boolean logout(int sessionId){
        if (rb != null){
            return rb.logout(sessionId);
        }
        else{
            return true;
        }
    }

    public int openStore(int sessionId){
        if (rb != null){
            return rb.openStore(sessionId);
        }
        else{
            return 69;
        }
    }

    public String viewPurchaseHistory(int sessionId){
        if(rb != null){
            return rb.viewPurchaseHistory(sessionId);
        }
        else{
            return null;
        }
    }

    public String searchUserHistory(int sessionId, int userId){
        if(rb != null){
            return rb.searchUserHistory(sessionId, userId);
        }
        else{
            return null;
        }
    }


    public boolean addProductToStore(boolean flag, int sessionId, int productId, int storeId, int amount){
        if(rb != null){
            return rb.addProductToStore(flag, sessionId, productId, storeId, amount);
        }
        else{
            return false;
        }
    }

    public boolean editProduct(boolean flag, int sessionId, int storeId, int productId, String productInfo){
        if(rb != null){
            return rb.editProduct(flag, sessionId, storeId, productId, productInfo);
        }
        else{
            return false;
        }
    }

    public boolean deleteProduct(boolean flag, int sessionId, int storeId, int productId){
            if(rb != null){
                return rb.deleteProduct(flag, sessionId, storeId, productId);
            }
            else{
                return false;
            }
        }

    public boolean appointManager(int sessionId, int storeId, int userId){
        if(rb != null){
            return rb.appointManager(sessionId, storeId, userId);
        }
        else{
            return false;
        }
    }

    public boolean appointOwner(int sessionId, int storeId, int userId){
        if(rb != null){
            return rb.appointOwner(sessionId, storeId, userId);
        }
        else{
            return false;
        }
    }

    public boolean removeManager(int sessionId, int storeId, int userId){
        if(rb != null){
            return rb.removeManager(sessionId, storeId, userId);
        }
        else{
            return false;
        }
    }

    public boolean editManagerOptions(int sessionId, int storeId, int userId, String option){
        if(rb != null){
            return rb.editManagerOptions(sessionId, storeId, userId, option);
        }
        else{
            return false;
        }
    }



    public String viewShopHistory(int sessionId, int storeId){
        if(rb != null){
            return rb.viewShopHistory(sessionId, storeId);
        }
        else{
            return null;
        }
    }

    public String getStoreHistory(int sessionId, int storeId){
        if(rb != null){
            return rb.getStoreHistory(sessionId, storeId);
        }
        else{
            return null;
        }
    }
    public void setRealBridge(RealBridge realBridge) {
        rb = realBridge;
    }

    public int startSession(){
        if(rb != null){
            return rb.startSession();
        }
        else{
            return 1;
        }
    }

    public void addProductInfo(int sessionId, int id, String name, String category, double basePrice){
        if(rb != null){
            rb.addProductInfo(sessionId, id, name, category, basePrice);
        }
        else{

        }
    }

    public boolean changeBuyingPolicy(int sessionId, boolean flag, int storeId, String newPolicy) {
        if(rb != null){
            return rb.changeBuyingPolicy(sessionId, flag, storeId, newPolicy);
        }
        else return false;
    }

    @Override
    public boolean removeOwner(int sessionId, int storeId, int userId) {
        if(rb != null){
            return rb.removeOwner(sessionId,storeId,userId);
        }
        else return false;
    }

}
