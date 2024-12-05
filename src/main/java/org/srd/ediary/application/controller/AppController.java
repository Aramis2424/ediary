package org.srd.ediary.application.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.srd.ediary.application.security.OwnerDetails;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class AppController {
    @GetMapping
    public ResponseEntity<String> getUser(@RequestParam Long id, @RequestParam String name, @AuthenticationPrincipal OwnerDetails principal) {
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("text/plain;charset=UTF-8"))
                .body("User ID: " + id + ", Name: " + name + "\nServer id: " +
                        principal.getId() + ", Server name: " + principal.getUsername());
    }

    @GetMapping("/hello")
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("text/plain;charset=UTF-8"))
                .body("Привет, это корневая страница");
    }

}
