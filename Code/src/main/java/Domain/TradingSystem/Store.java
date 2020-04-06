package Domain.TradingSystem;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Store {
    private int id;
    private List<ProductInStore> products;


    public int getId() {
        return id;
    }

    public void addProduct(int productId, int amount) {
        AtomicBoolean found = new AtomicBoolean(false);
        for(ProductInStore p : products){
            if(p.getId() == productId){
                p.addAmount(amount);
                found.set(true);
            }
        }

        if(!found.get()){
            ProductInStore newProduct = new ProductInStore(productId,amount);
            products.add(newProduct);
        }
    }
}
