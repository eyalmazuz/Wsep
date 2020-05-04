package com.example.communicationLayer.controllers;


import Service.ManagerHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ManagerController {

    private ManagerHandler managerHandler ;


    @GetMapping("/ManagerHandler")
    public void ManagerHandler(@RequestParam(value = "sessionId", defaultValue = "") int sessionId){
        managerHandler = new ManagerHandler(sessionId);
    }
    @GetMapping("/addProductToStore")
    @ResponseBody
    public boolean addProductToStore(
            @RequestParam(value = "storeId", defaultValue = "") int storeId,
            @RequestParam(value = "productId", defaultValue = "") int productId,
            @RequestParam(value = "amount", defaultValue = "") int amount){
        return managerHandler.addProductToStore(storeId,productId,amount);
    }


    @GetMapping("/editProductToStore")
    @ResponseBody
    public boolean editProductToStore(
            @RequestParam(value = "storeId", defaultValue = "") int storeId,
            @RequestParam(value = "productId", defaultValue = "") int productId,
            @RequestParam(value = "info", defaultValue = "") String info){
            return managerHandler.editProductToStore(storeId, productId, info);
    }

    @GetMapping("/ManagerDeleteProductFromStore")
    @ResponseBody
    public boolean deleteProductFromStore(
            @RequestParam(value = "storeId", defaultValue = "") int storeId,
            @RequestParam(value = "productId", defaultValue = "") int productId
            ){
            return managerHandler.deleteProductFromStore(storeId,productId);

    }

    @GetMapping("/viewPurchaseHistory")
    @ResponseBody
    public String viewPurchaseHistory(@RequestParam(value = "storeId", defaultValue = "") int storeId){
        return managerHandler.viewPurchaseHistory(storeId);
    }

    @GetMapping("/changeBuyingPolicy")
    @ResponseBody
    public boolean changeBuyingPolicy(
            @RequestParam(value = "storeId", defaultValue = "") int storeId,
            @RequestParam(value = "newPolicy", defaultValue = "") String newPolicy){
        return managerHandler.changeBuyingPolicy(storeId,newPolicy);
    }





}
