package org.srd.ediary.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.srd.ediary.application.dto.OwnerCreateDTO;
import org.srd.ediary.application.dto.OwnerInfoDTO;
import org.srd.ediary.application.exception.InvalidCredentialsException;
import org.srd.ediary.application.exception.OwnerAlreadyExistException;
import org.srd.ediary.application.security.OwnerDetails;
import org.srd.ediary.application.service.OwnerService;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "owner", description = "Operations about user")
public class OwnerController {
    private final OwnerService service;

    @PostMapping("/owners")
    @Operation(summary = "Create user.")
    public ResponseEntity<OwnerInfoDTO> createOwner(@RequestBody OwnerCreateDTO dto) {
        return new ResponseEntity<>(service.registerOwner(dto), HttpStatus.CREATED);
    }

    @GetMapping("/owners/me")
    public ResponseEntity<OwnerInfoDTO> fetchOwnerByToken(@AuthenticationPrincipal OwnerDetails principal) {
        return new ResponseEntity<>(service.fetchOwner(principal.getId()), HttpStatus.OK);
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
