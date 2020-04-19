package Domain.TradingSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class History {

    private List<PurchaseDetails> purchaseHistory = new ArrayList<>();

    public PurchaseDetails addPurchase(int purchaseId, User user, Map<Integer, Integer> products, double price) {
        PurchaseDetails details = new PurchaseDetails(purchaseId, user, products, price);
        purchaseHistory.add(details);
        return details;
    }

    public void removePurchase(PurchaseDetails purchaseDetails) {
        purchaseHistory.remove(purchaseDetails);
    }
}
