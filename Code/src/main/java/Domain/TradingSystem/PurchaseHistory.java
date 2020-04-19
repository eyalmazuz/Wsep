package Domain.TradingSystem;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PurchaseHistory {

    private ShoppingCart latestCart;
    private Map<Integer, List<PurchaseDetails>> storePurchaseLists = new HashMap<>();

    @Override
    public String toString() {
        // TODO: tostring
        return "";
    }

    public void setLatestCart(ShoppingCart cart) {
        latestCart = cart;
    }

    public void addPurchase (Map < Integer, PurchaseDetails > storePurchaseDetails){
        for (Integer storeId : storePurchaseDetails.keySet()) {
            PurchaseDetails purchaseDetails = storePurchaseDetails.get(storeId);

            if (storePurchaseLists.containsKey(storeId)) {
                storePurchaseLists.get(storeId).add(purchaseDetails);
            } else {
                List<PurchaseDetails> detailsList = new ArrayList<>();
                detailsList.add(purchaseDetails);
                storePurchaseLists.put(storeId, detailsList);
            }

        }
    }

    public void removePurchase (Map < Integer, PurchaseDetails > storePurchaseDetails) {
        for (Integer storeId : storePurchaseDetails.keySet()) {
            PurchaseDetails purchaseDetails = storePurchaseDetails.get(storeId);

            if (storePurchaseLists.containsKey(storeId)) {
                storePurchaseLists.get(storeId).remove(purchaseDetails);
            }
        }
    }
}