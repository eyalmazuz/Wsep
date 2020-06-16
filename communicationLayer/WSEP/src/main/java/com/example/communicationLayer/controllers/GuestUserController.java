package com.example.communicationLayer.controllers;

import DTOs.*;
import Service.GuestUserHandler;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;

@CrossOrigin()
@RestController
public class GuestUserController {
    GuestUserHandler guestUserHandler = new GuestUserHandler();

    @GetMapping("/login")
    @ResponseBody
    public IntActionResultDto login(
            @RequestParam(value = "sessionId", defaultValue = "-1") int sessionId,
            @RequestParam(value = "username", defaultValue = "") String username,
            @RequestParam(value = "password", defaultValue = "") String password) {
        return guestUserHandler.login(sessionId,username,password);
    }

    @GetMapping("/register")
    @ResponseBody
    public IntActionResultDto register(
            @RequestParam(value = "sessionId", defaultValue = "-1") int sessionId,
            @RequestParam(value = "username", defaultValue = "") String username,
            @RequestParam(value = "password", defaultValue = "") String password) {
        return guestUserHandler.register(sessionId,username,password);
    }


    @GetMapping("/addProductToCart")
    @ResponseBody
    public ActionResultDTO addProductToCart(
            @RequestParam(value = "sessionId", defaultValue = "-1") int sessionId,
            @RequestParam(value = "storeId", defaultValue = "-1") int storeId,
            @RequestParam(value = "productId", defaultValue = "-1") int productId,
            @RequestParam(value = "amount", defaultValue = "-1") int amount) {
        return guestUserHandler.addProductToCart(sessionId,storeId,productId,amount);
    }


    @GetMapping("/editProductInCart")
    @ResponseBody
    public ActionResultDTO editProductInCart(
            @RequestParam(value = "sessionId", defaultValue = "-1") int sessionId,
            @RequestParam(value = "storeId", defaultValue = "-1") int storeId,
            @RequestParam(value = "productId", defaultValue = "-1") int productId,
            @RequestParam(value = "amount", defaultValue = "-1") int amount) {
        return guestUserHandler.editProductInCart(sessionId, storeId, productId, amount);
    }

    @GetMapping("/removeProductInCart")
    @ResponseBody
    public ActionResultDTO removeProductInCart(@RequestParam(value = "sessionId", defaultValue = "-1") int sessionId,
                                               @RequestParam(value = "storeId", defaultValue = "-1") int storeId,
                                               @RequestParam(value = "productId", defaultValue = "-1") int productId) {
        return guestUserHandler.removeProductInCart(sessionId, storeId, productId);
    }

    @GetMapping("/clearCart")
    @ResponseBody
    public ActionResultDTO clearCart(@RequestParam(value = "sessionId", defaultValue = "-1") int sessionId) {
        return guestUserHandler.clearCart(sessionId);
    }

    /*
    @GetMapping("/setPaymentDetails")
    @ResponseBody
    public boolean setPaymentDetails(@RequestParam(value = "sessionId", defaultValue = "") int sessionId,
                                     @RequestParam(value = "paymentDetails", defaultValue = "") String paymentDetails) {
        return guestUserHandler.setPaymentDetails(sessionId, paymentDetails);
    }
    */


    @GetMapping("/requestPurchase")
    @ResponseBody
    public ActionResultDTO requestPurchase(@RequestParam(value = "sessionId", defaultValue = "-1") int sessionId) {
        return guestUserHandler.requestPurchase(sessionId);
    }

    @GetMapping("/confirmPurchase")
    @ResponseBody
    public ActionResultDTO confirmPurchase(@RequestParam(value = "sessionId", defaultValue = "-1") int sessionId,
                                           @RequestParam(value = "cardNumber", defaultValue = "") String cardNumber,
                                           @RequestParam(value = "cardMonth", defaultValue = "") String cardMonth,
                                           @RequestParam(value = "cardYear", defaultValue = "") String cardYear,
                                           @RequestParam(value = "cardHolder", defaultValue = "") String cardHolder,
                                           @RequestParam(value = "cardCcv", defaultValue = "") String cardCcv,
                                           @RequestParam(value = "cardId", defaultValue = "") String cardId,
                                           @RequestParam(value = "buyerName", defaultValue = "") String buyerName,
                                           @RequestParam(value = "address", defaultValue = "") String address,
                                           @RequestParam(value = "city", defaultValue = "") String city,
                                           @RequestParam(value = "country", defaultValue = "") String country,
                                           @RequestParam(value = "zip", defaultValue = "") String zip
                                           ) {
       return guestUserHandler.confirmPurchase(sessionId, cardNumber, cardMonth, cardYear, cardHolder, cardCcv, cardId, buyerName, address, city, country, zip);
        }


    @GetMapping("/searchProducts")
    @ResponseBody
    public ProductsActionResultDTO searchProducts(
            @RequestParam(value = "sessionId", defaultValue = "-1") int sessionId,
            @RequestParam(value = "productName", defaultValue = "") String productName,
            @RequestParam(value = "categoryName", defaultValue = "") String categoryName,
            @RequestParam(value = "keywords", defaultValue = "") String[] keywords,
            @RequestParam(value = "minItemRating", defaultValue = "-1") int minItemRating,
            @RequestParam(value = "minStoreRating", defaultValue = "-1") int minStoreRating) {
        return guestUserHandler.searchProducts(sessionId,productName,categoryName,keywords,minItemRating,minStoreRating);
    }

    @GetMapping("/viewStoreProductInfo")
    @ResponseBody
    public StoreActionResultDTO viewStoreProductInfo() {
        return guestUserHandler.viewStoreProductInfo();
    }

    @GetMapping("/getStore")
    @ResponseBody
    public StoreActionResultDTO viewStoreProductInfo(@RequestParam(value = "storeId", defaultValue = "-1") int storeId) {
        return guestUserHandler.getStore(storeId);
    }

    @GetMapping("/viewCart")
    @ResponseBody
    public ShoppingCartDTO viewCart(@RequestParam(value = "sessionId", defaultValue = "-1") int sessionId) {
        return guestUserHandler.viewCart(sessionId);
    }

    @GetMapping("/viewBuyingPolicies")
    @ResponseBody
    public BuyingPolicyActionResultDTO viewBuyingPolicies(
            @RequestParam(value = "sessionId", defaultValue = "") int sessionId,
            @RequestParam(value = "storeId", defaultValue = "") int storeId
    ) {
        return guestUserHandler.viewBuyingPolicies(sessionId, storeId);
    }
}
