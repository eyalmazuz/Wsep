package com.example.communicationLayer.controllers;


import DTOs.ActionResultDTO;
import DTOs.IntActionResultDto;
import DTOs.SubscriberActionResultDTO;
import DTOs.UserPurchaseHistoryDTO;
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
    public ActionResultDTO logout(){
        return subscriberStateHandler.logout();
    }

    @GetMapping("/openStore")
    @ResponseBody
    public IntActionResultDto openStore(){
        return subscriberStateHandler.openStore();
    }

    @GetMapping("/getHistory")
    @ResponseBody
    public UserPurchaseHistoryDTO getHistory(){
        return subscriberStateHandler.getHistory();
    }

    @GetMapping("/getAllManagers")
    @ResponseBody
    public SubscriberActionResultDTO getManagers(@RequestParam(value = "storeId",defaultValue = "-1") int storeId){
        return subscriberStateHandler.getAllManagers(storeId);
    }
}
