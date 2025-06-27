package com.magabyzr.ecommercemg.controllers;

import com.magabyzr.ecommercemg.entities.Message;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//with this, this API is build.
@RestController
public class MessageController {
    @RequestMapping("/hello")
    public Message sayHello() {
        return new Message("Hello Panzi!");
    }
}
