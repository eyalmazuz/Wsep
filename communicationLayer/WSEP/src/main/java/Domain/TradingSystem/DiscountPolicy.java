package Domain.TradingSystem;

import java.util.*;

public class DiscountPolicy {

    private Map<Integer, DiscountType> discountTypes = new HashMap<>();

    public double getBasketDiscountedPrice(User user, ShoppingBasket basket) {
        List<DiscountBasket.DiscountProduct> discountProducts = new ArrayList<>();

        Map<ProductInfo, Integer> products = basket.getProducts();

        for (Map.Entry<ProductInfo, Integer> entry : products.entrySet()) {
            double productPrice = basket.getProductPrice(entry.getKey().getId());

            //java.lang.System.out.println(entry.getValue());
            //system.println(entry.getValue());
            discountProducts.add(new DiscountBasket.DiscountProduct(entry.getKey().getId(), entry.getKey().getCategory(), entry.getValue(), entry.getValue() * productPrice));
        }


        DiscountBasket discountBasket = new DiscountBasket(discountProducts);


        for (DiscountType discount: discountTypes.values()) {
            discountBasket = discount.getDiscountedBasket(user, discountBasket);
        }

        return discountBasket.getTotalPrice();
    }


    @Override
    public String toString() {
        return "";

    }

    public int addDiscountType(DiscountType discount) {
        int id = 0;
        if (!discountTypes.isEmpty()) id = Collections.max(discountTypes.keySet()) + 1;
        discountTypes.put(id, discount);
        return id;
    }

    public void removeDiscountType(int discountTypeId) {
        discountTypes.remove(discountTypeId);
    }

    public void clearDiscountTypes() {
        discountTypes.clear();
    }
}
