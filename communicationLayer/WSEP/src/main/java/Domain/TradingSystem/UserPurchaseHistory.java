package Domain.TradingSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserPurchaseHistory {


    private Map<Store, List<PurchaseDetails>> storePurchaseLists = new HashMap<>();

    @Override
    public String toString() {
        String output = "";
        for (Map.Entry<Store, List<PurchaseDetails>> purchase: storePurchaseLists.entrySet()) {
            output += "Basket Purchase for store ID: " + purchase.getKey().getId() + "\n";
            for(PurchaseDetails p: purchase.getValue()){
                output += p.toString() + "\n";
            }

        }
        return output;
    }




    public void addPurchase (Map <Store, PurchaseDetails> storePurchaseDetails){
        for (Store store : storePurchaseDetails.keySet()) {
            PurchaseDetails purchaseDetails = storePurchaseDetails.get(store);

            if (storePurchaseLists.containsKey(store)) {
                storePurchaseLists.get(store).add(purchaseDetails);
            } else {
                List<PurchaseDetails> detailsList = new ArrayList<>();
                detailsList.add(purchaseDetails);
                storePurchaseLists.put(store, detailsList);
            }
        }
    }

    public void removeLastItem(List<Store> stores) {
        for (Store store : stores) {
            List<PurchaseDetails> detailsList = storePurchaseLists.get(store);
            if (detailsList != null) {
                detailsList.remove(detailsList.size() - 1);
                if (detailsList.isEmpty()) storePurchaseLists.remove(store);
            }
        }
    }

    public Map<Store, List<PurchaseDetails>> getStorePurchaseLists() {
        return storePurchaseLists;
    }
}