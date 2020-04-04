package Service;

import AcceptanceTest.Data.*;

import java.util.ArrayList;
import java.util.List;

public class ProxyBridge implements Bridge {
    private RealBridge rb = null;


    public boolean login(String username, String password) {
        if (rb != null) {
            return rb.login(username, password);
        }
        else {
            return false;
        }
    }

    public boolean register(String username, String password) {
        if (rb != null) {
            return rb.register(username, password);
        }
        else {
            return false;
        }
    }

    public ArrayList<Store> getAllInfo(){
        if (rb != null) {
            return rb.getAllInfo();
        }
        else {
            return null;
        }

    }

    public List<Product> searchProducts(String name, String category, String keyword, String filterOptions) {
        if (rb != null) {
            return rb.searchProducts(name, category, keyword, filterOptions);
        }
        else {
           return null;

        }
    }

    public boolean addToCart(String productName, Integer amount) {
        if (rb != null) {
            return rb.addToCart(productName, amount);
        }
        else {
            return false;

        }
    }

    public boolean updateAmount(int amount) {
        if (rb != null) {
            return rb.updateAmount(amount);
        }
        else {
            return false;
        }
    }

    public boolean deleteItemInCart(String productName) {
        if (rb != null) {
            return rb.deleteItemInCart(productName);
        }
        else {
            return true;
        }
    }

    public boolean clearCart() {
        if (rb != null) {
            return rb.clearCart();
        }
        else {
            return true;
        }
    }

    public boolean buyCart(String user, String cart) {
        if (rb != null) {
            return rb.buyCart(user, cart);
        }
        else {
            return false;
        }
    }

    public ShoppingCart viewCart(){
        if (rb != null) {
            return rb.viewCart();
        }
        else {
            return null;
        }

    }

    public boolean logout(){
        if (rb != null){
            return rb.logout();
        }
        else{
            return true;
        }
    }

    public boolean openStore(){
        if (rb != null){
            return rb.logout();
        }
        else{
            return true;
        }
    }

    public List<History> viewPurchaseHistory(){
        if(rb != null){
            return rb.viewPurchaseHistory();
        }
        else{
            return null;
        }
    }

    public List<History> searchUserHistory(String username){
        if(rb != null){
            return rb.searchUserHistory(username);
        }
        else{
            return null;
        }
    }

    public List<History> searchStoreHistory(String storeName){
        if(rb != null){
            return rb.searchUserHistory(storeName);
        }
        else{
            return null;
        }
    }

    public boolean addProduct(int id, int amount){
        if(rb != null){
            return rb.addProduct(id, amount);
        }
        else{
            return false;
        }
    }

    public boolean editProduct(int id, int amount){
        if(rb != null){
            return rb.editProduct(id, amount);
        }
        else{
            return false;
        }
    }

    public boolean deleteProduct(int id){
            if(rb != null){
                return rb.deleteProduct(id);
            }
            else{
                return false;
            }
        }

    public boolean appointManager(String username){
        if(rb != null){
            return rb.appointManager(username);
        }
        else{
            return false;
        }
    }

    public boolean appointOwner(String username){
        if(rb != null){
            return rb.appointOwner(username);
        }
        else{
            return false;
        }
    }

    public boolean removeManager(int id){
        if(rb != null){
            return rb.removeManager(id);
        }
        else{
            return false;
        }
    }

    public boolean editManagerOptions(int id, int option){
        if(rb != null){
            return rb.editManagerOptions(id, option);
        }
        else{
            return false;
        }
    }

    public boolean updateItemDiscount(int itemID, int discount){
        if(rb != null){
            return rb.updateItemDiscount(itemID, discount);
        }
        else{
            return false;
        }
    }



    public List<History> viewShopHistory(){
        if(rb != null){
            return rb.viewShopHistory();
        }
        else{
            return null;
        }
    }


    public void setRealBridge(RealBridge realBridge) {
        rb = realBridge;
    }
}
