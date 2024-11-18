package org.srd.ediary.application.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.srd.ediary.application.dto.EntryCreateDTO;
import org.srd.ediary.application.dto.EntryInfoDTO;
import org.srd.ediary.application.dto.EntryUpdateDTO;
import org.srd.ediary.application.service.EntryService;

import java.util.List;

@RestController
@RequestMapping("/entries")
@RequiredArgsConstructor
public class EntryController {
    private final EntryService service;

    @GetMapping("/diary/{diaryId}")
    public ResponseEntity<List<EntryInfoDTO>> getEntriesByDiary(@PathVariable Long diaryId) {
            return new ResponseEntity<>(service.getAllEntriesByDiary(diaryId), HttpStatus.OK);
    }

    @GetMapping("/{entryId}")
    public ResponseEntity<EntryInfoDTO> getEntry(@PathVariable Long entryId) {
        return new ResponseEntity<>(service.getEntry(entryId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<EntryInfoDTO> createEntry(@RequestBody EntryCreateDTO entry) {
        return new ResponseEntity<>(service.create(entry), HttpStatus.CREATED);
    }

    @PutMapping("/{entryId}")
    public ResponseEntity<EntryInfoDTO> updateEntry(@PathVariable Long entryId, @RequestBody EntryUpdateDTO entry) {
        return new ResponseEntity<>(service.update(entryId, entry), HttpStatus.OK);
    }

    @DeleteMapping("/{entryId}")
    public HttpStatus deleteEntry(@PathVariable Long entryId) {
        service.delete(entryId);
        return HttpStatus.OK;
    }
}
