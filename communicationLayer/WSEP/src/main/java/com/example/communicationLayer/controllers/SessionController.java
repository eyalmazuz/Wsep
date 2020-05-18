package com.example.communicationLayer.controllers;


import DTOs.ActionResultDTO;
import DTOs.IntActionResultDto;
import Service.*;
import org.springframework.web.bind.annotation.*;

@CrossOrigin()
@RestController
public class SessionController {

    SessionHandler sessionHandler = new SessionHandler();


    @GetMapping("/setup")
    @ResponseBody
    public ActionResultDTO setup(@RequestParam(value = "supplyConfig", defaultValue = "") String supplyConfig,
                                 @RequestParam(value = "paymentConfig", defaultValue = "") String paymentConfig) {
        return sessionHandler.setup(supplyConfig,paymentConfig);

    }

    @GetMapping("/startSession")
    @ResponseBody
    public IntActionResultDto startSession() {
        return sessionHandler.startSession();

    }

    @GetMapping("/notificationAck")
    @ResponseBody
    public void notificationAck(@RequestParam(value = "subId",defaultValue = "-1") int subId,
                                @RequestParam(value = "notification", defaultValue = "-1")int notificationId){
        System.out.println(String.format("received ack on notfication: %d from user: %d", notificationId, subId));
        sessionHandler.notoficationAck(subId,notificationId);
    }

    @GetMapping("/ready")
    public void pullNotifications(@RequestParam(value = "subId", defaultValue = "-1") int subId){
        System.out.println("pulling notification");
        sessionHandler.pullNotifications(subId);
    }




}