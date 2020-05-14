package com.example.communicationLayer.controllers;


import DTOs.ActionResultDTO;
import DTOs.StorePurchaseHistoryDTO;
import DTOs.SubscriberActionResultDTO;
import Service.OwnerHandler;
import org.springframework.web.bind.annotation.*;

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
    public ActionResultDTO addProductToStore(@RequestParam(value = "storeId", defaultValue = "") int storeId,
                                             @RequestParam(value = "productId", defaultValue = "") int productId,
                                             @RequestParam(value = "amount", defaultValue = "") int amount){
        return ownerHandler.addProductToStore(storeId,productId,amount);
    }


    @GetMapping("/editProductToStore")
    @ResponseBody
    public ActionResultDTO editProductToStore(@RequestParam(value = "storeId", defaultValue = "") int storeId,
                                              @RequestParam(value = "productId", defaultValue = "") int productId,
                                              @RequestParam(value = "info", defaultValue = "") String info){
        return ownerHandler.editProductToStore(storeId,productId,info);
    }

    @GetMapping("/OwnerDeleteProductFromStore")
    @ResponseBody
    public ActionResultDTO deleteProductFromStore(@RequestParam(value = "storeId", defaultValue = "") int storeId,
                                                  @RequestParam(value = "productId", defaultValue = "") int productId){
        return ownerHandler.deleteProductFromStore(storeId,productId);
    }

    @GetMapping("/addStoreOwner")
    @ResponseBody
    public ActionResultDTO addStoreOwner(@RequestParam(value = "storeId", defaultValue = "") int storeId,
                                         @RequestParam(value = "subId", defaultValue = "") int subId){
        return ownerHandler.addStoreOwner(storeId,subId);
    }

    @GetMapping("/addStoreManager")
    @ResponseBody
    public ActionResultDTO addStoreManager(@RequestParam(value = "storeId", defaultValue = "") int storeId,
                                           @RequestParam(value = "userId", defaultValue = "") int userId){
        return ownerHandler.addStoreManager(storeId,userId);
    }


    @GetMapping("/editManageOptions")
    @ResponseBody
    public ActionResultDTO editManageOptions(@RequestParam(value = "storeId", defaultValue = "") int storeId,
                                             @RequestParam(value = "userId", defaultValue = "") int userId,
                                             @RequestParam(value = "options", defaultValue = "") String options){
        return ownerHandler.editManageOptions(storeId,userId,options);
    }


    @GetMapping("/deleteManager")
    @ResponseBody
    public ActionResultDTO deleteManager(@RequestParam(value = "storeId", defaultValue = "") int storeId,
                                         @RequestParam(value = "userId", defaultValue = "") int userId){
        return ownerHandler.deleteManager(storeId,userId);
    }


    @GetMapping("/viewPurchaseHistory")
    @ResponseBody
    public StorePurchaseHistoryDTO viewPurchaseHistory(@RequestParam(value = "storeId", defaultValue = "") int storeId){
        return ownerHandler.viewPurchaseHistory(storeId);
    }

    @GetMapping("/changeBuyingPolicy")
    @ResponseBody
    public ActionResultDTO changeBuyingPolicy(@RequestParam(value = "storeId", defaultValue = "") int storeId,
                                              @RequestParam(value = "newPolicy", defaultValue = "") String newPolicy){
        return ownerHandler.changeBuyingPolicy(storeId, newPolicy);
    }

    @GetMapping("/getOptionalManagers")
    @ResponseBody
    public SubscriberActionResultDTO getOptionalManagers(@RequestParam(value = "storeId" , defaultValue = "-1")int storeId){
        return ownerHandler.getPossibleManagers(storeId);
    }

}
