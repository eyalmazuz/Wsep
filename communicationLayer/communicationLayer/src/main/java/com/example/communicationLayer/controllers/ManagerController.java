package com.example.communicationLayer.controllers;


import DTOs.ActionResultDTO;
import DTOs.StorePurchaseHistoryDTO;
import Service.ManagerHandler;
import org.springframework.web.bind.annotation.*;

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
            @RequestParam(value = "storeId", defaultValue = "") int storeId,
            @RequestParam(value = "productId", defaultValue = "") int productId,
            @RequestParam(value = "amount", defaultValue = "") int amount){
        return managerHandler.addProductToStore(storeId,productId,amount);
    }


    @GetMapping("/ManagerEditProductToStoreManager")
    @ResponseBody
    public ActionResultDTO editProductToStore(
            @RequestParam(value = "storeId", defaultValue = "") int storeId,
            @RequestParam(value = "productId", defaultValue = "") int productId,
            @RequestParam(value = "info", defaultValue = "") String info){
            return managerHandler.editProductToStore(storeId, productId, info);
    }

    @GetMapping("/ManagerDeleteProductFromStore")
    @ResponseBody
    public ActionResultDTO deleteProductFromStore(
            @RequestParam(value = "storeId", defaultValue = "") int storeId,
            @RequestParam(value = "productId", defaultValue = "") int productId
            ){
            return managerHandler.deleteProductFromStore(storeId,productId);

    }

    @GetMapping("/ManagerViewPurchaseHistory")
    @ResponseBody
    public StorePurchaseHistoryDTO viewPurchaseHistory(@RequestParam(value = "storeId", defaultValue = "") int storeId){
        return managerHandler.viewPurchaseHistory(storeId);
    }

    @GetMapping("/ManagerChangeBuyingPolicy")
    @ResponseBody
    public ActionResultDTO changeBuyingPolicy(
            @RequestParam(value = "storeId", defaultValue = "") int storeId,
            @RequestParam(value = "newPolicy", defaultValue = "") String newPolicy){
        return managerHandler.changeBuyingPolicy(storeId,newPolicy);
    }





}