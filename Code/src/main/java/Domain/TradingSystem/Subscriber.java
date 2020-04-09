package Domain.TradingSystem;

import java.util.LinkedList;
import java.util.List;

public class Subscriber implements UserState {

    //FIELDS:
    private static int idCounter = 0;

    private int id;
    private List <Permission> permissions;
    private User user;
    private String username;
    private String password;
    private boolean isAdmin;
    private PurchaseHistory allCartsHistory;

    //METHODS:
    public Subscriber(){
        this.id = idCounter;
        idCounter++;

        permissions = new LinkedList<Permission>();

    }

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

    public void setUser(User user) {
        this.user = user;
    }


    public Store openStore() {
        Store newStore = new Store();
        addPermission(newStore,null,"Owner");
        newStore.addOwner(this);
        return newStore;
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

    public boolean addOwner(Store store, Subscriber newOwner) {
        if(hasPermission(store.getId(),"Owner")!= null){
            if (!(newOwner.hasOwnerPermission(store.getId()))) {
                store.addOwner(newOwner);
               return newOwner.addPermission(store, this, "Owner");
            }
        }
        return false;
    }

    public boolean addManager(Store store, Subscriber newManager) {
        if(hasPermission(store.getId(),"Owner")!= null){
            if (!(newManager.hasManagerPermission(store.getId()))) {
                store.addOwner(newManager);
                return newManager.addPermission(store, this, "Manager");
            }
        }
        return false;
    }

    public boolean deleteManager(Store store, Subscriber managerToDelete) {
        if(hasPermission(store.getId(),"Owner")!= null){
            if (this.equals(managerToDelete.getGrantor("Manager",store))){
                managerToDelete.removePermission(store,"Manager");
                return true;
            }
        }
        return false;
    }

    private void removePermission(Store store, String type) {
        for (Permission permission : permissions) {
            if (permission.getStore().equals(store) && permission.getType().equals(type)) {
                permissions.remove(permission);
                break;
            }
        }
    }


    private Subscriber getGrantor(String type, Store store) {
        for (Permission permission: permissions){
            if (permission.getStore().equals(store) && permission.getType().equals(type)){
                return permission.getGrantor();
            }
        }
        return null;
    }


    public boolean hasOwnerPermission(int storeId) {
        for (Permission permission: permissions){
            if (permission.isOwner(storeId))
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

    public boolean addPermission(Store store, Subscriber grantor, String type ){
        if(!this.equals(grantor)) {
            Permission permission = new Permission(this, grantor, type, store);
            permissions.add(permission);
            return true;
        }
        return false;
    }

    public int getId() {
        return id;
    }

    public boolean hasManagerPermission(int storeId) {
        for (Permission permission: permissions){
            if (permission.isManager(storeId))
                return true;
        }
        return false;
    }

    public boolean isGrantedBy(int storeId, int grantorId) {
        for (Permission permission : permissions){
            if (permission.getType().equals("Manager") &&
                    permission.getStore().getId() == storeId
                        && permission.getGrantor().getId()==grantorId){
                return true;
            }
        }
        return false;
    }

    public String getManagerDetails(int storeId) {
        for (Permission permission : permissions){
            if (permission.getStore().getId()==storeId)
                return permission.getDetails();
        }
        return null;
    }


    boolean editPermission(Subscriber manager, Store store, String details) {
        if (hasPermission(store.getId(), "Owner") != null) {
            if (this.equals(manager.getGrantor("Manager",store))){
                manager.overridePermission("Manager",store,details);
                return true;
            }
        }
        return false;
    }

    public String getStoreHistory(int storeId) {
        Store store = hasPermission(storeId, "Owner");
        if (store == null){
            store = hasPermission(storeId, "Manager");
            if (store == null)
                return null;
            return store.getHistory();
        }
        return null;
    }

    private void overridePermission(String type, Store store, String details) {
        for (Permission permission: permissions){
            if (permission.getStore().equals(store) &&
                   permission.getType().equals(type))
                permission.setDetails(details);
        }

    }

}
