package Domain.TradingSystem;

import DTOs.ActionResultDTO;
import DTOs.ResultCode;

public class SimpleBuying implements BuyingType {

    protected int id = -1;

    public SimpleBuying() {

    }


    public ActionResultDTO canBuy(User user, ShoppingBasket basket) {
        if (this instanceof BasketBuyingConstraint) {
            BasketBuyingConstraint c = (BasketBuyingConstraint) this;
            return c.canBuy(basket);
        }
        if (this instanceof SystemBuyingConstraint) {
            SystemBuyingConstraint c = (SystemBuyingConstraint) this;
            return c.canBuy();
        }
        if (this instanceof UserBuyingConstraint) {
            UserBuyingConstraint c = (UserBuyingConstraint) this;
            return c.canBuy(user.getCountry());
        }
        return new ActionResultDTO(ResultCode.SUCCESS, null);
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

}

