package org.srd.ediary.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.srd.ediary.application.security.OwnerDetails;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "App", description = "Base test-app operations")
public class AppController {
    @GetMapping
    @Operation(summary = "Info about user from AuthenticationPrincipal")
    public ResponseEntity<String> getUser(@RequestParam Long id, @RequestParam String name, @AuthenticationPrincipal OwnerDetails principal) {
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("text/plain;charset=UTF-8"))
                .body("User ID: " + id + ", Name: " + name + "\nServer id: " +
                        principal.getId() + ", Server name: " + principal.getUsername());
    }

    @GetMapping("/hello")
    @Operation(summary = "Ping pong test")
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("text/plain;charset=UTF-8"))
                .body("Привет, это корневая страница");
    }

}
