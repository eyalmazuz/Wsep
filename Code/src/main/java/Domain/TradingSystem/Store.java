package Domain.TradingSystem;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Store {
    private int id;
    private List<ProductInStore> products;
    private List <User> managers;


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

    public boolean editProduct(int productId, String info) {
        for (ProductInStore product:products){
            if (product.getId() == productId){
                product.editInfo(info);
                return true;
            }
        }
        return false;

    }


    public boolean deleteProduct(int productId) {
        for (ProductInStore product: products){
            if (product.getId() == productId){
                products.remove(product);
                return true;
            }
        }
        return false;
    }

    public List<Subscriber> getOwners() {
        List <Subscriber> owners = new LinkedList<Subscriber>();
        for (User manager: managers){
            if (manager.hasOwnerPermission())
                owners.add((Subscriber) manager.getState());
        }
        return owners;
    }

    public void addOwner(Subscriber newOwner, User grantor) {
        for (User manager: managers){
            if (manager.getState().equals(newOwner))
                manager.addPermission("Owner", this);
        }


    }
}
