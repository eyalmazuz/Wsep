package com.example.communicationLayer.controllers;


import Service.*;
import com.example.communicationLayer.CommunicationLayerApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.Session;

@RestController
public class sessionController {

    SessionHandler sessionHandler = new SessionHandler();


    @GetMapping("/setup")
    @ResponseBody
    public boolean setup(@RequestParam(value = "supplyConfig", defaultValue = "") String supplyConfig,
                      @RequestParam(value = "paymentConfig", defaultValue = "") String paymentConfig) {
        return sessionHandler.setup(supplyConfig,paymentConfig);

    }

    @GetMapping("/startSession")
    @ResponseBody
    public int startSession(@RequestParam(value = "supplyConfig", defaultValue = "") String supplyConfig,
                         @RequestParam(value = "paymentConfig", defaultValue = "") String paymentConfig) {
        return sessionHandler.startSession();

    }




}