package Domain.TradingSystem;

import DTOs.ActionResultDTO;

public interface BuyingType {

    ActionResultDTO canBuy(User user, ShoppingBasket basket);

    void setId(int id);

    int getId();
}
