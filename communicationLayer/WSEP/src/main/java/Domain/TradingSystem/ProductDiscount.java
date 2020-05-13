package Domain.TradingSystem;

import java.util.List;

public class ProductDiscount extends SimpleDiscount {

    @Override
    public DiscountBasket getDiscountedBasket(User user, DiscountBasket discountBasket) {
        return null;
    }

    public static class ProductSaleDiscount extends ProductDiscount {

        private int productId;
        private double salePercentage;

        public ProductSaleDiscount(int productId, double salePercentage) {
            this.productId = productId;
            this.salePercentage = salePercentage;
        }

        public DiscountBasket getDiscountedBasket(User user, DiscountBasket discountBasket) {
            List<DiscountBasket.DiscountProduct> discountProducts = discountBasket.getDiscountProducts();

            for (DiscountBasket.DiscountProduct discountProduct: discountProducts) {
                if (discountProduct.getProductInfo().getId() == productId) {
                    double curPrice = discountProduct.getPrice();
                    discountProduct.setPrice(salePercentage * curPrice);
                }
            }
            return discountBasket;
        }
    }

    public static class CategorySaleDiscount extends ProductDiscount {

        private String categoryName;
        private double salePercentage;

        public CategorySaleDiscount(String categoryName, double salePercentage) {
            this.categoryName = categoryName;
            this.salePercentage = salePercentage;
        }

        public DiscountBasket getDiscountedBasket(User user, DiscountBasket discountBasket) {
            List<DiscountBasket.DiscountProduct> discountProducts = discountBasket.getDiscountProducts();

            for (DiscountBasket.DiscountProduct discountProduct: discountProducts) {
                if (discountProduct.getProductInfo().getCategory().equals(categoryName)) {
                    double curPrice = discountProduct.getPrice();
                    discountProduct.setPrice(salePercentage * curPrice);
                }
            }
            return discountBasket;
        }
    }

}

