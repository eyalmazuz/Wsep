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
        // TODO implement this
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

    public void setRealBridge(RealBridge realBridge) {
        rb = realBridge;
    }
}
