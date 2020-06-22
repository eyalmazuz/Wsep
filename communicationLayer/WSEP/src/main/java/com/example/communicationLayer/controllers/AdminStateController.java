package com.example.communicationLayer.controllers;

import DTOs.*;
import Domain.TradingSystem.Subscriber;
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
                                          @RequestParam(value = "id", defaultValue = "-1") int id,
                                          @RequestParam(value = "name", defaultValue = "") String name,
                                          @RequestParam(value = "category", defaultValue = "") String category,
                                          @RequestParam(value = "basePrice", defaultValue = "") double basePrice){
         return new AdminStateHandler(sessionId).addProductInfo(id,name,category, basePrice);

   }

    @GetMapping("/getAllSubscribers")
    public SubscriberActionResultDTO getAllSubscribers(@RequestParam(value = "sessionId", defaultValue = "-1") int sessionId){
       return new AdminStateHandler(sessionId).getAllSubscribers();
    }

    @GetMapping("/getStats")
       public StatisticsResultsDTO getStatistics(@RequestParam(value = "sessionId",defaultValue = "-1") int sessionId,
                                                 @RequestParam(value="from",defaultValue = "") String from,
                                                 @RequestParam(value = "to",defaultValue = "")String to){
           return new AdminStateHandler(sessionId).getStatistics(from,to);
        }
    }



