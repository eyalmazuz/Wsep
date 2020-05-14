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

    @GetMapping("/getSubscriberHistory")
    @ResponseBody
    public UserPurchaseHistoryDTO getSubscriberHistory(@RequestParam(value = "sessionId", defaultValue = "") int sessionId,
                                                       @RequestParam(value = "subId", defaultValue = "") int subId){
        return new AdminStateHandler(sessionId).getSubscriberHistory(subId);
    }

    @GetMapping("/getStoreHistory")
    @ResponseBody
    public StorePurchaseHistoryDTO getStoreHistory(@RequestParam(value = "sessionId", defaultValue = "") int sessionId,
                                                    @RequestParam(value = "storeId", defaultValue = "") int storeId){
        return new AdminStateHandler(sessionId).getStoreHistory(storeId);
    }
    @GetMapping("/addProductInfo")
    public ActionResultDTO addProductInfo(@RequestParam(value = "sessionId", defaultValue = "") int sessionId,
                                          @RequestParam(value = "id", defaultValue = "") int id,
                                          @RequestParam(value = "name", defaultValue = "") String name,
                                          @RequestParam(value = "category", defaultValue = "") String category){
         return new AdminStateHandler(sessionId).addProductInfo(id,name,category);

   }


}
