package com.example.communicationLayer.controllers;

import DTOs.ActionResultDTO;
import DTOs.StorePurchaseHistoryDTO;
import DTOs.SubscriberActionResultDTO;
import DTOs.UserPurchaseHistoryDTO;
import Service.AdminStateHandler;
import org.springframework.web.bind.annotation.*;

@CrossOrigin()
@RestController
public class AdminStateController {

    AdminStateHandler adminStateHandler ;


   @GetMapping("/AdminStateHandler")
    public void AdminStateHandler(@RequestParam(value = "sessionId", defaultValue = "") int sessionId){
        adminStateHandler = new AdminStateHandler(sessionId);
    }

    @GetMapping("/getAllSubscribers")
    @ResponseBody
    public SubscriberActionResultDTO getAllSubscribers(){
       return adminStateHandler.getAllSubscribers();
    }

    @GetMapping("/getSubscriberHistory")
    @ResponseBody
    public UserPurchaseHistoryDTO getSubscriberHistory(@RequestParam(value = "subId", defaultValue = "-1") int subId){
        return adminStateHandler.getSubscriberHistory(subId);
    }

    @GetMapping("/getStoreHistory")
    @ResponseBody
    public StorePurchaseHistoryDTO getStoreHistory(@RequestParam(value = "storeId", defaultValue = "-1") int storeId){
        return adminStateHandler.getStoreHistory(storeId);
    }
    @GetMapping("/addProductInfo")
    public ActionResultDTO addProductInfo(@RequestParam(value = "id", defaultValue = "-1") int id,
                                          @RequestParam(value = "name", defaultValue = "") String name,
                                          @RequestParam(value = "category", defaultValue = "") String category){
        return adminStateHandler.addProductInfo(id,name,category);

   }


}
