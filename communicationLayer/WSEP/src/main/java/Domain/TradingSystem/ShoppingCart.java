package Domain.TradingSystem;

import DTOs.ActionResultDTO;
import DTOs.DoubleActionResultDTO;
import DTOs.ResultCode;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ShoppingCart {

    private User user;
    private ArrayList<ShoppingBasket> shoppingBaskets;

    public ShoppingCart(User user) {
        shoppingBaskets = new ArrayList<>();
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
        getOrCreateBasket(store).addProduct(product, amount);
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
            return basket.editProduct(product, newAmount);
        }
    }

    public ActionResultDTO removeProductFromCart(Store store, ProductInfo product) {
        if (store == null) return new ActionResultDTO(ResultCode.ERROR_CART_MODIFICATION, "Invalid store.");
        if (product == null) return new ActionResultDTO(ResultCode.ERROR_CART_MODIFICATION, "Invalid product ID");

        ShoppingBasket basket = getBasket(store);
        if (basket == null) {
            return new ActionResultDTO(ResultCode.ERROR_CART_MODIFICATION, "No basket for store " + store.getId());
        } else {
            return basket.removeProduct(product);
        }
    }

    public void removeAllProducts() {
        shoppingBaskets.clear();
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
        for (ShoppingBasket basket : shoppingBaskets) {
            storeProductsIds.put(basket.getStoreId(), covertInfoToIds(basket.getProducts()));
        }
        return storeProductsIds;
    }


    public Map<Integer, Map<Integer, Integer>> getPrimitiveDetails() {
        Map<Integer, Map<Integer, Integer>> map = new HashMap<>();
        for (ShoppingBasket basket : shoppingBaskets) {
            map.put(basket.getStoreId(), covertInfoToIds(basket.getProducts()));
        }
        return map;
    }


    private ShoppingBasket getBasket(Store store) {
        for (ShoppingBasket basket : shoppingBaskets) {
            if (basket.getStoreId() == store.getId()) {
                return basket;
            }
        }
        return null;
    }

    private ShoppingBasket getOrCreateBasket(Store store) {
        for (ShoppingBasket basket : shoppingBaskets) {
            if (store.getId() == basket.getStoreId()) {
                return basket;
            }
        }
        ShoppingBasket newBasket = new ShoppingBasket(store);
        shoppingBaskets.add(newBasket);
        return newBasket;
    }

    public void merge(ShoppingCart other) {
        List<Integer> existingStoreIDs = new ArrayList<>();
        for (ShoppingBasket basket : shoppingBaskets) {
            existingStoreIDs.add(basket.getStoreId());
            for (ShoppingBasket otherBasket : other.shoppingBaskets) {
                if (otherBasket.getStoreId() == basket.getStoreId()) {
                    basket.merge(otherBasket);
                    break;
                }
            }
        }

        for (ShoppingBasket otherBasket : other.shoppingBaskets) {
            if(!existingStoreIDs.contains(otherBasket.getStoreId())) {
                shoppingBaskets.add(otherBasket);
            }
        }

    }


    public ArrayList<ShoppingBasket> getBaskets(){
        return shoppingBaskets;
    }

    @Override
    public String toString() {
        String output = "";
        for (ShoppingBasket basket : shoppingBaskets) {
            output += "Basket for store ID: " + basket.getStoreId() + "\n";
            output += basket.toString() + "\n";
        }
        return output;
    }



    public boolean isEmpty() {
        return shoppingBaskets.isEmpty();
    }


    // 2.8 related
    public ActionResultDTO checkBuyingPolicy() {
        for (ShoppingBasket basket : shoppingBaskets) {
            if(!basket.checkBuyingPolicy(user, basket)) {
                return new ActionResultDTO(ResultCode.ERROR_PURCHASE, "Store " + basket.getStoreId() + " does not allow this purchase.");
            }
        }
        return new ActionResultDTO(ResultCode.SUCCESS, null);
    }

    public boolean checkStoreSupplies() {
        boolean missing = false;
        for (ShoppingBasket basket : shoppingBaskets) {
            if (!basket.checkStoreSupplies()) {
                missing = true;
                break;
            }
        }
        return !missing;
    }

    public DoubleActionResultDTO getPrice() {
        double totalPrice = 0;
        for (ShoppingBasket basket : shoppingBaskets) {
            totalPrice += basket.getTotalPrice(user).getPrice();
        }
        return new DoubleActionResultDTO(ResultCode.SUCCESS, "get price", totalPrice);
    }


    public Map<Store, PurchaseDetails> saveAndGetStorePurchaseDetails() {
        Map<Store, PurchaseDetails> storePurchaseDetailsMap = new HashMap<>();
        for (ShoppingBasket basket : shoppingBaskets) {
            PurchaseDetails details = basket.savePurchase(user);
            storePurchaseDetailsMap.put(basket.getStore(), details);
        }
        return storePurchaseDetailsMap;
    }

    public boolean updateStoreSupplies() {
        boolean flag = true;
        List <ShoppingBasket> baskets = new LinkedList<>();
        for (ShoppingBasket basket : shoppingBaskets) {
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
        for(ShoppingBasket basket:shoppingBaskets){
            storeIds.add(basket.getStoreId());
        }
        return storeIds;
    }
}
