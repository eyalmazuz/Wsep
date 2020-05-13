package Domain.TradingSystem;

import Domain.Util.Pair;

import java.util.List;
import java.util.Map;

public class DiscountBasket {

    private List<DiscountProduct> discountBasket;

    public class DiscountProduct {
        private int productId;
        private int amount;
        private double price;

        public DiscountProduct(int productId, int amount, double price) {
            this.productId = productId;
            this.amount = amount;
            this.price = price;
        }

        public double getPrice() {
            return price;
        }
    }

    public double getTotalPrice() {
        double total = 0;
        for (DiscountProduct discountProduct: discountBasket) {
            total += discountProduct.getPrice();
        }
        return total;
    }

}
