package Domain.TradingSystem;

import java.util.ArrayList;
import java.util.List;

public class ProductDiscount extends SimpleDiscount {

    @Override
    public DiscountBasket getDiscountedBasket(User user, DiscountBasket discountBasket) {
        return discountBasket;
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
            List<DiscountBasket.DiscountProduct> resDiscountProducts = new ArrayList<>();

            for (DiscountBasket.DiscountProduct discountProduct: discountProducts) {
                resDiscountProducts.add(new DiscountBasket.DiscountProduct(discountProduct.getProductId(), discountProduct.getCategory(), discountProduct.getAmount(), discountProduct.getPrice()));
            }

            DiscountBasket resBasket = new DiscountBasket(resDiscountProducts);

            for (DiscountBasket.DiscountProduct discountProduct: resDiscountProducts) {
                if (discountProduct.getProductId() == productId) {
                    double curPrice = discountProduct.getPrice();
                    discountProduct.setPrice(salePercentage * curPrice);
                }
            }
            return resBasket;
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
            List<DiscountBasket.DiscountProduct> resDiscountProducts = new ArrayList<>();

            for (DiscountBasket.DiscountProduct discountProduct: discountProducts) {
                resDiscountProducts.add(new DiscountBasket.DiscountProduct(discountProduct.getProductId(), discountProduct.getCategory(), discountProduct.getAmount(), discountProduct.getPrice()));
            }

            DiscountBasket resBasket = new DiscountBasket(resDiscountProducts);

            for (DiscountBasket.DiscountProduct discountProduct: resDiscountProducts) {
                //java.lang.System.out.println(discountProduct.getProductId());
                java.lang.System.out.println(System.getInstance().getProductInfoById(5));



                if (System.getInstance().getProductInfoById(discountProduct.getProductId()).getCategory().equals(categoryName)) {
                    double curPrice = discountProduct.getPrice();
                    discountProduct.setPrice(salePercentage * curPrice);
                }
            }
            return resBasket;
        }
    }

}

