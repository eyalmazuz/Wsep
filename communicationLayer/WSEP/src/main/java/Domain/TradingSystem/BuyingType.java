package Domain.TradingSystem;

public interface BuyingType {

    boolean canBuy(User user, int storeId);
}
