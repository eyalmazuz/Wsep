package Domain.TradingSystem;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Subscriber implements UserState {

    //FIELDS:
    private static int idCounter = 0;

    private int id;
    private List <Permission> permissions;
    private User user;
    private String username;
    private String hashedPassword;
    private boolean isAdmin;
    private UserPurchaseHistory userPurchaseHistory;

    public Subscriber() {
        userPurchaseHistory = new UserPurchaseHistory();
        permissions = new LinkedList<>();
    }

    public Subscriber(String username, String hashedPassword, boolean isAdmin) {
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.isAdmin = isAdmin;
        this.id = idCounter;
        idCounter++;
        permissions = new LinkedList<Permission>();
        // FIX for acceptance tests
        userPurchaseHistory = new UserPurchaseHistory();

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



    /**
     *
     * Function For Usecases 3.*
     *
     */
    public boolean logout () {

        user.setState(new Guest());
        return true;

    }

    public void saveCart (ShoppingCart cart){
        userPurchaseHistory.setLatestCart(cart);
        cart.removeAllProducts();
    }

    public String getHistory() {
        return userPurchaseHistory.toString();
    }

    @Override
    public void setUser(User user) {
        if(user == null){
            throw new NullPointerException();
        }
        this.user = user;
    }



    public Store openStore() {
        Store newStore = new Store();
        addPermission(newStore,null,"Owner");
        newStore.addOwner(this);
        return newStore;
    }




    /**
     * goes by the subscriber permissions and check if he can do certein tasks
     * @param store_id
     * @param type
     * @return true if the user had the permissions, false otherwise
     */
    public boolean checkPrivilage(int store_id,String type) {
        if(type.equals("any"))
            return true;
        
        for (Permission permission: permissions){
            if ((permission.getStore().getId()==store_id)&&(permission.hasPrivilage(type)))
                return true;
        }
        return false;
    }


    public boolean addOwner(Store store, Subscriber newOwner) {
        if(newOwner.addPermission(store, this, "Owner")){
            store.addOwner(newOwner);
            return true;
        }

        return false;

    }

    public boolean addManager(Store store, Subscriber newManager) {
        if(newManager.addPermission(store, this, "Manager")){
            store.addOwner(newManager);
            return true;
        }

        return false;
    }

    public boolean deleteManager(Store store, Subscriber managerToDelete) {

        managerToDelete.removePermission(store,"Manager");
        store.removeManger(managerToDelete);
        return true;

    }

    private void removePermission(Store store, String type) {
        for (Permission permission : permissions) {
            if (permission.getStore().equals(store) && permission.getType().equals(type)) {
                permissions.remove(permission);
                break;
            }
        }
    }


    public Subscriber getGrantor(String type, Store store) {
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


    public boolean editPermission(Subscriber manager, Store store, String details) {

        String[] validDetailes = {"any", "add product", "edit product", "delete product"};
        if(Arrays.asList(validDetailes).contains(details)) {
            manager.overridePermission("Manager", store, details);
            return true;
        }
        return false;

    }


    @Override
    public void addPurchase(Map<Store, PurchaseDetails> storePurchaseDetails) {
        userPurchaseHistory.addPurchase(storePurchaseDetails);
    }

    private void overridePermission(String type, Store store, String details) {
        for (Permission permission: permissions){
            if (permission.getStore().equals(store) &&
                   permission.getType().equals(type))
                permission.setDetails(details);
        }

    }


    public void removeLastHistoryItem(List<Store> stores) {
        userPurchaseHistory.removeLastItem(stores);
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    @Override
    public UserPurchaseHistory getUserPurchaseHistory() {
        return userPurchaseHistory;
    }


    public User getUser() {
        return user;
    }
}
