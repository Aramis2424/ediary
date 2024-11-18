package org.srd.ediary.application.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.srd.ediary.application.dto.DiaryCreateDTO;
import org.srd.ediary.application.dto.DiaryInfoDTO;
import org.srd.ediary.application.dto.DiaryUpdateDTO;
import org.srd.ediary.application.service.DiaryService;

import java.util.List;

@RestController
@RequestMapping("/diaries")
@RequiredArgsConstructor
public class DiaryController {
    private final DiaryService service;

    @GetMapping("/{diaryId}")
    public ResponseEntity<DiaryInfoDTO> getDiary(@PathVariable Long diaryId) {
        return new ResponseEntity<>(service.getDiary(diaryId), HttpStatus.OK);
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<DiaryInfoDTO>> getDiariesByOwner(@PathVariable Long ownerId) {
        return new ResponseEntity<>(service.getOwnerDiaries(ownerId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<DiaryInfoDTO> createDiary(@RequestBody DiaryCreateDTO diary) {
        return new ResponseEntity<>(service.create(diary), HttpStatus.OK);
    }

    @PutMapping("/{diaryId}")
    public ResponseEntity<DiaryInfoDTO> updateDiary(@PathVariable Long diaryId, @RequestBody DiaryUpdateDTO diary) {
        return new ResponseEntity<>(service.update(diaryId, diary), HttpStatus.OK);
    }

    @DeleteMapping("/{diaryId}")
    public HttpStatus deleteDiary(@PathVariable Long diaryId) {
        service.remove(diaryId);
        return HttpStatus.OK;
    }
}
