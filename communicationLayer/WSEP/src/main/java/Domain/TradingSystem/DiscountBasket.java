package Domain.TradingSystem;

import Domain.Util.Pair;

import java.util.List;
import java.util.Map;

public class DiscountBasket {

    private List<DiscountProduct> discountProducts;

    public DiscountBasket(List<DiscountProduct> discountProducts) {
        this.discountProducts = discountProducts;
    }

    public static class DiscountProduct {
        // private ProductInfo productInfo;
        private int amount;
        private double price;
        private int productId;
        private String category;

        public DiscountProduct(int productId, String category, int amount, double price) {
            //this.productInfo = System.getProductInfoById(productId);
            this.productId = productId;
            this.category = category;
            this.amount = amount;
            this.price = price;
        }

        public double getPrice() {
            return price;
        }

//        public ProductInfo getProductInfo() {
//            return productInfo;
//        }

        public int getProductId() {
            return productId;
        }

        public String getCategory() {
            return category;
        }

        public int getAmount() {
            return amount;
        }

        public void setPrice(double price) {
            this.price = price;
        }
    }

    public double getTotalPrice() {
        double total = 0;
        for (DiscountProduct discountProduct: discountProducts) {
            total += discountProduct.getPrice();
        }
        return total;
    }

    public List<DiscountProduct> getDiscountProducts() {
        return discountProducts;
    }

}
