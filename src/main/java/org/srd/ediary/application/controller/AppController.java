package org.srd.ediary.application.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class AppController {
    @GetMapping
    public String getUser(@RequestParam Long id, @RequestParam String name) {
        return "User ID: " + id + ", Name: " + name;
    }

    @GetMapping("/hello")
    public String sayHello() {
        return "Привет, это корневая страница проекта ediary";
    }
}
