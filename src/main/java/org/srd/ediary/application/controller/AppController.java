package org.srd.ediary.application.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.srd.ediary.application.dto.OwnerCreateDTO;
import org.srd.ediary.application.dto.OwnerInfoDTO;
import org.srd.ediary.application.service.OwnerService;
import org.srd.ediary.domain.model.Owner;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class AppController {
    private final OwnerService service;

    @GetMapping
    public String getUser(@RequestParam Long id, @RequestParam String name) {
        return "User ID: " + id + ", Name: " + name;
    }

    @GetMapping("/hello")
    public String sayHello() {
        return "Привет, это корневая страница проекта ediary";
    }

    @GetMapping("/savetest")
    public ResponseEntity<OwnerInfoDTO> saveOwner(@RequestBody OwnerCreateDTO dto) {
        return new ResponseEntity<>(service.registerOwner(dto), HttpStatus.CREATED);
    }
}
