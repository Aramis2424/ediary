package org.srd.ediary.application.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.srd.ediary.application.dto.DiaryCreateDTO;
import org.srd.ediary.application.dto.DiaryInfoDTO;
import org.srd.ediary.application.dto.DiaryUpdateDTO;
import org.srd.ediary.application.mapper.DiaryMapper;
import org.srd.ediary.domain.model.Diary;
import org.srd.ediary.domain.model.Entry;
import org.srd.ediary.domain.model.Owner;
import org.srd.ediary.domain.repository.DiaryRepository;
import org.srd.ediary.domain.repository.EntryRepository;
import org.srd.ediary.domain.repository.OwnerRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DiaryService {
    private final DiaryRepository diaryRepo;
    private final EntryRepository entryRepo;
    private final OwnerRepository ownerRepo;

    public DiaryInfoDTO getDiary(Long id) {
        Diary diary = diaryRepo.getByID(id).orElseThrow(() -> new EntityNotFoundException("No diary with such id"));

        return DiaryMapper.INSTANCE.diaryToDiaryInfoDto(diary);
    }

    public List<DiaryInfoDTO> getOwnerDiaries(Long ownerID) {
        List<Diary> diaries = diaryRepo.getAllByOwnerID(ownerID);

        List<DiaryInfoDTO> diariesDTO = new ArrayList<>(diaries.size());
        for (var d : diaries)
            diariesDTO.add(DiaryMapper.INSTANCE.diaryToDiaryInfoDto(d));

        return diariesDTO;
    }

    public DiaryInfoDTO create(DiaryCreateDTO dto) {
        Owner owner = ownerRepo.getByID(dto.ownerID()).orElseThrow(() -> new EntityNotFoundException("No such owner"));
        Diary diary = new Diary(owner, dto.title(), dto.description());
        diary= diaryRepo.save(diary);
        return DiaryMapper.INSTANCE.diaryToDiaryInfoDto(diary);
    }

    public DiaryInfoDTO update(Long id, DiaryUpdateDTO dto) {
        Diary diary = diaryRepo.getByID(id)
                .map(d -> {
                    d.setTitle(dto.title());
                    d.setDescription(dto.description());
                    return diaryRepo.save(d);
                }).orElseThrow(() -> new EntityNotFoundException("No diary with such id"));
        return DiaryMapper.INSTANCE.diaryToDiaryInfoDto(diary);
    }

    public void remove(Long id) {
        List<Entry> entries = entryRepo.getAllByDiaryID(id);
        for (var entry : entries)
            entryRepo.delete(entry.getId());
        diaryRepo.delete(id);
    }
}
