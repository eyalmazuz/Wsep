package com.example.communicationLayer.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

@Component
public class NotificationHandler {

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;


    public void sendStoreUpdate(int sessionId, int storeId){
        messagingTemplate.convertAndSend("/storeUpdate/"+sessionId,"Store "+storeId+" Has been updated");
    }



}
