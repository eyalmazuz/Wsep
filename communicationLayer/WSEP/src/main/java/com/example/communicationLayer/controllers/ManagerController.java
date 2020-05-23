package com.example.communicationLayer.controllers;


import DTOs.*;
import Service.ManagerHandler;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin()
@RestController
public class ManagerController {

    private ManagerHandler managerHandler ;


    @GetMapping("/ManagerHandler")
    public void ManagerHandler(@RequestParam(value = "sessionId", defaultValue = "") int sessionId){
        managerHandler = new ManagerHandler(sessionId);
    }
    @GetMapping("/ManagerAddProductToStore")
    @ResponseBody
    public ActionResultDTO addProductToStore(
            @RequestParam(value = "sessionId", defaultValue = "") int sessionId,
            @RequestParam(value = "storeId", defaultValue = "") int storeId,
            @RequestParam(value = "productId", defaultValue = "") int productId,
            @RequestParam(value = "amount", defaultValue = "") int amount){
        return new ManagerHandler(sessionId).addProductToStore(storeId,productId,amount);
    }

    @GetMapping("/ManagerAddNewProductToStore")
    @ResponseBody
    public ActionResultDTO addProductToStore(@RequestParam(value = "sessionId", defaultValue = "") int sessionId,
                                             @RequestParam(value = "storeId", defaultValue = "") int storeId,
                                             @RequestParam(value = "productId", defaultValue = "") int productId,
                                             @RequestParam(value = "prodectName",defaultValue = "") String productName,
                                             @RequestParam(value="productCategory",defaultValue = "") String productCategory,
                                             @RequestParam(value = "amount", defaultValue = "") int amount,
                                             @RequestParam(value = "basePrice", defaultValue = "") double basePrice){
        return new ManagerHandler(sessionId).addProductToStore(storeId,productId,productName,productCategory,amount,basePrice);
    }


    @GetMapping("/ManagerEditProductToStoreManager")
    @ResponseBody
    public ActionResultDTO editProductToStore(
            @RequestParam(value = "sessionId", defaultValue = "") int sessionId,
            @RequestParam(value = "storeId", defaultValue = "") int storeId,
            @RequestParam(value = "productId", defaultValue = "") int productId,
            @RequestParam(value = "info", defaultValue = "") String info){
            return new ManagerHandler(sessionId).editProductToStore(storeId, productId, info);
    }

    @GetMapping("/ManagerDeleteProductFromStore")
    @ResponseBody
    public ActionResultDTO deleteProductFromStore(
            @RequestParam(value = "sessionId", defaultValue = "") int sessionId,
            @RequestParam(value = "storeId", defaultValue = "") int storeId,
            @RequestParam(value = "productId", defaultValue = "") int productId
            ){
            return new ManagerHandler(sessionId).deleteProductFromStore(storeId,productId);

    }

    @GetMapping("/ManagerViewPurchaseHistory")
    @ResponseBody
    public StorePurchaseHistoryDTO viewPurchaseHistory(@RequestParam(value = "sessionId", defaultValue = "") int sessionId,
                                                       @RequestParam(value = "storeId", defaultValue = "") int storeId){
        return new ManagerHandler(sessionId).viewPurchaseHistory(storeId);
    }

    @GetMapping("/ManagerChangeBuyingPolicy")
    @ResponseBody
    public ActionResultDTO changeBuyingPolicy(
            @RequestParam(value = "sessionId", defaultValue = "") int sessionId,
            @RequestParam(value = "storeId", defaultValue = "") int storeId,
            @RequestParam(value = "newPolicy", defaultValue = "") String newPolicy){
        return new ManagerHandler(sessionId).changeBuyingPolicy(storeId,newPolicy);
    }

    @GetMapping("/ManagerAddSimpleBuyingTypeBasketConstraint")
    @ResponseBody
    public IntActionResultDto addSimpleBuyingTypeBasketConstraint(
            @RequestParam(value = "sessionId", defaultValue = "") int sessionId,
            @RequestParam(value = "storeId", defaultValue = "") int storeId,
            @RequestParam(value = "productId", defaultValue = "") int productId,
            @RequestParam(value = "minmax", defaultValue = "") String minmax,
            @RequestParam(value = "amount", defaultValue = "") int amount
    ) {
        return new ManagerHandler(sessionId).addSimpleBuyingTypeBasketConstraint(storeId, productId, minmax, amount);
    }

    @GetMapping("/ManagerAddSimpleBuyingTypeUserConstraint")
    @ResponseBody
    public IntActionResultDto addSimpleBuyingTypeUserConstraint(
            @RequestParam(value = "sessionId", defaultValue = "") int sessionId,
            @RequestParam(value = "storeId", defaultValue = "") int storeId,
            @RequestParam(value = "country", defaultValue = "") String country
    ) {
        return new ManagerHandler(sessionId).addSimpleBuyingTypeUserConstraint(storeId, country);
    }

    @GetMapping("/ManagerAddSimpleBuyingTypeSystemConstraint")
    @ResponseBody
    public IntActionResultDto addSimpleBuyingTypeSystemConstraint(
            @RequestParam(value = "sessionId", defaultValue = "") int sessionId,
            @RequestParam(value = "storeId", defaultValue = "") int storeId,
            @RequestParam(value = "dayOfWeek", defaultValue = "") int dayOfWeek
    ) {
        return new ManagerHandler(sessionId).addSimpleBuyingTypeSystemConstraint(storeId, dayOfWeek);
    }

    @GetMapping("/ManagerRemoveBuyingType")
    @ResponseBody
    public ActionResultDTO removeBuyingType(
            @RequestParam(value = "sessionId", defaultValue = "") int sessionId,
            @RequestParam(value = "storeId", defaultValue = "") int storeId,
            @RequestParam(value = "buyingTypeID", defaultValue = "") int buyingTypeID
    ) {
        return new ManagerHandler(sessionId).removeBuyingType(storeId, buyingTypeID);
    }

    @GetMapping("/ManagerRemoveAllBuyingTypes")
    @ResponseBody
    public ActionResultDTO removeAllBuyingTypes(
            @RequestParam(value = "sessionId", defaultValue = "") int sessionId,
            @RequestParam(value = "storeId", defaultValue = "") int storeId
    ) {
        return new ManagerHandler(sessionId).removeAllBuyingTypes(storeId);
    }

    @GetMapping("/ManagerCreateAdvancedBuyingType")
    @ResponseBody
    public ActionResultDTO createAdvancedBuyingType(
            @RequestParam(value = "sessionId", defaultValue = "") int sessionId,
            @RequestParam(value = "storeId", defaultValue = "") int storeId,
            @RequestParam(value = "buyingTypeIDs", defaultValue = "") List<Integer> buyingTypeIDs,
            @RequestParam(value = "logicalOperation", defaultValue = "") String logicalOperation
    ) {
        return new ManagerHandler(sessionId).createAdvancedBuyingType(storeId, buyingTypeIDs, logicalOperation);
    }

    @GetMapping("/ManagerViewBuyingPolicies")
    @ResponseBody
    public BuyingPolicyActionResultDTO viewBuyingPolicies(
            @RequestParam(value = "sessionId", defaultValue = "") int sessionId,
            @RequestParam(value = "storeId", defaultValue = "") int storeId
    ) {
        return new ManagerHandler(sessionId).viewBuyingPolicies(sessionId, storeId);
    }

    @GetMapping("/ManagerChangeProductPrice")
    @ResponseBody
    public ActionResultDTO changeProductPrice(
            @RequestParam(value = "sessionId", defaultValue = "") int sessionId,
            @RequestParam(value = "storeId", defaultValue = "") int storeId,
            @RequestParam(value = "productId", defaultValue = "") int productId,
            @RequestParam(value = "price", defaultValue = "") double price
    ) {
        return new ManagerHandler(sessionId).changeProductPrice(storeId, productId, price);
    }


    // discount policies

    @GetMapping("/ManagerChangeDiscountPolicy")
    @ResponseBody
    public ActionResultDTO changeDiscountPolicy(
            @RequestParam(value = "sessionId", defaultValue = "") int sessionId,
            @RequestParam(value = "storeId", defaultValue = "") int storeId,
            @RequestParam(value = "newPolicy", defaultValue = "") String newPolicy){
        return new ManagerHandler(sessionId).changeDiscountPolicy(storeId, newPolicy);
    }

    @GetMapping("/ManagerAddSimpleProductDiscount")
    @ResponseBody
    public IntActionResultDto addSimpleProductDiscount(
            @RequestParam(value = "sessionId", defaultValue = "") int sessionId,
            @RequestParam(value = "storeId", defaultValue = "") int storeId,
            @RequestParam(value = "productId", defaultValue = "") int productId,
            @RequestParam(value = "salePercentage", defaultValue = "") double salePercentage
    ) {
        return new ManagerHandler(sessionId).addSimpleProductDiscount(storeId, productId, salePercentage);
    }

    @GetMapping("/ManagerAddSimpleCategoryDiscount")
    @ResponseBody
    public IntActionResultDto addSimpleCategoryDiscount(
            @RequestParam(value = "sessionId", defaultValue = "") int sessionId,
            @RequestParam(value = "storeId", defaultValue = "") int storeId,
            @RequestParam(value = "categoryName", defaultValue = "") String categoryName,
            @RequestParam(value = "salePercentage", defaultValue = "") double salePercentage
    ) {
        return new ManagerHandler(sessionId).addSimpleCategoryDiscount(storeId, categoryName, salePercentage);
    }


    @GetMapping("/ManagerRemoveDiscountType")
    @ResponseBody
    public ActionResultDTO removeDiscountType(
            @RequestParam(value = "sessionId", defaultValue = "") int sessionId,
            @RequestParam(value = "storeId", defaultValue = "") int storeId,
            @RequestParam(value = "discountTypeID", defaultValue = "") int discountTypeID
    ) {
        return new ManagerHandler(sessionId).removeDiscountType(storeId, discountTypeID);
    }

    @GetMapping("/ManagerRemoveAllDiscountTypes")
    @ResponseBody
    public ActionResultDTO removeAllDiscountTypes(
            @RequestParam(value = "sessionId", defaultValue = "") int sessionId,
            @RequestParam(value = "storeId", defaultValue = "") int storeId
    ) {
        return new ManagerHandler(sessionId).removeAllDiscountTypes(storeId);
    }

    @GetMapping("/ManagerCreateAdvancedDiscountType")
    @ResponseBody
    public ActionResultDTO createAdvancedDiscountType(
            @RequestParam(value = "sessionId", defaultValue = "") int sessionId,
            @RequestParam(value = "storeId", defaultValue = "") int storeId,
            @RequestParam(value = "discountTypeIDs", defaultValue = "") List<Integer> discountTypeIDs,
            @RequestParam(value = "logicalOperation", defaultValue = "") String logicalOperation
    ) {
        return new ManagerHandler(sessionId).createAdvancedDiscountType(storeId, discountTypeIDs, logicalOperation);
    }

    @GetMapping("/ManagerViewDiscountPolicies")
    @ResponseBody
    public DiscountPolicyActionResultDTO viewDiscountPolicies(
            @RequestParam(value = "sessionId", defaultValue = "") int sessionId,
            @RequestParam(value = "storeId", defaultValue = "") int storeId
    ) {
        return new ManagerHandler(sessionId).viewDiscountPolicies(sessionId, storeId);
    }

}
