package org.srd.ediary.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.srd.ediary.application.dto.*;
import org.srd.ediary.application.exception.MoodNotFoundException;
import org.srd.ediary.application.exception.OwnerNotFoundException;
import org.srd.ediary.application.service.MoodService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "mood", description = "Access to moods")
public class MoodController {
    private final MoodService service;

    @GetMapping("/moods/{moodId}")
    @Operation(summary = "Get mood by id")
    public ResponseEntity<MoodInfoDTO> getMood(@PathVariable Long moodId) {
        return new ResponseEntity<>(service.getMood(moodId), HttpStatus.OK);
    }

    @GetMapping("/owners/{ownerId}/moods")
    @Operation(summary = "Get all owner`s moods")
    public ResponseEntity<List<MoodInfoDTO>> getMoodByOwner(@PathVariable Long ownerId) {
        return new ResponseEntity<>(service.getMoodsByOwner(ownerId), HttpStatus.OK);
    }

    @GetMapping("/owners/{ownerId}/can-create-mood")
    @Operation(summary = "Get permission for creating mood")
    public ResponseEntity<MoodPermission> canCreateMood(@PathVariable Long ownerId,
                                                        @RequestParam("date")
                                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate requestedDate) {
        return new ResponseEntity<>(service.canCreateMood(ownerId, requestedDate), HttpStatus.OK);
    }

    @PostMapping("/moods")
    @Operation(summary = "Create mood for owner")
    public ResponseEntity<MoodInfoDTO> createMood(@RequestBody MoodCreateDTO mood) {
        return new ResponseEntity<>(service.create(mood), HttpStatus.CREATED);
    }

    @PutMapping("/moods/{moodId}")
    @Operation(summary = "Update mood by id")
    public ResponseEntity<MoodInfoDTO> updateMood(@PathVariable Long moodId, @RequestBody MoodUpdateDTO mood) {
        return new ResponseEntity<>(service.update(moodId, mood), HttpStatus.OK);
    }

    @DeleteMapping("/moods/{moodId}")
    @Operation(summary = "Delete mood by id")
    public HttpStatus deleteMood(@PathVariable Long moodId) {
        service.delete(moodId);
        return HttpStatus.OK;
    }

    @ExceptionHandler(OwnerNotFoundException.class)
    public ResponseEntity<String> handleOwnerNotFoundException(OwnerNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MoodNotFoundException.class)
    public ResponseEntity<String> handleMoodNotFoundException(MoodNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}
