package Service;

public class ProxyBridge implements Bridge {
    private RealBridge rb = null;


    public boolean setupSystem(String supplyConfig, String paymentConfig){
        if (rb != null){
            return rb.setupSystem(supplyConfig, paymentConfig);
        }
        else{
            return false;
        }
    }

    public boolean login(String username, String password) {
        if (rb != null) {
            return rb.login(username, password);
        }
        else {
            return false;
        }
    }

    public boolean register(String username, String password) {
        if (rb != null) {
            return rb.register(username, password);
        }
        else {
            return false;
        }
    }

    public String[][] getAllInfo(){
        if (rb != null) {
            return rb.getAllInfo();
        }
        else {
            return null;
        }

    }

    public String[][] searchProducts(int id, String category, String keyword, Integer productRating, Integer storeRating, Integer priceFrom, Integer priceTo) {
        if (rb != null) {
            return rb.searchProducts(id, category, keyword, productRating, storeRating, priceFrom, priceTo);
        }
        else {
           return null;

        }
    }

    public boolean addToCart(int productId, Integer amount) {
        if (rb != null) {
            return rb.addToCart(productId, amount);
        }
        else {
            return false;

        }
    }

    public boolean updateAmount(int productId, int amount) {
        if (rb != null) {
            return rb.updateAmount(productId, amount);
        }
        else {
            return false;
        }
    }

    public boolean deleteItemInCart(int productId) {
        if (rb != null) {
            return rb.deleteItemInCart(productId);
        }
        else {
            return true;
        }
    }

    public boolean clearCart() {
        if (rb != null) {
            return rb.clearCart();
        }
        else {
            return true;
        }
    }

    public boolean buyCart(String[][] cart) {
        if (rb != null) {
            return rb.buyCart(cart);
        }
        else {
            return false;
        }
    }

    public String[][] viewCart(){
        if (rb != null) {
            return rb.viewCart();
        }
        else {
            return null;
        }

    }

    public boolean logout(){
        if (rb != null){
            return rb.logout();
        }
        else{
            return true;
        }
    }

    public int openStore(){
        if (rb != null){
            return rb.openStore();
        }
        else{
            return 69;
        }
    }

    public String[][] viewPurchaseHistory(){
        if(rb != null){
            return rb.viewPurchaseHistory();
        }
        else{
            return null;
        }
    }

    public String[][] searchUserHistory(String username){
        if(rb != null){
            return rb.searchUserHistory(username);
        }
        else{
            return null;
        }
    }

    public String[][] searchStoreHistory(int storeId){
        if(rb != null){
            return rb.searchStoreHistory(storeId);
        }
        else{
            return null;
        }
    }

    public boolean addProduct(int productId, int storeId, int amount){
        if(rb != null){
            return rb.addProduct(productId, storeId, amount);
        }
        else{
            return false;
        }
    }

    public boolean editProduct(int storeId, int productId, String productInfo){
        if(rb != null){
            return rb.editProduct(storeId, productId, productInfo);
        }
        else{
            return false;
        }
    }

    public boolean deleteProduct(int storeId, int productId){
            if(rb != null){
                return rb.deleteProduct(storeId, productId);
            }
            else{
                return false;
            }
        }

    public boolean appointManager(int storeId, String username){
        if(rb != null){
            return rb.appointManager(storeId, username);
        }
        else{
            return false;
        }
    }

    public boolean appointOwner(int storeId, String username){
        if(rb != null){
            return rb.appointOwner(storeId, username);
        }
        else{
            return false;
        }
    }

    public boolean removeManager(int storeId, String username){
        if(rb != null){
            return rb.removeManager(storeId, username);
        }
        else{
            return false;
        }
    }

    public boolean editManagerOptions(int storeId, int userId,int adminId, String option){
        if(rb != null){
            return rb.editManagerOptions(storeId, userId, adminId, option);
        }
        else{
            return false;
        }
    }

    public boolean updateItemDiscount(int storeId, int itemID, int discount){
        if(rb != null){
            return rb.updateItemDiscount(storeId, itemID, discount);
        }
        else{
            return false;
        }
    }



    public String[][] viewShopHistory(int storeId){
        if(rb != null){
            return rb.viewShopHistory(storeId);
        }
        else{
            return null;
        }
    }


    public void setRealBridge(RealBridge realBridge) {
        rb = realBridge;
    }
}
