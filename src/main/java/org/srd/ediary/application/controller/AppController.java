package org.srd.ediary.application.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class AppController {
    @GetMapping
    public String getUser(@RequestParam Long id, @RequestParam String name, Principal principal) {
        return "User ID: " + id + ", Name: " + name + "\n" + principal.getName();
    }

    @GetMapping("/hello")
    public String sayHello() {
        return "Привет, это корневая страница проекта ediary";
    }
}
