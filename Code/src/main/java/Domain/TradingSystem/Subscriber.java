package Domain.TradingSystem;

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
    private PurchaseHistory purchaseHistory;

    public Subscriber() {
        purchaseHistory = new PurchaseHistory();


    }

    public Subscriber(String username, String hashedPassword, boolean isAdmin) {
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.isAdmin = isAdmin;
        this.id = idCounter;
        idCounter++;
        permissions = new LinkedList<Permission>();
        // FIX for acceptance tests
        purchaseHistory = new PurchaseHistory();

    }

    public boolean addProductToStore(Store store, int productId, int ammount) {
        store.addProduct(productId,ammount);
        return true;
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
    public boolean logout (ShoppingCart cart) {
        purchaseHistory.setLatestCart(cart);
        cart.removeAllProducts();
        user.setState(new Guest());
        return true;

    }

    public String getHistory() {
        return purchaseHistory.toString();
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
            return currStore.addProduct(productId,ammount);
        }
        else{
            //Adition to usecase 5.1
            if((hasManagerPermission(store))&&(checkPrivilage(store,"add product"))){
                currStore = hasPermission(store,"manager");
                currStore.addProduct(productId,ammount);
                return true;
            }
        }
        return false;

    }

    /**
     * goes by the subscriber permissions and check if he can do certein tasks
     * @param store_id
     * @param type
     * @return true if the user had the permissions, false otherwise
     */
    private boolean checkPrivilage(int store_id,String type) {
        for (Permission permission: permissions){
            if ((permission.getStore().getId()==store_id)&&(permission.hasPrivilage(type)))
                return true;
        }
        return false;
    }

    public boolean deleteProductFromStore(int store, int productId) {
        Store currStore = hasPermission(store,"Owner");
        if(currStore != null){
            return currStore.deleteProduct(productId);
        }
        else{
            //Adition to usecase 5.1
            if((hasManagerPermission(store))&&(checkPrivilage(store,"delete product"))){
                currStore = hasPermission(store,"manager");
                currStore.deleteProduct(productId);
                return true;
            }
        }
        return false;
    }


    public boolean editProductInStore(int store, int productId, String info) {
        Store currStore = hasPermission(store,"Owner");
        if(currStore != null){
            return currStore.editProduct(productId,info);
        }
        else{
            //Adition to usecase 5.1
            if((hasManagerPermission(store))&&(checkPrivilage(store,"edit product"))){
                currStore = hasPermission(store,"manager");
                currStore.editProduct(productId,info);
                return true;
            }
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


    public boolean editPermission(Subscriber manager, Store store, String details) {
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

        }
        return store.getHistory();
    }

    @Override
    public void addPurchase(Map<Integer, PurchaseDetails> storePurchaseDetails) {
        purchaseHistory.addPurchase(storePurchaseDetails);
    }

    @Override
    public void removePurchase(Map<Integer, PurchaseDetails> storePurchaseDetails) {
        purchaseHistory.removePurchase(storePurchaseDetails);
    }

    private void overridePermission(String type, Store store, String details) {
        for (Permission permission: permissions){
            if (permission.getStore().equals(store) &&
                   permission.getType().equals(type))
                permission.setDetails(details);
        }

    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public PurchaseHistory getPurchaseHistory() {
        return purchaseHistory;
    }

}
