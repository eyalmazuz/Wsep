package Domain.TradingSystem;

import DataAccess.DAOManager;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@DatabaseTable (tableName = "userPurchaseHistories")
public class UserPurchaseHistory {

    @DatabaseField (id = true)
    private int id;

    private HashMap<Store, List<PurchaseDetails>> storePurchaseLists = new HashMap<>();

    @DatabaseField (dataType = DataType.SERIALIZABLE)
    private HashMap<Integer, List<Integer>> storePurchaseListsPrimitive = new HashMap<>();

    public UserPurchaseHistory() {}

    public UserPurchaseHistory(int subscriberId) {
        this.id = subscriberId;
    }

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
        DAOManager.createOrUpdateUserPurchaseHistory(this);
    }

    public void removeLastItem(List<Store> stores) {
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
        DAOManager.createOrUpdateUserPurchaseHistory(this);
    }

    public Map<Store, List<PurchaseDetails>> getStorePurchaseLists() {
        return storePurchaseLists;
    }

    public void setStorePurchaseLists(HashMap<Store, List<PurchaseDetails>> storePurchaseLists) {
        this.storePurchaseLists = storePurchaseLists;
    }

    public Map<Integer, List<Integer>> getStorePurchaseListsPrimitive() {
        return storePurchaseListsPrimitive;
    }
}