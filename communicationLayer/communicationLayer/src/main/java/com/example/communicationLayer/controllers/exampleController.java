package com.example.communicationLayer.controllers;


import java.util.concurrent.atomic.AtomicLong;

import com.example.communicationLayer.CommunicationLayerApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class exampleController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/greeting")
    public CommunicationLayerApplication greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new CommunicationLayerApplication(counter.incrementAndGet(), String.format(template, name));
    }
}
