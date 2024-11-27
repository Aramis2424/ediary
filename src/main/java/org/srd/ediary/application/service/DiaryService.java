package org.srd.ediary.application.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.srd.ediary.application.dto.DiaryCreateDTO;
import org.srd.ediary.application.dto.DiaryInfoDTO;
import org.srd.ediary.application.dto.DiaryUpdateDTO;
import org.srd.ediary.application.exception.DiaryNotFoundException;
import org.srd.ediary.application.exception.OwnerNotFoundException;
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

    @PreAuthorize("@diaryAccess.isAllowed(#id, authentication.principal.id)")
    public DiaryInfoDTO getDiary(Long id) {
        Diary diary = diaryRepo.getByID(id).orElseThrow(() -> new DiaryNotFoundException("No diary with such id"));

        return DiaryMapper.INSTANCE.diaryToDiaryInfoDto(diary);
    }

    @PreAuthorize("#ownerID.equals(authentication.principal.id)")
    public List<DiaryInfoDTO> getOwnerDiaries(Long ownerID) {
        List<Diary> diaries = diaryRepo.getAllByOwner(ownerID);

        List<DiaryInfoDTO> diariesDTO = new ArrayList<>(diaries.size()); // TODO вынести в мэппер
        for (var d : diaries)
            diariesDTO.add(DiaryMapper.INSTANCE.diaryToDiaryInfoDto(d));

        return diariesDTO;
    }

    @PreAuthorize("#dto.ownerID().equals(authentication.principal.id)")
    public DiaryInfoDTO create(DiaryCreateDTO dto) { // TODO реализовать как owner.addDiary(diary);
        Owner owner = ownerRepo.getByID(dto.ownerID()).orElseThrow(() -> new OwnerNotFoundException("No such owner"));
        Diary diary = new Diary(owner, dto.title(), dto.description());
        diary= diaryRepo.save(diary);
        return DiaryMapper.INSTANCE.diaryToDiaryInfoDto(diary);
    }


    @PreAuthorize("@diaryAccess.isAllowed(#id, authentication.principal.id)")
    public DiaryInfoDTO update(Long id, DiaryUpdateDTO dto) {
        Diary diary = diaryRepo.getByID(id)
                .map(d -> {
                    d.setTitle(dto.title());
                    d.setDescription(dto.description());
                    return diaryRepo.save(d);
                }).orElseThrow(() -> new DiaryNotFoundException("No diary with such id"));
        return DiaryMapper.INSTANCE.diaryToDiaryInfoDto(diary);
    }

    @PreAuthorize("@diaryAccess.isAllowed(#id, authentication.principal.id)")
    public void remove(Long id) {
        List<Entry> entries = entryRepo.getAllByDiary(id);
        for (var entry : entries)
            entryRepo.delete(entry.getId());
        diaryRepo.delete(id);
    }
}
