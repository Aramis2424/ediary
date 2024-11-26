package org.srd.ediary.application.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.srd.ediary.application.security.OwnerDetails;

import java.security.Principal;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class AppController {
    @GetMapping
    public String getUser(@RequestParam Long id, @RequestParam String name, @AuthenticationPrincipal OwnerDetails principal) {
        return "User ID: " + id + ", Name: " + name + "\n" + principal.getId() + " " + principal.getUsername();
    }

    @GetMapping("/hello")
    public String sayHello() {
        return "Привет, это корневая страница проекта ediary";
    }
}
