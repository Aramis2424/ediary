package org.srd.ediary.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.srd.ediary.application.dto.DiaryCreateDTO;
import org.srd.ediary.application.dto.DiaryInfoDTO;
import org.srd.ediary.application.dto.DiaryUpdateDTO;
import org.srd.ediary.application.exception.DiaryNotFoundException;
import org.srd.ediary.application.exception.OwnerNotFoundException;
import org.srd.ediary.application.service.DiaryService;

import java.util.List;

@RestController
@RequestMapping("/diaries")
@RequiredArgsConstructor
@Tag(name = "diary", description = "Everything about user`s diary")
public class DiaryController {
    private final DiaryService service;

    @GetMapping("/{diaryId}")
    @Operation(summary = "Get diary by id")
    public ResponseEntity<DiaryInfoDTO> getDiary(@PathVariable Long diaryId) {
        return new ResponseEntity<>(service.getDiary(diaryId), HttpStatus.OK);
    }

    @GetMapping("/owner/{ownerId}")
    @Operation(summary = "Get all owner`s entries")
    public ResponseEntity<List<DiaryInfoDTO>> getDiariesByOwner(@PathVariable Long ownerId) {
        return new ResponseEntity<>(service.getOwnerDiaries(ownerId), HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Create diary")
    public ResponseEntity<DiaryInfoDTO> createDiary(@RequestBody DiaryCreateDTO diary) {
        return new ResponseEntity<>(service.create(diary), HttpStatus.OK);
    }

    @PutMapping("/{diaryId}")
    @Operation(summary = "Update diary by id")
    public ResponseEntity<DiaryInfoDTO> updateDiary(@PathVariable Long diaryId, @RequestBody DiaryUpdateDTO diary) {
        return new ResponseEntity<>(service.update(diaryId, diary), HttpStatus.OK);
    }

    @DeleteMapping("/{diaryId}")
    @Operation(summary = "Delete diary by id")
    public HttpStatus deleteDiary(@PathVariable Long diaryId) {
        service.remove(diaryId);
        return HttpStatus.OK;
    }

    @ExceptionHandler(DiaryNotFoundException.class)
    public ResponseEntity<String> handleDiaryNotFoundException(DiaryNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OwnerNotFoundException.class)
    public ResponseEntity<String> handleOwnerNotFoundException(OwnerNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}
