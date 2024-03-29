package com.example.communicationLayer.controllers;


import DTOs.*;
import Service.OwnerHandler;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin()
@RestController
public class OwnerController {

    OwnerHandler ownerHandler;

    @GetMapping("/OwnerHandler")
    public void OwnerHandler(@RequestParam(value = "sessionId", defaultValue = "") int sessionId){
        ownerHandler = new OwnerHandler(sessionId);
    }

    @GetMapping("/addProductToStore")
    @ResponseBody
    public ActionResultDTO addProductToStore(@RequestParam(value = "sessionId", defaultValue = "") int sessionId,
                                             @RequestParam(value = "storeId", defaultValue = "") int storeId,
                                             @RequestParam(value = "productId", defaultValue = "") int productId,
                                             @RequestParam(value = "amount", defaultValue = "") int amount){
        return new OwnerHandler(sessionId).addProductToStore(storeId,productId,amount);
    }

    @GetMapping("/addNewProductToStore")
    @ResponseBody
    public ActionResultDTO addProductToStore(@RequestParam(value = "sessionId", defaultValue = "") int sessionId,
                                             @RequestParam(value = "storeId", defaultValue = "") int storeId,
                                             @RequestParam(value = "productId", defaultValue = "") int productId,
                                             @RequestParam(value = "prodectName",defaultValue = "") String productName,
                                             @RequestParam(value="productCategory",defaultValue = "") String productCategory,
                                             @RequestParam(value = "amount", defaultValue = "") int amount,
                                             @RequestParam(value = "basePrice", defaultValue = "") double basePrice){
        return new OwnerHandler(sessionId).addProductToStore(storeId,productId,productName,productCategory,amount,basePrice);
    }


    @GetMapping("/editProductToStore")
    @ResponseBody
    public ActionResultDTO editProductToStore(@RequestParam(value = "sessionId", defaultValue = "") int sessionId,
                                              @RequestParam(value = "storeId", defaultValue = "") int storeId,
                                              @RequestParam(value = "productId", defaultValue = "") int productId,
                                              @RequestParam(value = "info", defaultValue = "") String info){
        return new OwnerHandler(sessionId).editProductToStore(storeId,productId,info);
    }

    @GetMapping("/OwnerDeleteProductFromStore")
    @ResponseBody
    public ActionResultDTO deleteProductFromStore(@RequestParam(value = "sessionId", defaultValue = "") int sessionId,
                                                  @RequestParam(value = "storeId", defaultValue = "") int storeId,
                                                  @RequestParam(value = "productId", defaultValue = "") int productId){
        return new OwnerHandler(sessionId).deleteProductFromStore(storeId,productId);
    }

    @GetMapping("/addStoreOwner")
    @ResponseBody
    public ActionResultDTO addStoreOwner(@RequestParam(value = "sessionId", defaultValue = "") int sessionId,
                                         @RequestParam(value = "storeId", defaultValue = "") int storeId,
                                         @RequestParam(value = "subId", defaultValue = "") int subId){
        return new OwnerHandler(sessionId).addStoreOwner(storeId,subId);
    }

    @GetMapping("/approveStoreOwner")
    @ResponseBody
    public ActionResultDTO approveStoreOwner(@RequestParam(value = "sessionId", defaultValue = "") int sessionId,
                                         @RequestParam(value = "storeId", defaultValue = "") int storeId,
                                         @RequestParam(value = "subId", defaultValue = "") int subId){
        return new OwnerHandler(sessionId).approveStoreOwner(storeId,subId);
    }

    @GetMapping("/declineStoreOwner")
    @ResponseBody
    public ActionResultDTO declineStoreOwner(@RequestParam(value = "sessionId", defaultValue = "") int sessionId,
                                             @RequestParam(value = "storeId", defaultValue = "") int storeId,
                                             @RequestParam(value = "subId", defaultValue = "") int subId){
        return new OwnerHandler(sessionId).declineStoreOwner(storeId,subId);
    }

    @GetMapping("/getGrantings")
    @ResponseBody
    public GrantingResultDTO getGrantings(@RequestParam(value = "sessionId", defaultValue = "") int sessionId,
                                          @RequestParam(value = "subId", defaultValue = "") int subId)
    {
        return new OwnerHandler(sessionId).getAllGrantings(subId);
    }


    @GetMapping("/addStoreManager")
    @ResponseBody
    public ActionResultDTO addStoreManager(@RequestParam(value = "sessionId", defaultValue = "") int sessionId,
                                           @RequestParam(value = "storeId", defaultValue = "") int storeId,
                                           @RequestParam(value = "userId", defaultValue = "") int userId){
        return new OwnerHandler(sessionId).addStoreManager(storeId,userId);
    }


    @GetMapping("/editManageOptions")
    @ResponseBody
    public ActionResultDTO editManageOptions(@RequestParam(value = "sessionId", defaultValue = "") int sessionId,
                                             @RequestParam(value = "storeId", defaultValue = "") int storeId,
                                             @RequestParam(value = "userId", defaultValue = "") int userId,
                                             @RequestParam(value = "options", defaultValue = "") String options){
        return new OwnerHandler(sessionId).editManageOptions(storeId,userId,options);
    }


    @GetMapping("/deleteManager")
    @ResponseBody
    public ActionResultDTO deleteManager(@RequestParam(value = "sessionId", defaultValue = "") int sessionId,
                                         @RequestParam(value = "storeId", defaultValue = "") int storeId,
                                         @RequestParam(value = "userId", defaultValue = "") int userId){
        return new OwnerHandler(sessionId).deleteManager(storeId,userId);
    }

    @GetMapping("/deleteOwner")
    @ResponseBody
    public ActionResultDTO deleteOwner(@RequestParam(value = "sessionId", defaultValue = "") int sessionId,
                                         @RequestParam(value = "storeId", defaultValue = "") int storeId,
                                         @RequestParam(value = "userId", defaultValue = "") int userId){
        return new OwnerHandler(sessionId).deleteOwner(storeId,userId);
    }


    @GetMapping("/viewPurchaseHistory")
    @ResponseBody
    public StorePurchaseHistoryDTO viewPurchaseHistory(@RequestParam(value = "sessionId", defaultValue = "") int sessionId,
                                                       @RequestParam(value = "storeId", defaultValue = "") int storeId){
        return new OwnerHandler(sessionId).viewPurchaseHistory(storeId);
    }

    @GetMapping("/changeBuyingPolicy")
    @ResponseBody
    public ActionResultDTO changeBuyingPolicy(@RequestParam(value = "sessionId", defaultValue = "") int sessionId,
                                              @RequestParam(value = "storeId", defaultValue = "") int storeId,
                                              @RequestParam(value = "newPolicy", defaultValue = "") String newPolicy){
        return new OwnerHandler(sessionId).changeBuyingPolicy(storeId, newPolicy);
    }

    @GetMapping("/changeDiscountPolicy")
    @ResponseBody
    public ActionResultDTO changeDiscountPolicy(@RequestParam(value = "sessionId", defaultValue = "") int sessionId,
                                              @RequestParam(value = "storeId", defaultValue = "") int storeId,
                                              @RequestParam(value = "newPolicy", defaultValue = "") String newPolicy){
        return new OwnerHandler(sessionId).changeDiscountPolicy(storeId, newPolicy);
    }


    @GetMapping("/getOptionalManagers")
    @ResponseBody
    public SubscriberActionResultDTO getOptionalManagers(@RequestParam(value = "sessionId", defaultValue = "") int sessionId,
                                                         @RequestParam(value = "storeId" , defaultValue = "-1")int storeId){
        return new OwnerHandler(sessionId).getPossibleManagers(storeId);
    }

    @GetMapping("/OwnerAddSimpleBuyingTypeBasketConstraint")
    @ResponseBody
    public IntActionResultDto addSimpleBuyingTypeBasketConstraint(
            @RequestParam(value = "sessionId", defaultValue = "") int sessionId,
            @RequestParam(value = "storeId", defaultValue = "") int storeId,
            @RequestParam(value = "productId", defaultValue = "") int productId,
            @RequestParam(value = "minmax", defaultValue = "") String minmax,
            @RequestParam(value = "amount", defaultValue = "") int amount
    ) {
        return new OwnerHandler(sessionId).addSimpleBuyingTypeBasketConstraint(storeId, productId, minmax, amount);
    }

    @GetMapping("/OwnerAddSimpleBuyingTypeUserConstraint")
    @ResponseBody
    public IntActionResultDto addSimpleBuyingTypeUserConstraint(
            @RequestParam(value = "sessionId", defaultValue = "") int sessionId,
            @RequestParam(value = "storeId", defaultValue = "") int storeId,
            @RequestParam(value = "country", defaultValue = "") String country
    ) {
        return new OwnerHandler(sessionId).addSimpleBuyingTypeUserConstraint(storeId, country);
    }

    @GetMapping("/OwnerAddSimpleBuyingTypeSystemConstraint")
    @ResponseBody
    public IntActionResultDto addSimpleBuyingTypeSystemConstraint(
            @RequestParam(value = "sessionId", defaultValue = "") int sessionId,
            @RequestParam(value = "storeId", defaultValue = "") int storeId,
            @RequestParam(value = "dayOfWeek", defaultValue = "") int dayOfWeek
    ) {
        return new OwnerHandler(sessionId).addSimpleBuyingTypeSystemConstraint(storeId, dayOfWeek);
    }

    @GetMapping("/OwnerRemoveBuyingType")
    @ResponseBody
    public ActionResultDTO removeBuyingType(
            @RequestParam(value = "sessionId", defaultValue = "") int sessionId,
            @RequestParam(value = "storeId", defaultValue = "") int storeId,
            @RequestParam(value = "buyingTypeID", defaultValue = "") int buyingTypeID
    ) {
        return new OwnerHandler(sessionId).removeBuyingType(storeId, buyingTypeID);
    }

    @GetMapping("/OwnerRemoveAllBuyingTypes")
    @ResponseBody
    public ActionResultDTO removeAllBuyingTypes(
            @RequestParam(value = "sessionId", defaultValue = "") int sessionId,
            @RequestParam(value = "storeId", defaultValue = "") int storeId
    ) {
        return new OwnerHandler(sessionId).removeAllBuyingTypes(storeId);
    }

    @GetMapping("/OwnerCreateAdvancedBuyingType")
    @ResponseBody
    public ActionResultDTO createAdvancedBuyingType(
            @RequestParam(value = "sessionId", defaultValue = "") int sessionId,
            @RequestParam(value = "storeId", defaultValue = "") int storeId,
            @RequestParam(value = "buyingTypeIDs", defaultValue = "") List<Integer> buyingTypeIDs,
            @RequestParam(value = "logicalOperation", defaultValue = "") String logicalOperation
    ) {
        return new OwnerHandler(sessionId).createAdvancedBuyingType(storeId, buyingTypeIDs, logicalOperation);
    }

    @GetMapping("/OwnerViewBuyingPolicies")
    @ResponseBody
    public BuyingPolicyActionResultDTO viewBuyingPolicies(
            @RequestParam(value = "sessionId", defaultValue = "") int sessionId,
            @RequestParam(value = "storeId", defaultValue = "") int storeId
    ) {
        return new OwnerHandler(sessionId).viewBuyingPolicies(sessionId, storeId);
    }

    @GetMapping("/OwnerViewDiscountPolicies")
    @ResponseBody
    public DiscountPolicyActionResultDTO viewDiscountPolicies(
            @RequestParam(value = "sessionId", defaultValue = "") int sessionId,
            @RequestParam(value = "storeId", defaultValue = "") int storeId
    ) {
        return new OwnerHandler(sessionId).viewDiscountPolicies(sessionId, storeId);
    }

    @GetMapping("/OwnerChangeProductPrice")
    @ResponseBody
    public ActionResultDTO changeProductPrice(
            @RequestParam(value = "sessionId", defaultValue = "") int sessionId,
            @RequestParam(value = "storeId", defaultValue = "") int storeId,
            @RequestParam(value = "productId", defaultValue = "") int productId,
            @RequestParam(value = "price", defaultValue = "") double price
    ) {
        return new OwnerHandler(sessionId).changeProductPrice(storeId, productId, price);
    }

    // discount policies

    @GetMapping("/OwnerAddSimpleProductDiscount")
    @ResponseBody
    public IntActionResultDto addSimpleProductDiscount(
            @RequestParam(value = "sessionId", defaultValue = "") int sessionId,
            @RequestParam(value = "storeId", defaultValue = "") int storeId,
            @RequestParam(value = "productId", defaultValue = "") int productId,
            @RequestParam(value = "salePercentage", defaultValue = "") double salePercentage
    ) {
        return new OwnerHandler(sessionId).addSimpleProductDiscount(storeId, productId, salePercentage);
    }

    @GetMapping("/OwnerAddSimpleCategoryDiscount")
    @ResponseBody
    public IntActionResultDto addSimpleCategoryDiscount(
            @RequestParam(value = "sessionId", defaultValue = "") int sessionId,
            @RequestParam(value = "storeId", defaultValue = "") int storeId,
            @RequestParam(value = "categoryName", defaultValue = "") String categoryName,
            @RequestParam(value = "salePercentage", defaultValue = "") double salePercentage
    ) {
        return new OwnerHandler(sessionId).addSimpleCategoryDiscount(storeId, categoryName, salePercentage);
    }


    @GetMapping("/OwnerRemoveDiscountType")
    @ResponseBody
    public ActionResultDTO removeDiscountType(
            @RequestParam(value = "sessionId", defaultValue = "") int sessionId,
            @RequestParam(value = "storeId", defaultValue = "") int storeId,
            @RequestParam(value = "discountTypeID", defaultValue = "") int discountTypeID
    ) {
        return new OwnerHandler(sessionId).removeDiscountType(storeId, discountTypeID);
    }

    @GetMapping("/OwnerRemoveAllDiscountTypes")
    @ResponseBody
    public ActionResultDTO removeAllDiscountTypes(
            @RequestParam(value = "sessionId", defaultValue = "") int sessionId,
            @RequestParam(value = "storeId", defaultValue = "") int storeId
    ) {
        return new OwnerHandler(sessionId).removeAllDiscountTypes(storeId);
    }

    @GetMapping("/OwnerCreateAdvancedDiscountType")
    @ResponseBody
    public ActionResultDTO createAdvancedDiscountType(
            @RequestParam(value = "sessionId", defaultValue = "") int sessionId,
            @RequestParam(value = "storeId", defaultValue = "") int storeId,
            @RequestParam(value = "discountTypeIDs", defaultValue = "") List<Integer> discountTypeIDs,
            @RequestParam(value = "logicalOperation", defaultValue = "") String logicalOperation
    ) {
        return new OwnerHandler(sessionId).createAdvancedDiscountType(storeId, discountTypeIDs, logicalOperation);
    }


}
