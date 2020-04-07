package Domain.TradingSystem;

import java.util.List;

public class Subscriber implements UserState {

    //FIELDS:
    private List <Permission> permissions;
    private User user;
    private String username;
    private String password;
    private boolean isAdmin;
    private PurchaseHistory allCartsHistory;

    //METHODS:
        public String getUsername() {
            return username;
        }
    /**
     *
     * Functions For Usecases 1.*
     *
     */
        public boolean isAdmin() {
            return isAdmin;
        }

        public void setAdmin() {
            isAdmin = true;
        }

        public void setUserName(String userName) {
            this.username = userName;
        }


        public void setPassword(String password) {
            this.password = password;
        }


    /**
     *
     * Function For Usecases 3.*
     *
     */
        public boolean logout(ShoppingCart cart) {
            allCartsHistory.setLatestCart(cart);
            cart.cleanCart();
            user.setState(new Guest());
            return true;

        }

    public String getHistory() {
        return allCartsHistory.toString();
    }

    /**
     *
     * Functions For Usecases 4.*
     *
     */
    public boolean addProductToStore(int store, int productId, int ammount) {

        Store currStore = hasPermission(store,"Owner");
        if(currStore != null){
            currStore.addProduct(productId,ammount);
            return true;
        }
        return false;


    }

    public boolean deleteProductFromStore(int store, int productId) {
        Store currStore = hasPermission(store,"Owner");
        if(currStore != null){
            return currStore.deleteProduct(productId);
        }
        return false;
    }


    public boolean editProductInStore(int store, int productId, String info) {
        Store currStore = hasPermission(store,"Owner");
        if(currStore != null){
            return currStore.editProduct(productId,info);
        }
        return false;
    }











    public boolean hasOwnerPermission() {
        for (Permission permission: permissions){
            if (permission.getType().equals("Owner"))
                return true;
        }
        return false;

      }


    protected Store hasPermission(int storeId, String type){
        for (Permission permission: permissions){
            if (permission.getStore().getId() == storeId &&
                    permission.getType().equals(type)){
                return permission.getStore();
            }
        }
        return null;
      }

    public void addPermission(Store store,User user, User grantor, String type ){
        Permission permission = new Permission(user,grantor, type, store);
        permissions.add(permission);
    }
}
