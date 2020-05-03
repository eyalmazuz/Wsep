package com.example.communicationLayer.controllers;

import Service.GuestUserHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class GuestUserController {
    GuestUserHandler guestUserHandler = new GuestUserHandler();

    @GetMapping("/login")
    @ResponseBody
    public boolean login(
            @RequestParam(value = "sessionId", defaultValue = "") int sessionId,
            @RequestParam(value = "username", defaultValue = "") String username,
            @RequestParam(value = "password", defaultValue = "") String password) {
        return guestUserHandler.login(sessionId,username,password);
    }

    @GetMapping("/register")
    @ResponseBody
    public int register(
            @RequestParam(value = "sessionId", defaultValue = "") int sessionId,
            @RequestParam(value = "username", defaultValue = "") String username,
            @RequestParam(value = "password", defaultValue = "") String password) {
        return guestUserHandler.register(sessionId,username,password);
    }


    @GetMapping("/addProductToCart")
    @ResponseBody
    public boolean addProductToCart(
            @RequestParam(value = "sessionId", defaultValue = "") int sessionId,
            @RequestParam(value = "storeId", defaultValue = "") int storeId,
            @RequestParam(value = "productId", defaultValue = "") int productId,
            @RequestParam(value = "amount", defaultValue = "") int amount) {
        return guestUserHandler.addProductToCart(sessionId,storeId,productId,amount);
    }


    @GetMapping("/editProductInCart")
    @ResponseBody
    public boolean editProductInCart(
            @RequestParam(value = "sessionId", defaultValue = "") int sessionId,
            @RequestParam(value = "storeId", defaultValue = "") int storeId,
            @RequestParam(value = "productId", defaultValue = "") int productId,
            @RequestParam(value = "amount", defaultValue = "") int amount) {
        return guestUserHandler.editProductInCart(sessionId, storeId, productId, amount);
    }

    @GetMapping("/removeProductInCart")
    @ResponseBody
    public boolean removeProductInCart(@RequestParam(value = "sessionId", defaultValue = "") int sessionId,
                                       @RequestParam(value = "storeId", defaultValue = "") int storeId,
                                       @RequestParam(value = "productId", defaultValue = "") int productId) {
        return guestUserHandler.removeProductInCart(sessionId, storeId, productId);
    }

    @GetMapping("/clearCart")
    @ResponseBody
    public boolean clearCart(@RequestParam(value = "sessionId", defaultValue = "") int sessionId) {
        return guestUserHandler.clearCart(sessionId);
    }

    @GetMapping("/setPaymentDetails")
    @ResponseBody
    public boolean setPaymentDetails(@RequestParam(value = "sessionId", defaultValue = "") int sessionId,
                                     @RequestParam(value = "paymentDetails", defaultValue = "") String paymentDetails) {
        return guestUserHandler.setPaymentDetails(sessionId, paymentDetails);
    }


    @GetMapping("/requestPurchase")
    @ResponseBody
    public boolean requestPurchase(@RequestParam(value = "sessionId", defaultValue = "") int sessionId) {
        return guestUserHandler.requestPurchase(sessionId);
    }

    @GetMapping("/confirmPurchase")
    @ResponseBody
    public boolean confirmPurchase(@RequestParam(value = "sessionId", defaultValue = "") int sessionId,
                                   @RequestParam(value = "paymentDetails", defaultValue = "") String paymentDetails) {
       return guestUserHandler.confirmPurchase(sessionId,paymentDetails);
        }


    @GetMapping("/searchProducts")
    @ResponseBody
    public String searchProducts(
            @RequestParam(value = "sessionId", defaultValue = "") int sessionId,
            @RequestParam(value = "productName", defaultValue = "") String productName,
            @RequestParam(value = "categoryName", defaultValue = "") String categoryName,
            @RequestParam(value = "keywords", defaultValue = "") String[] keywords,
            @RequestParam(value = "minItemRating", defaultValue = "") int minItemRating,
            @RequestParam(value = "minStoreRating", defaultValue = "") int minStoreRating) {
        return guestUserHandler.searchProducts(sessionId,productName,categoryName,keywords,minItemRating,minStoreRating);
    }

    @GetMapping("/viewStoreProductInfo")
    @ResponseBody
    public String viewStoreProductInfo() {
        return guestUserHandler.viewStoreProductInfo();
    }

    @GetMapping("/viewCart")
    @ResponseBody
    public String viewCart(@RequestParam(value = "sessionId", defaultValue = "") int sessionId) {
        return guestUserHandler.viewCart(sessionId);
    }
}
