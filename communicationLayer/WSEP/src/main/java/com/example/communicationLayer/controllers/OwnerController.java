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
    public ActionResultDTO addProductToStore(@RequestParam(value = "sessionId", defaultValue = "") int sessionId,
                                             @RequestParam(value = "storeId", defaultValue = "") int storeId,
                                             @RequestParam(value = "productId", defaultValue = "") int productId,
                                             @RequestParam(value = "amount", defaultValue = "") int amount){
        return new OwnerHandler(sessionId).addProductToStore(storeId,productId,amount);
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

    @GetMapping("/getOptionalManagers")
    @ResponseBody
    public SubscriberActionResultDTO getOptionalManagers(@RequestParam(value = "sessionId", defaultValue = "") int sessionId,
                                                         @RequestParam(value = "storeId" , defaultValue = "-1")int storeId){
        return new OwnerHandler(sessionId).getPossibleManagers(storeId);
    }

}
