package Domain.TradingSystem;

import java.util.List;

public class ShoppingCart {
    private List<ShoppingBasket> baskets;

    /**
     * Clones the shoppingBaskets from cart onto this
     * @param cart
     */
    public void copyCart(ShoppingCart cart) {
        //TODO:Implement this
    }

    /**
     * Removes all products from current basket,activates when logging out.
     */
    public void cleanCart() {
        //TODO:Implemet this
    }

    public List<ShoppingBasket> getBaskets(){
        return baskets;
    }
}
