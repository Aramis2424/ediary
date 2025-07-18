package org.srd.ediary.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.srd.ediary.application.dto.*;
import org.srd.ediary.application.exception.DiaryNotFoundException;
import org.srd.ediary.application.exception.EntryNotFoundException;
import org.srd.ediary.application.service.EntryCardService;
import org.srd.ediary.application.service.EntryService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "entry", description = "Access to entries")
public class EntryController {
    private final EntryService service;
    private final EntryCardService serviceCard;

    @GetMapping("/diaries/{diaryId}/entries")
    @Operation(summary = "Get all owner`s entries")
    public ResponseEntity<List<EntryInfoDTO>> getEntriesByDiary(@PathVariable Long diaryId) {
            return new ResponseEntity<>(service.getAllEntriesByDiary(diaryId), HttpStatus.OK);
    }

    @GetMapping("/entries/{entryId}")
    @Operation(summary = "Get entry by id")
    public ResponseEntity<EntryInfoDTO> getEntry(@PathVariable Long entryId) {
        return new ResponseEntity<>(service.getEntry(entryId), HttpStatus.OK);
    }

    @GetMapping("/diaries/{diaryId}/entry-cards")
    @Operation(summary = "Get entry cards representation")
    public ResponseEntity<List<EntryCardDTO>> getEntryCards(@PathVariable Long diaryId) {
        return new ResponseEntity<>(serviceCard.getEntryCards(diaryId), HttpStatus.OK);
    }

    @GetMapping("/diaries/{diaryId}/can-create-entry")
    @Operation(summary = "Get permission for creating entry")
    public ResponseEntity<EntryPermission> canCreateEntry(@PathVariable Long diaryId,
                                                          @RequestParam("date")
                                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate requestedDate) {
        return new ResponseEntity<>(service.canCreateEntry(diaryId, requestedDate), HttpStatus.OK);
    }

    @PostMapping("/entries")
    @Operation(summary = "Create entry for diary")
    public ResponseEntity<EntryInfoDTO> createEntry(@RequestBody EntryCreateDTO entry) {
        return new ResponseEntity<>(service.create(entry), HttpStatus.CREATED);
    }

    @PutMapping("/entries/{entryId}")
    @Operation(summary = "Update entry by id")
    public ResponseEntity<EntryInfoDTO> updateEntry(@PathVariable Long entryId, @RequestBody EntryUpdateDTO entry) {
        return new ResponseEntity<>(service.update(entryId, entry), HttpStatus.OK);
    }

    @DeleteMapping("/entries/{entryId}")
    @Operation(summary = "Delete entry by id")
    public HttpStatus deleteEntry(@PathVariable Long entryId) {
        service.delete(entryId);
        return HttpStatus.OK;
    }

    @ExceptionHandler(DiaryNotFoundException.class)
    public ResponseEntity<String> handleDiaryNotFoundException(DiaryNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntryNotFoundException.class)
    public ResponseEntity<String> handleEntryNotFoundException(EntryNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}
