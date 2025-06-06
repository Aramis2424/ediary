package org.srd.ediary.application.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.srd.ediary.application.dto.OwnerCreateDTO;
import org.srd.ediary.application.dto.OwnerInfoDTO;
import org.srd.ediary.application.dto.OwnerLoginDTO;
import org.srd.ediary.application.exception.InvalidCredentialsException;
import org.srd.ediary.application.exception.OwnerAlreadyExistException;
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
    public ResponseEntity<OwnerInfoDTO> loginOwner(@RequestBody OwnerLoginDTO req) {
        return new ResponseEntity<>(service.loginOwner(req.login(), req.password()), HttpStatus.OK);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<String> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(OwnerAlreadyExistException.class)
    public ResponseEntity<String> handleOwnerAlreadyExistException(OwnerAlreadyExistException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }
}
