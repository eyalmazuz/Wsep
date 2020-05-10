package Domain.TradingSystem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Subscriber implements UserState {

    //FIELDS:
    private static int idCounter = 0;
    private int id;
    private Map <Integer,Permission> permissions; //StoreId -> Permission
    private User user;
    private String username;
    private String hashedPassword;
    private boolean isAdmin;
    private UserPurchaseHistory userPurchaseHistory;
    private Object permissionLock;

    public Subscriber() {
        userPurchaseHistory = new UserPurchaseHistory();
        permissions = new HashMap<>();
        permissionLock = new Object();
    }

    public Subscriber(String username, String hashedPassword, boolean isAdmin) {
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.isAdmin = isAdmin;
        this.id = idCounter;
        idCounter++;
        permissions = new HashMap<>();
        // FIX for acceptance tests
        userPurchaseHistory = new UserPurchaseHistory();
        permissionLock = new Object();

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

    public UserPurchaseHistory getHistory() {
        return userPurchaseHistory;
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

        Permission p = permissions.get(store_id);

        return (p!=null && p.hasPrivilage(type));
        

    }





    public void removePermission(Store store, String type) {

        Permission permission = permissions.get(store.getId());
        synchronized (permissionLock) {
            permission = permissions.get(store.getId());//in case that another subscriber removes the permission
            if (permission != null && permission.getType().equals(type)) {
                permissions.remove(store.getId());
            }
        }

    }


    public Subscriber getGrantor(String type, Store store) {
        Permission permission = permissions.get(store.getId());

        if(permission!= null && permission.getType().equals(type)) {
            return permission.getGrantor();
        }

        return null;
    }


    public boolean hasOwnerPermission(int storeId) {
        Permission permission = permissions.get(storeId);

        return permission!=null && permission.isOwner(storeId);

      }


    protected Store hasPermission(int storeId, String type){

        Permission permission = permissions.get(storeId);

        if(permission!= null && permission.getType().equals(type))
            return permission.getStore();

        return null;

      }

    public boolean addPermission(Store store, Subscriber grantor, String type ){
        if(!this.equals(grantor)) {
            Permission permission = permissions.get(store.getId()) ;
            synchronized (permissionLock){
                permission = permissions.get(store.getId()) ; //in case that another process removes the permission
                if (permission == null || (permission.getType().equals("Manager") && type.equals("Owner"))) {
                    //The line above grants permission will be set only once Or upgrade from manager to owner
                    Permission newPermission = new Permission(this, grantor, type, store);
                    permissions.put(store.getId(), newPermission);
                    return true;
                }
            }
        }
        return false;
    }

    public int getId() {
        return id;
    }

    public boolean hasManagerPermission(int storeId) {

        Permission permission = permissions.get(storeId);
        return permission!= null && permission.isManager(storeId);

    }

    public boolean isGrantedBy(int storeId, int grantorId) {

        Permission permission = permissions.get(storeId);
        return permission!=null && permission.getGrantor().getId() == grantorId;

    }

    public String getManagerDetails(int storeId) {

        Permission permission = permissions.get(storeId);
        if (permission!=null){
            return permission.getDetails();
        }
        return null;
    }





    @Override
    public void addPurchase(Map<Store, PurchaseDetails> storePurchaseDetails) {
        userPurchaseHistory.addPurchase(storePurchaseDetails);
    }

    public void overridePermission(String type, Store store, String details) {
        Permission permission = permissions.get(store.getId());
        synchronized (permissionLock) {
            permission = permissions.get(store.getId()); //in case that another process removes the permission
            if (permission != null && permission.getType().equals(type)) {
                permission.setDetails(details);
            }
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
