package com.example.communicationLayer.controllers;

import NotificationPublisher.MessageBrokerImpl;
import NotificationPublisher.Publisher;
import Service.SessionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Controller
public class NotificationHandler {

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    private Publisher publisher;

    public NotificationHandler(SimpMessageSendingOperations messagingTemplate){
        this.messagingTemplate = messagingTemplate;
        publisher = new Publisher(new MessageBrokerImpl(this.messagingTemplate));
        SessionHandler sessionHandler = new SessionHandler();
        sessionHandler.setPublisher(publisher);
        if(this.messagingTemplate == null){
            System.out.println("All not Good MAN");
        }
    }

    public void sendStoreUpdate(int sessionId, int storeId){

        if(messagingTemplate!=null){

            messagingTemplate.convertAndSend("/storeUpdate/"+sessionId,"Store "+storeId+" Has been updated");
        }
        else {
            System.out.println("message Template is null");
        }
    }



}
