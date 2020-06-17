package Domain.TradingSystem;

import DTOs.Notification;
import DataAccess.DAOManager;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

@DatabaseTable(tableName = "subscribers")
public class Subscriber implements UserState {

    //FIELDS:
    public static int idCounter = 0;

    @DatabaseField (id = true)
    private int id;

    private Map <Integer,Permission> permissions; //StoreId -> Permission

    private User user;

    @DatabaseField(unique = true)
    private String username;

    @DatabaseField
    private String hashedPassword;

    @DatabaseField
    private boolean isAdmin;

    @DatabaseField
    private String country;

    private HashMap<Store, List<PurchaseDetails>> storePurchaseLists = new HashMap<>();

    @DatabaseField (dataType = DataType.SERIALIZABLE)
    private HashMap<Integer, List<Integer>> storePurchaseListsPrimitive = new HashMap<>();

    @DatabaseField (foreign = true)
    private ShoppingCart shoppingCart;

    @DatabaseField (dataType = DataType.SERIALIZABLE)
    private ArrayList<Notification> notificationQueue ;

    private Object permissionLock;

    public Subscriber() {
        notificationQueue = new ArrayList<>();
        permissions = new HashMap<>();
        permissionLock = new Object();
        shoppingCart = new ShoppingCart(user);
        this.id = idCounter;
        idCounter++;
    }

    public Subscriber(String username, String hashedPassword, boolean isAdmin) {
        this();
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.isAdmin = isAdmin;
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
        DAOManager.updateSubscriber(this);
    }

    public void setUserName(String userName) {
        this.username = userName;
        DAOManager.updateSubscriber(this);
   }



    /**
     *
     * Function For Usecases 3.*
     *
     */
    public boolean logout () {

        user.setState(new Guest(user));
        return true;

    }

    public String getPurchaseHistory() {
        String output = "";
        for (Map.Entry<Store, List<PurchaseDetails>> purchase: storePurchaseLists.entrySet()) {
            output += "Basket Purchase for store ID: " + purchase.getKey().getId() + "\n";
            for(PurchaseDetails p: purchase.getValue()){
                output += p.toString() + "\n";
            }

        }
        return output;
    }

    @Override
    public void setUser(User user) {
        if(user == null){
            throw new NullPointerException();
        }
        this.user = user;
        shoppingCart.setUser(user);
    }



    public Store openStore() {
        Store newStore = new Store();
        addPermission(newStore,null,"Owner");
        newStore.addOwner(this);
        return newStore;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public Map<Integer, List<Integer>> getStorePurchaseListsPrimitive() {
        return storePurchaseListsPrimitive;
    }

    @Override
    public ShoppingCart getShoppingCart() {
        return shoppingCart;
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

        DAOManager.updateSubscriber(this);
        DAOManager.removePermission(permission);

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
                    DAOManager.addPermission(newPermission);
                    DAOManager.updateSubscriber(this);
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
        return permission!=null && permission.getGrantor()!=null && permission.getGrantor().getId() == grantorId;

    }

    public ArrayList<String> getManagerDetails(int storeId) {

        Permission permission = permissions.get(storeId);
        if (permission!=null){
            return permission.getDetails();
        }
        return null;
    }


    @Override
    public void addPurchase(Map<Store, PurchaseDetails> storePurchaseDetails) {
        for (Store store : storePurchaseDetails.keySet()) {
            PurchaseDetails purchaseDetails = storePurchaseDetails.get(store);

            if (storePurchaseLists.containsKey(store)) {
                storePurchaseLists.get(store).add(purchaseDetails);
                storePurchaseListsPrimitive.get(store.getId()).add(purchaseDetails.getId());
            } else {
                List<PurchaseDetails> detailsList = new ArrayList<>();
                detailsList.add(purchaseDetails);
                storePurchaseLists.put(store, detailsList);
                List<Integer> detailsListIds = new ArrayList<>();
                for (PurchaseDetails details : detailsList) detailsListIds.add(details.getId());
                storePurchaseListsPrimitive.put(store.getId(), detailsListIds);
            }
        }
        DAOManager.updateSubscriber(this);
    }

    public void overridePermission(String type, Store store, String details) {
        Permission permission = permissions.get(store.getId());
        synchronized (permissionLock) {
            permission = permissions.get(store.getId()); //in case that another process removes the permission
            if (permission != null && permission.getType().equals(type)) {
                permission.setDetails(details);
                DAOManager.updatePermission(permission);
            }
        }
        DAOManager.updateSubscriber(this);

    }


    public void removeLastHistoryItem(List<Store> stores) {
        for (Store store : stores) {
            List<PurchaseDetails> detailsList = storePurchaseLists.get(store);
            if (detailsList != null) {
                detailsList.remove(detailsList.size() - 1);
                if (detailsList.isEmpty()) {
                    storePurchaseLists.remove(store);
                    storePurchaseListsPrimitive.remove(store.getId());
                }
            }
        }
        DAOManager.updateSubscriber(this);
    }

    public String getHashedPassword() {
        return hashedPassword;
    }


    public User getUser() {
        return user;
    }

    public String getPermissionType(int storeId) {
        Permission permission = permissions.get(storeId);
        if(permission!=null){
            return permission.getType();
        }
        return "No Permissions";
    }

    public void setNotification(Notification message){
        notificationQueue.add(message);
        DAOManager.updateSubscriber(this);
    }

    public ArrayList<Notification> getAllNotification(){
        return notificationQueue;
    }

    public void setNotificationQueue(ArrayList<Notification> notificationQueue) {
        this.notificationQueue = notificationQueue;
    }

    public void removeNotification(int id){
        Notification toRemove = null;
        synchronized (notificationQueue){
            for(Notification notification:notificationQueue){
                if(notification.getId() == id){
                   toRemove = notification;
                   break;
                }
            }
        }
        if(toRemove!=null){
            notificationQueue.remove(toRemove);
            DAOManager.updateSubscriber(this);

        }
    }

    public Permission getPermission(int storeId) {
        return permissions.get(storeId);
    }

    public void setPermissions(Map<Integer, Permission> permissions) {
        this.permissions = permissions;
    }

    public boolean equals(Object o) {
        return (o instanceof Subscriber) && ((Subscriber) o).getId() == id;
    }

    public Map<Store, List<PurchaseDetails>> getStorePurchaseLists() {
        return storePurchaseLists;
    }

    public void setStorePurchaseLists(HashMap<Store, List<PurchaseDetails>> storePurchaseLists) {
        this.storePurchaseLists = storePurchaseLists;
    }

    public List<Integer> removeOwnership(int storeId) {
        Store store = getPermission(storeId).getStore();
        List<Integer> removed = new ArrayList<>();
        List<Subscriber> grantedBy = store.getAllGrantedBy(this);
        for(Subscriber manager: grantedBy){
            if(manager.hasOwnerPermission(storeId)){
                removed.addAll(manager.removeOwnership(storeId));
            }
            else if(manager.hasManagerPermission(storeId)){
                manager.removeManagment(storeId);
                removed.add(manager.getId());
            }
        }
        removePermission(store,"Owner");
        store.removeManger(this);
        removed.add(this.getId());
        return removed;
    }

    public void removeManagment(int storeId) {
        Store store = getPermission(storeId).getStore();
        removePermission(store,"Manager");
        store.removeManger(this);
    }

    public List<Store> getAllOwnedStores() {
        List<Store> stores = new ArrayList<>();
        for( Permission permission : permissions.values()){
            if(permission.getType().equals("Owner"))
                stores.add(permission.getStore());
        }
        return stores;
    }

    public void setId(int sessionId) {
        this.id = sessionId;
    }

    @Override
    public void setShoppingCart(ShoppingCart cart) {
        this.shoppingCart = cart;
    }

    /*
    Goes over Permission List and find if any of them is owner permission
     */
    public boolean isOwner() {
        for(Permission p :permissions.values()){
            if(p.getType().equals("Owner"))
                return true;
        }
        return false;
    }

    public boolean isManager() {
        for(Permission p :permissions.values()){
            if(p.getType().equals("Manager"))
                return true;
        }
        return false;
    }

    public void updateNotificationQueue(ArrayList<Notification> notifications) {

        this.notificationQueue.addAll(notifications);

    }
}
