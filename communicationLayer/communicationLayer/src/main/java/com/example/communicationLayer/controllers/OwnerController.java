package com.example.communicationLayer.controllers;


import Service.OwnerHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OwnerController {

    OwnerHandler ownerHandler;

    @GetMapping("/OwnerHandler")
    public void OwnerHandler(@RequestParam(value = "sessionId", defaultValue = "") int sessionId){
        ownerHandler = new OwnerHandler(sessionId);
    }

    @GetMapping("/addProductToStore")
    @ResponseBody
    public boolean addProductToStore(@RequestParam(value = "storeId", defaultValue = "") int storeId,
                                    @RequestParam(value = "productId", defaultValue = "") int productId,
                                    @RequestParam(value = "amount", defaultValue = "") int amount){
        return ownerHandler.addProductToStore(storeId,productId,amount);
    }


    @GetMapping("/editProductToStore")
    @ResponseBody
    public boolean editProductToStore(@RequestParam(value = "storeId", defaultValue = "") int storeId,
                                      @RequestParam(value = "productId", defaultValue = "") int productId,
                                      @RequestParam(value = "info", defaultValue = "") String info){
        return ownerHandler.editProductToStore(storeId,productId,info);
    }

    @GetMapping("/OwnerDeleteProductFromStore")
    @ResponseBody
    public boolean deleteProductFromStore(@RequestParam(value = "storeId", defaultValue = "") int storeId,
                                          @RequestParam(value = "productId", defaultValue = "") int productId){
        return ownerHandler.deleteProductFromStore(storeId,productId);
    }

    @GetMapping("/addStoreOwner")
    @ResponseBody
    public boolean addStoreOwner(@RequestParam(value = "storeId", defaultValue = "") int storeId,
                                 @RequestParam(value = "subId", defaultValue = "") int subId){
        return ownerHandler.addStoreOwner(storeId,subId);
    }

    @GetMapping("/addStoreManager")
    @ResponseBody
    public boolean addStoreManager(@RequestParam(value = "storeId", defaultValue = "") int storeId,
                                   @RequestParam(value = "userId", defaultValue = "") int userId){
        return ownerHandler.addStoreManager(storeId,userId);
    }


    @GetMapping("/editManageOptions")
    @ResponseBody
    public boolean editManageOptions(@RequestParam(value = "storeId", defaultValue = "") int storeId,
                                     @RequestParam(value = "userId", defaultValue = "") int userId,
                                     @RequestParam(value = "options", defaultValue = "") String options){
        return ownerHandler.editManageOptions(storeId,userId,options);
    }


    @GetMapping("/deleteManager")
    @ResponseBody
    public boolean deleteManager(@RequestParam(value = "storeId", defaultValue = "") int storeId,
                                 @RequestParam(value = "userId", defaultValue = "") int userId){
        return ownerHandler.deleteManager(storeId,userId);
    }


    @GetMapping("/viewPurchaseHistory")
    @ResponseBody
    public String viewPurchaseHistory(@RequestParam(value = "storeId", defaultValue = "") int storeId){
        return ownerHandler.viewPurchaseHistory(storeId);
    }

    @GetMapping("/changeBuyingPolicy")
    @ResponseBody
    public boolean changeBuyingPolicy(@RequestParam(value = "storeId", defaultValue = "") int storeId,
                                      @RequestParam(value = "newPolicy", defaultValue = "") String newPolicy){
        return ownerHandler.changeBuyingPolicy(storeId, newPolicy);
    }

}
