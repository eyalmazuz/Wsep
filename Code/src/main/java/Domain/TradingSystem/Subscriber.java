package Domain.TradingSystem;

import java.util.List;

public class Subscriber implements UserState {

    private List <Permission> permissions;
    private String username;
    private String password;
    private boolean isAdmin;

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

    public void setUserName(String userName) {
        this.username = userName;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    public void addPermission(Store store,User user, User grantor, String type ){
        Permission permission = new Permission(user,grantor, type, store);
        permissions.add(permission);
    }

    public String getUsername() {
        return username;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin() {
        isAdmin = true;
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


}
