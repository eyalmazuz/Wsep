package Domain.TradingSystem;

import java.util.*;

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

    public boolean addProduct(Store store, int productId, int amount) {
        if (store == null || productId < 0 || amount < 1) return false;
        getOrCreateBasket(store).addProduct(productId, amount);
        return true;
    }

    public boolean editProduct(Store store, int productId, int newAmount) {
        if (newAmount < 1 || store == null) return false;

        ShoppingBasket basket = getBasket(store);
        if (basket == null) {
            return false;
        } else {
            return basket.editProduct(productId, newAmount);
        }
    }

    public boolean removeProductFromCart(Store store, int productId) {
        ShoppingBasket basket = getBasket(store);
        if (basket == null) {
            return false;
        } else {
            return basket.removeProduct(productId);
        }
    }

    public void removeAllProducts() {
        shoppingBaskets.clear();
    }


    public Map<Integer, Map<Integer, Integer>> getStoreProductsIds() {
        Map<Integer, Map<Integer, Integer>> storeProductsIds = new HashMap<>();
        for (ShoppingBasket basket : shoppingBaskets) {
            storeProductsIds.put(basket.getStoreId(), basket.getProducts());
        }
        return storeProductsIds;
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
    public boolean checkBuyingPolicy() {
        boolean allowed = true;
        for (ShoppingBasket basket : shoppingBaskets) {
            if(!basket.checkBuyingPolicy(user)) {
                allowed = false;
                break;
            }
        }
        return allowed;
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

    public double getPrice() {
        double totalPrice = 0;
        for (ShoppingBasket basket : shoppingBaskets) {
            totalPrice += basket.getTotalPrice(user);
        }
        return totalPrice;
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

    public Map<Integer, Map<Integer, Integer>> getPrimitiveDetails() {
        Map<Integer, Map<Integer, Integer>> map = new HashMap<>();
        for (ShoppingBasket basket : shoppingBaskets) {
            map.put(basket.getStoreId(), basket.getProducts());
        }
        return map;
    }
}
