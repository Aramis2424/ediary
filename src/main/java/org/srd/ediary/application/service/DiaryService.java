package org.srd.ediary.application.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.srd.ediary.application.dto.DiaryInfoDTO;
import org.srd.ediary.application.mapper.DiaryMapper;
import org.srd.ediary.domain.model.Diary;
import org.srd.ediary.domain.repository.DiaryRepository;
import org.srd.ediary.domain.repository.EntryRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class DiaryService {
    private final DiaryRepository diaryRepo;
    private final EntryRepository entryRepo;

    public DiaryInfoDTO getDiary(Long id) {
        Diary diary = diaryRepo.getByID(id).orElseThrow(() -> new EntityNotFoundException("No diary with such id"));

        return DiaryMapper.INSTANCE.diaryToDiaryInfoDto(diary);
    }
}
