package com.example.communicationLayer.controllers;


import DTOs.ActionResultDTO;
import DTOs.BuyingPolicyActionResultDTO;
import DTOs.IntActionResultDto;
import DTOs.StorePurchaseHistoryDTO;
import Service.ManagerHandler;
import Service.OwnerHandler;
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
                                             @RequestParam(value = "amount", defaultValue = "") int amount){
        return new ManagerHandler(sessionId).addProductToStore(storeId,productId,productName,productCategory,amount);
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
}
