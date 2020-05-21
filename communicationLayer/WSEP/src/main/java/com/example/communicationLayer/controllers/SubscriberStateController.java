package com.example.communicationLayer.controllers;


import DTOs.*;
import Service.SubscriberStateHandler;
import org.springframework.web.bind.annotation.*;

@CrossOrigin()
@RestController
public class SubscriberStateController {

    SubscriberStateHandler subscriberStateHandler ;

    @GetMapping("/SubscriberStateHandler")
    public void SubscriberStateHandler(@RequestParam(value = "sessionId", defaultValue = "-1") int sessionId){
        subscriberStateHandler = new SubscriberStateHandler(sessionId);
    }

    @GetMapping("/logout")
    @ResponseBody
    public ActionResultDTO logout(@RequestParam(value = "sessionId", defaultValue = "") int sessionId){
        return new SubscriberStateHandler(sessionId).logout();
    }

    @GetMapping("/openStore")
    @ResponseBody
    public IntActionResultDto openStore(@RequestParam(value = "sessionId", defaultValue = "") int sessionId){
        return new SubscriberStateHandler(sessionId).openStore();
    }

    @GetMapping("/getHistory")
    @ResponseBody
    public UserPurchaseHistoryDTO getHistory(@RequestParam(value = "sessionId", defaultValue = "") int sessionId){
        return new SubscriberStateHandler(sessionId).getHistory();
    }

    @GetMapping("/getAllManagers")
    @ResponseBody
    public SubscriberActionResultDTO getManagers(@RequestParam(value = "sessionId", defaultValue = "") int sessionId,
                                                 @RequestParam(value = "storeId",defaultValue = "-1") int storeId){
        return new SubscriberStateHandler(sessionId).getAllManagers(storeId);
    }


}
