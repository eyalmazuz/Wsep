package Domain.TradingSystem;

import java.util.List;

public class PurchaseHistory {

    private ShoppingCart latestCart;
    private List<ShoppingCart> allTimeHistory;

    @Override
    public String toString() {
        String output = "History:\n ";
        for (ShoppingCart cart: allTimeHistory){
            output+=cart.toString();
        }
        return output;
    }

    public void setLatestCart(ShoppingCart cart) {
        latestCart.copyCart(cart);
    }
}
