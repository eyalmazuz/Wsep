package Service;

public class ProxyBridge implements Bridge {
    private RealBridge rb = null;


    public boolean login(String username, String password) {
        if (rb != null) {
            return rb.login(username, password);
        }
        else {
            return (username.equals("bob") && password.equals("1234")) || (username.equals("danny") && password.equals("password"));
        }
    }

    public boolean register(String username, String password) {
        if (rb != null) {
            return rb.register(username, password);
        }
        else {
            return !username.equals("bob");
        }
    }

    public String getAllInfo(){
        if (rb != null) {
            return rb.getAllInfo();
        }
        else {
            return "Store: Ebay, Products: Iphone 15 XS SUPER MAX ULTRA DELUX";
        }

    }

    public String searchProducts(String name, String category, String keyword, String filterOptions) {
        if (rb != null) {
            return rb.getAllInfo();
        }
        else {
            if (name.equals("Iphone")){
                return filterOptions == null ?
                        "Name: Iphone 11, Price: 3000, Category: Phones\n Name: Iphone 10, Price: 2500, Category: Phones" :
                        "Name: Iphone 11, Price: 3000, Category: Phones";
            }
            else{
                return "Error Bad Search Word";
            }

        }
    }

    public boolean addToCart(String productName, Integer amount) {
        if (rb != null) {
            return rb.addToCart(productName, amount);
        }
        else {
            if (amount == null || amount <= 0 ||  productName.equals("Iphone")){
                return false;
            }
            else{
                return true;
            }

        }
    }

    public boolean updateAmount(int amount) {
        if (rb != null) {
            return rb.updateAmount(amount);
        }
        else {
            return amount > 0;
        }
    }

    public boolean deleteItemInCart(String productName) {
        if (rb != null) {
            return rb.deleteItemInCart(productName);
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

    public boolean buyCart(String user, String cart) {
        if (rb != null) {
            return rb.buyCart(user, cart);
        }
        else {
            return user.equals("bob") && cart.equals("Name: Iphone 11, amount: 30");
        }
    }

    public String viewCart(){
        if (rb != null) {
            return rb.getAllInfo();
        }
        else {
            return "Item: Iphone 15 XS SUPER MAX ULTRA DELUX, Amount: 50";
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

    public boolean openStore(){
        if (rb != null){
            return rb.logout();
        }
        else{
            return true;
        }
    }

    public String viewPurchaseHistory(){
        if(rb != null){
            return rb.viewPurchaseHistory();
        }
        else{
            return "Date: 2020.3.25 Bought 30 Iphone 11";
        }
    }

    public String searchUserHistory(String username){
        if(rb != null){
            return rb.searchUserHistory(username);
        }
        else{
            return username.equals("bob") ? "Bob, Register Date: 3.3.3, Purchases: 30000" : "Invalid user";
        }
    }

    public String searchStoreHistory(String storeName){
        if(rb != null){
            return rb.searchUserHistory(storeName);
        }
        else{
            return storeName.equals("Amazon") ? "Amazon, Register Date: 3.3.3, items: 30000" : "Invalid Store";
        }
    }

    public boolean addProduct(int id, int amount){
        if(rb != null){
            return rb.addProduct(id, amount);
        }
        else{
            return amount > 0 && (id == 123 || id == 124);
        }
    }

    public boolean editProduct(int id, int amount){
        if(rb != null){
            return rb.editProduct(id, amount);
        }
        else{
            return amount > 0 && (id == 123 || id == 124);
        }
    }

    public boolean deleteProduct(int id){
            if(rb != null){
                return rb.deleteProduct(id);
            }
            else{
                return id == 123 || id == 124;
            }
        }

    public boolean appointManager(String username){
        if(rb != null){
            return rb.appointManager(username);
        }
        else{
            return username.equals("bob");
        }
    }

    public boolean appointOwner(String username){
        if(rb != null){
            return rb.appointOwner(username);
        }
        else{
            return username.equals("bob");
        }
    }

    public boolean removeManager(int id){
        if(rb != null){
            return rb.removeManager(id);
        }
        else{
            return id == 1;
        }
    }

    public boolean editManagerOptions(int id, int option){
        if(rb != null){
            return rb.editManagerOptions(id, option);
        }
        else{
            return id == 1 && option < 10;
        }
    }

    public boolean updateItemDiscount(int itemID, int discount){
        if(rb != null){
            return rb.updateItemDiscount(itemID, discount);
        }
        else{
            return itemID <= 10  && discount > 10 && discount < 100;
        }
    }



    public String viewShopHistory(){
        if(rb != null){
            return rb.viewShopHistory();
        }
        else{
            return "THIS IS A SHOP";
        }
    }


    public void setRealBridge(RealBridge realBridge) {
        rb = realBridge;
    }
}
