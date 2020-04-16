package Service;


public class RealBridge implements Bridge {

    DomainController dc = new DomainController();

    public boolean setupSystem(String supplyConfig, String paymentConfig) { return false; }

    public boolean login(String username, String password) {
        return false;
    }

    public boolean register(String username, String password) {
        return false;
    }

    public String[][] getAllInfo() {
        return null;
    }

    public String[][] searchProducts(int id, String category, String keyword, Integer productRating, Integer storeRating, Integer priceFrom, Integer priceTo) {
        return null;
    }

    public boolean addToCart(int productId, Integer amount) {
        return false;
    }

    public boolean updateAmount(int productId, int amount) {
        return false;
    }

    public boolean deleteItemInCart(int productId) {
        return false;
    }

    public boolean clearCart() {
        return false;
    }

    public boolean buyCart(String[][] cart) {
        return false;
    }

    public String[][] viewCart(){
        return null;
    }

    public boolean logout(){
        return dc.logout();
    }

    public int openStore() {
        return dc.openStore();
    }

    //
    public String[][] viewPurchaseHistory(){
        String systemHistory = dc.getPurchaseHistory();
        //TODO: adapt the System history to the AT structure of history.
        return null;
    }

    public String[][] searchUserHistory(String username){ return null;}

    public String[][] searchStoreHistory(int storeId){ return null;}

    public boolean addProduct(int productId, int storeId, int amount) {
        return dc.addProduct(productId,storeId,amount) ;
    }

    public boolean editProduct(int storeId, int productId, String productInfo) {
        //TODO:Not match The usecases Doc.
        return false ;
    }

    public boolean deleteProduct(int storeId, int productId) {

        return dc.deleteProduct(storeId,productId) ;
    }
    //TODO:Match the usecases with user id's
    public boolean appointManager(int storeId, String username) {
        return false ;
    }

    public boolean appointOwner(int storeId, String username) { return false ;}

    public boolean removeManager(int storeId, String username) { return false ;}

    public boolean editManagerOptions(int storeId, int userId,int adminId, String option){
       return dc.editManagerOptions(storeId,userId,option);
    }

    public boolean updateItemDiscount(int storeId, int itemID, int discount){
        return false;
    }

    //TODO: Match to usecases
    public String[][] viewShopHistory(int storeId){ return null; }
}
