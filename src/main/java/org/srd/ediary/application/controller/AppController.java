package org.srd.ediary.application.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class AppController {
    public String welcome() {
        return "Статус OK";
    }
    @GetMapping("/hello")
    public String sayHello() {
        return "Привет, это корневая страница проекта ediary";
    }
}
