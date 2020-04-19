package Service;


import Domain.TradingSystem.Pair;

public class RealBridge implements Bridge {

    DomainController dc = new DomainController();

    public boolean setupSystem(String supplyConfig, String paymentConfig) {
        return dc.setup(supplyConfig, paymentConfig); }

    public boolean login(String username, String password) {

        return dc.login(username, password);
    }

    public int register(String username, String password) {
        return dc.register(username, password);
    }

    public String[][] getAllInfo() {

        return dc.getAllInfo();
    }

    public String[][] searchProducts(int id, String category, String keyword, Integer productRating, Integer storeRating, Integer priceFrom, Integer priceTo) {
        Pair<Integer, Integer> priceRange = new Pair<>(priceFrom, priceTo);
        return dc.searchProducts(id, category, keyword, priceRange, productRating, storeRating);
    }

    public void addToCart(int storeId, int productId, Integer amount) {
        dc.addToCart(storeId, productId, amount);
    }

    public void updateAmount(int storeId, int productId, int amount) {
        dc.updateAmount(storeId, productId, amount);
    }

    public void deleteItemInCart(int storeId, int productId) {

        dc.deleteItemInCart(storeId, productId);
    }

    public void clearCart() {
        dc.clearCart();
    }

    //TODO FIX THIS
    public void buyCart(String[][] cart) {
        //TODO add ADAPTER
        dc.buyCart();
    }

    //TODO FIX THIS
    public String[][] viewCart(){
        //TODO add ADAPTER
        return dc.viewCart();
    }

    public boolean logout(){
        return dc.logout();
    }

    public int openStore() {
        return dc.openStore();
    }

    //TODO FIX THIS
    public String[][] viewPurchaseHistory(){
        String systemHistory = dc.getPurchaseHistory();
        //TODO: adapt the System history to the AT structure of history.
        return null;
    }
    //TODO FIX THIS
    public String[][] searchUserHistory(int userId){ return dc.viewUserHistory(userId);}

    //TODO FIX THIS
    public String[][] searchStoreHistory(int storeId){ return dc.viewShopHistory(storeId);}

    public boolean addProduct(int productId, int storeId, int amount) {
        return dc.addProduct(productId,storeId,amount) ;
    }

    public boolean editProduct(int storeId, int productId, String productInfo) {
        return dc.editProdct(storeId, productId, productInfo) ;
    }

    public boolean deleteProduct(int storeId, int productId) {

        return dc.deleteProduct(storeId,productId) ;
    }
    //TODO:Match the usecases with user id's
    public boolean appointManager(int storeId, int userId) {
        return dc.appointManager(storeId, userId) ;
    }

    public boolean appointOwner(int storeId, int userId) { return dc.appointOwner(storeId, userId);}

    public boolean removeManager(int storeId, int userId) { return dc.removeManager(storeId, userId);}

    public boolean editManagerOptions(int storeId, int userId, String option){
       return dc.editManagerOptions(storeId,userId,option);
    }

    public boolean updateItemDiscount(int storeId, int itemID, int discount){
        return dc.updateItemDiscount(storeId, itemID, discount);
    }

    public String[][] viewShopHistory(int storeId){ return dc.viewShopHistory(storeId); }
}
