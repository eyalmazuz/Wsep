package com.example.communicationLayer.controllers;


import DTOs.ActionResultDTO;
import DTOs.IntActionResultDto;
import Service.SubscriberStateHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
    public String getHistory(){
        return subscriberStateHandler.getHistory();
    }
}
