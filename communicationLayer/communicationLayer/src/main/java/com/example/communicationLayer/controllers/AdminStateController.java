package com.example.communicationLayer.controllers;

import DTOs.StorePurchaseHistoryDTO;
import DTOs.UserPurchaseHistoryDTO;
import Service.AdminStateHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

public class AdminStateController {

    AdminStateHandler adminStateHandler ;


   @GetMapping("/AdminStateHandler")
    public void AdminStateHandler(@RequestParam(value = "sessionId", defaultValue = "") int sessionId){
        adminStateHandler = new AdminStateHandler(sessionId);
    }

    @GetMapping("/getSubscriberHistory")
    @ResponseBody
    public UserPurchaseHistoryDTO getSubscriberHistory(@RequestParam(value = "subId", defaultValue = "") int subId){
        return adminStateHandler.getSubscriberHistory(subId);
    }

    @GetMapping("/getStoreHistory")
    @ResponseBody
    public StorePurchaseHistoryDTO getStoreHistory(@RequestParam(value = "storeId", defaultValue = "") int storeId){
        return adminStateHandler.getStoreHistory(storeId);
    }
    @GetMapping("/addProductInfo")
    public void addProductInfo(@RequestParam(value = "id", defaultValue = "") int id,
                               @RequestParam(value = "name", defaultValue = "") String name,
                                @RequestParam(value = "category", defaultValue = "") String category){
        adminStateHandler.addProductInfo(id,name,category);

   }


}
