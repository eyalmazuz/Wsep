package Domain.TradingSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StorePurchaseHistory {

    private List<PurchaseDetails> purchaseHistory = new ArrayList<>();

    private Store store;

    public StorePurchaseHistory(Store store) {
        this.store = store;
    }

    public PurchaseDetails addPurchase(int purchaseId, User user, Map<ProductInfo, Integer> products, double price) {
        PurchaseDetails details = new PurchaseDetails(purchaseId, user, store, products, price);
        purchaseHistory.add(details);
        return details;
    }

    public void removePurchase(PurchaseDetails purchaseDetails) {
        purchaseHistory.remove(purchaseDetails);
    }

    public List<PurchaseDetails> getPurchaseHistory() {
        return purchaseHistory;
    }

    public void removeLastItem() {
        purchaseHistory.remove(purchaseHistory.size() - 1);
    }
}
