package com.example.startapp.service.common;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificationService {

    public void sendNotification(String sender,String title, String message){

        System.out.println("[" + LocalDateTime.now() + "] " + title + ": " + message);

    }
}
