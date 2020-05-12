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




}