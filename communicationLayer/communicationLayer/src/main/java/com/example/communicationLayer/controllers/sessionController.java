package com.example.communicationLayer.controllers;


import java.util.concurrent.atomic.AtomicLong;

import com.example.communicationLayer.CommunicationLayerApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class sessionController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/setup")
    public void setup(@RequestParam(value = "supplyConfig", defaultValue = "") String supplyConfig) {

        new CommunicationLayerApplication(counter.incrementAndGet(), String.format(template, name));
    }
}