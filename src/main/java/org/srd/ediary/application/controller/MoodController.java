package org.srd.ediary.application.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.srd.ediary.application.dto.*;
import org.srd.ediary.application.exception.MoodNotFoundException;
import org.srd.ediary.application.exception.OwnerNotFoundException;
import org.srd.ediary.application.service.MoodService;

import java.util.List;

@RestController
@RequestMapping("/moods")
@RequiredArgsConstructor
public class MoodController {
    private final MoodService service;

    @GetMapping("/{moodId}")
    public ResponseEntity<MoodInfoDTO> getMood(@PathVariable Long moodId) {
        return new ResponseEntity<>(service.getMood(moodId), HttpStatus.OK);
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<MoodInfoDTO>> getMoodByOwner(@PathVariable Long ownerId) {
        return new ResponseEntity<>(service.getMoodsByOwner(ownerId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<MoodInfoDTO> createMood(@RequestBody MoodCreateDTO mood) {
        return new ResponseEntity<>(service.create(mood), HttpStatus.OK);
    }

    @PutMapping("/{moodId}")
    public ResponseEntity<MoodInfoDTO> updateMood(@PathVariable Long moodId, @RequestBody MoodUpdateDTO mood) {
        return new ResponseEntity<>(service.update(moodId, mood), HttpStatus.OK);
    }

    @DeleteMapping("/{moodId}")
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
