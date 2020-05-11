package DTOs;

import DTOs.SimpleDTOS.ProductInStoreDTO;

import java.util.List;

public class ProductsActionResultDTO extends ActionResultDTO {
    private List<ProductInStoreDTO> products;

    public ProductsActionResultDTO(ResultCode resultCode, String details, List<ProductInStoreDTO> products){
        super(resultCode,details);
        this.products = products;
    }

    public List<ProductInStoreDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductInStoreDTO> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        String results = "Results:\n\n";
        for (ProductInStoreDTO pis: products) {
            results += pis.toString() + "\n---------------------------------\n";
        }
        return results;
    }
}
