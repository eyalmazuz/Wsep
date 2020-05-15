package Domain.TradingSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DiscountPolicy {

    private List<DiscountType> discounts = new ArrayList<>();

    public double getBasketDiscountedPrice(User user, ShoppingBasket basket) {
        List<DiscountBasket.DiscountProduct> discountProducts = new ArrayList<>();

        Map<Integer, Integer> products = basket.getProducts();

        for (Map.Entry<Integer, Integer> entry : products.entrySet()) {
            double productPrice = basket.getProductPrice(entry.getKey());

            //java.lang.System.out.println(entry.getValue());
            //system.println(entry.getValue());
            discountProducts.add(new DiscountBasket.DiscountProduct(entry.getKey(), entry.getValue(), entry.getValue() * productPrice));
        }


        DiscountBasket discountBasket = new DiscountBasket(discountProducts);


        for (DiscountType discount: discounts) {
            discountBasket = discount.getDiscountedBasket(user, discountBasket);
        }

        return discountBasket.getTotalPrice();
    }


    @Override
    public String toString() {
        return "";

    }

    public void addDiscount(DiscountType discount) {
        discounts.add(discount);
    }

    public void clearDiscounts() {
        discounts.clear();
    }
}
