package org.srd.ediary.application.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.srd.ediary.application.dto.OwnerCreateDTO;
import org.srd.ediary.application.dto.OwnerInfoDTO;
import org.srd.ediary.application.service.OwnerService;

@RestController
@RequestMapping("/owner")
@RequiredArgsConstructor
public class OwnerController {
    private final OwnerService service;

    @PostMapping("/register")
    public ResponseEntity<OwnerInfoDTO> createOwner(@RequestBody OwnerCreateDTO dto) {
        return new ResponseEntity<>(service.registerOwner(dto), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<OwnerInfoDTO> loginOwner(@RequestBody LoginRequest req) {
        return new ResponseEntity<>(service.loginOwner(req.login, req.password), HttpStatus.OK);
    }

    private record LoginRequest( // TODO переписать как DTO
            String login,
            String password
    ) {}
}
