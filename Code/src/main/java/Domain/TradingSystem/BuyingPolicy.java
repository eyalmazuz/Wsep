package Domain.TradingSystem;

public class BuyingPolicy {

    // This class defines a purchase policy for a store

    private String details;

    public BuyingPolicy(String details) {
        this.details = details;
    }

    public boolean isAllowed(User user, int productId) {
        if (details.equals("No one is allowed")) return false;
        return true;
    }

    @Override
    public String toString() {
        return "";

    }
}
