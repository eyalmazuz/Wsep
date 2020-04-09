package Domain.TradingSystem;

public class User {

    private Permission permissions;
    private UserState state;
    private ShoppingCart shoppingCart;

    public boolean addProductToStore(int storeId, int productId, int ammount) {
        Store currStore = permissions.hasPermission(storeId,"Owner");
        if(currStore != null){
            return state.addProductToStore(currStore,productId,ammount);
        }
        return false;
    }

    public void addProductToCart(int storeId, int productId, int amount) {
        shoppingCart.addProduct(storeId, productId, amount);
    }

    public void editCartProductAmount(int storeId, int productId, int newAmount) {
        shoppingCart.editProduct(storeId, productId, newAmount);
    }

    public void removeProductFromCart(int storeId, int productId) {
        shoppingCart.removeProductFromCart(storeId, productId);
    }

    public void removeAllProductsFromCart() {
        shoppingCart.removeAllProducts();
    }

}
