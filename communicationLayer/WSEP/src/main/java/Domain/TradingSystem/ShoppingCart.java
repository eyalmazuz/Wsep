package Domain.TradingSystem;

import DTOs.ActionResultDTO;
import DTOs.DoubleActionResultDTO;
import DTOs.ResultCode;
import DataAccess.DAOManager;
import DataAccess.DatabaseFetchException;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@DatabaseTable(tableName ="shoppingCarts")
public class ShoppingCart {

    @DatabaseField (generatedId = true)
    private int id;

    private User user;

    @ForeignCollectionField(eager = true)
    private ForeignCollection<ShoppingBasket> persistentShoppingBaskets = null;

    private List<ShoppingBasket> nonPersistentShoppingBaskets = new ArrayList<>();

    public ShoppingCart() {}

    public ShoppingCart(User user) {
        DAOManager.createBasketListForCart(this);
        this.user = user;

    }



    /**
     *
     * Functions For Usecases 2.6, 2.7.*
     *
     */

    public ActionResultDTO addProduct(Store store, ProductInfo product, int amount) {
        if (store == null) return new ActionResultDTO(ResultCode.ERROR_CART_MODIFICATION, "Invalid store.");
        if (product == null) return new ActionResultDTO(ResultCode.ERROR_CART_MODIFICATION, "Invalid product ID");
        if (amount < 1) return new ActionResultDTO(ResultCode.ERROR_CART_MODIFICATION, "Amount must be positive.");
        getOrCreateBasket(store).addProduct(product, amount, user.getState() instanceof Subscriber);
        return new ActionResultDTO(ResultCode.SUCCESS, null);
    }

    public ActionResultDTO editProduct(Store store, ProductInfo product, int newAmount) {
        if (store == null) return new ActionResultDTO(ResultCode.ERROR_CART_MODIFICATION, "Invalid store.");
        if (product == null) return new ActionResultDTO(ResultCode.ERROR_CART_MODIFICATION, "Invalid product ID");
        if (newAmount < 1) return new ActionResultDTO(ResultCode.ERROR_CART_MODIFICATION, "Amount must be positive.");

        ShoppingBasket basket = getBasket(store);
        if (basket == null) {
            return new ActionResultDTO(ResultCode.ERROR_CART_MODIFICATION, "No basket for store " + store.getId());
        } else {
            return basket.editProduct(product, newAmount, user.getState() instanceof Subscriber);
        }
    }

    public ActionResultDTO removeProductFromCart(Store store, ProductInfo product) {
        if (store == null) return new ActionResultDTO(ResultCode.ERROR_CART_MODIFICATION, "Invalid store.");
        if (product == null) return new ActionResultDTO(ResultCode.ERROR_CART_MODIFICATION, "Invalid product ID");

        ShoppingBasket basket = getBasket(store);
        if (basket == null) {
            return new ActionResultDTO(ResultCode.ERROR_CART_MODIFICATION, "No basket for store " + store.getId());
        } else {
            return basket.removeProduct(product, user.getState() instanceof Subscriber);
        }
    }

    public void removeAllProducts() {
        getCurrentBasketCollection().clear();
    }

    private Collection<ShoppingBasket> getCurrentBasketCollection() {
        if (user == null) return persistentShoppingBaskets; // no user? cart loaded from db
        return user.getState() instanceof Subscriber ? persistentShoppingBaskets : nonPersistentShoppingBaskets;
    }

    private Map<Integer,Integer> covertInfoToIds(Map <ProductInfo,Integer> infos){
        Map<Integer,Integer> result = new ConcurrentHashMap<>();
        for(ProductInfo product: infos.keySet()){
            result.put(product.getId(),infos.get(product));
        }
        return result;
    }

    public Map<Integer, Map<Integer, Integer>> getStoreProductsIds() {
        Map<Integer, Map<Integer, Integer>> storeProductsIds = new HashMap<>();
        for (ShoppingBasket basket : getCurrentBasketCollection()) {
            storeProductsIds.put(basket.getStoreId(), covertInfoToIds(basket.getProducts()));
        }
        return storeProductsIds;
    }


    public Map<Integer, Map<Integer, Integer>> getPrimitiveDetails() {
        Map<Integer, Map<Integer, Integer>> map = new HashMap<>();
        for (ShoppingBasket basket : getCurrentBasketCollection()) {
            map.put(basket.getStoreId(), covertInfoToIds(basket.getProducts()));
        }
        return map;
    }


    private ShoppingBasket getBasket(Store store) {
        for (ShoppingBasket basket : getCurrentBasketCollection()) {
            if (basket.getStoreId() == store.getId()) {
                return basket;
            }
        }
        return null;
    }

    private ShoppingBasket getOrCreateBasket(Store store) {
        for (ShoppingBasket basket : getCurrentBasketCollection()) {
            if (store.getId() == basket.getStoreId()) {
                return basket;
            }
        }
        ShoppingBasket newBasket = new ShoppingBasket(this, store);
        getCurrentBasketCollection().add(newBasket);
        return newBasket;
    }


    public ArrayList<ShoppingBasket> getBaskets(){
        return new ArrayList<>(getCurrentBasketCollection());
    }

    @Override
    public String toString() {
        String output = "";
        for (ShoppingBasket basket : getCurrentBasketCollection()) {
            output += "Basket for store ID: " + basket.getStoreId() + "\n";
            output += basket.toString() + "\n";
        }
        return output;
    }



    public boolean isEmpty() {
        return getCurrentBasketCollection().isEmpty();
    }


    // 2.8 related
    public ActionResultDTO checkBuyingPolicy() {
        boolean error = false;
        String errorStrings = "";
        for (ShoppingBasket basket : getCurrentBasketCollection()) {
            ActionResultDTO buyingPolicyCheckResult = basket.checkBuyingPolicy(user, basket);
            if(buyingPolicyCheckResult.getResultCode() != ResultCode.SUCCESS) {
                error = true;
                errorStrings += ("Store " + basket.getStoreId() + " does not allow this purchase. Details: " + buyingPolicyCheckResult.getDetails() + "\n");
            }
        }
        if (error) return new ActionResultDTO(ResultCode.ERROR_PURCHASE, errorStrings);
        return new ActionResultDTO(ResultCode.SUCCESS, null);
    }

    public boolean checkStoreSupplies() {
        boolean missing = false;
        for (ShoppingBasket basket : getCurrentBasketCollection()) {
            if (!basket.checkStoreSupplies()) {
                missing = true;
                break;
            }
        }
        return !missing;
    }

    public DoubleActionResultDTO getPrice() {
        double totalPrice = 0;
        for (ShoppingBasket basket : getCurrentBasketCollection()) {
            totalPrice += basket.getTotalPrice(user).getPrice();
        }
        return new DoubleActionResultDTO(ResultCode.SUCCESS, "get price", totalPrice);
    }


    public Map<Store, PurchaseDetails> saveAndGetStorePurchaseDetails() {
        Map<Store, PurchaseDetails> storePurchaseDetailsMap = new HashMap<>();
        for (ShoppingBasket basket : getCurrentBasketCollection()) {
            PurchaseDetails details = basket.savePurchase(user);
            storePurchaseDetailsMap.put(basket.getStore(), details);
        }
        return storePurchaseDetailsMap;
    }

    public boolean updateStoreSupplies() throws DatabaseFetchException {
        boolean flag = true;
        List <ShoppingBasket> baskets = new LinkedList<>();
        for (ShoppingBasket basket : getCurrentBasketCollection()) {
            flag = basket.updateStoreSupplies();
            if (flag == false){
                for (ShoppingBasket restoreBasket : baskets){
                    restoreBasket.restoreStoreSupplies();
                }
                return false;
            }
            else
                baskets.add(basket);
        }
        return true;
    }



    public List<Integer> getStores() {
        List<Integer> storeIds = new ArrayList<>();
        for(ShoppingBasket basket: getCurrentBasketCollection()){
            storeIds.add(basket.getStoreId());
        }
        return storeIds;
    }

    public void setUser(User u) {
        this.user = u;
    }

}
