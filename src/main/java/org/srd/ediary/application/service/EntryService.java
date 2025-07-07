package org.srd.ediary.application.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.srd.ediary.application.dto.EntryCreateDTO;
import org.srd.ediary.application.dto.EntryInfoDTO;
import org.srd.ediary.application.dto.EntryPermission;
import org.srd.ediary.application.dto.EntryUpdateDTO;
import org.srd.ediary.application.exception.DiaryNotFoundException;
import org.srd.ediary.application.exception.EntryNotFoundException;
import org.srd.ediary.application.mapper.EntryMapper;
import org.srd.ediary.domain.model.Diary;
import org.srd.ediary.domain.model.Entry;
import org.srd.ediary.domain.repository.DiaryRepository;
import org.srd.ediary.domain.repository.EntryRepository;

import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class EntryService {
    private final EntryRepository entryRepo;
    private final DiaryRepository diaryRepo;

    @PreAuthorize("@entryAccess.isAllowed(#id, authentication.principal.id)")
    public EntryInfoDTO getEntry(Long id) {
        Entry entry = entryRepo.getByID(id)
                .orElseThrow(() -> new EntryNotFoundException("No entry with such id"));
        return EntryMapper.INSTANCE.EntryToEntryInfoDto(entry);
    }

    @PreAuthorize("@entryAccess.isDiaryBelongsOwner(#diaryID, authentication.principal.id)")
    public List<EntryInfoDTO> getAllEntriesByDiary(Long diaryID) {
        List<Entry> entries = entryRepo.getAllByDiary(diaryID);
        List<EntryInfoDTO> dtoList = new ArrayList<>(entries.size());
        for (var entry : entries)
            dtoList.add(EntryMapper.INSTANCE.EntryToEntryInfoDto(entry));
        return dtoList;
    }

    @PreAuthorize("@entryAccess.isDiaryBelongsOwner(#dto.diaryID, authentication.principal.id)")
    public EntryInfoDTO create(EntryCreateDTO dto) {
        Diary diary = diaryRepo.getByID(dto.diaryID()).orElseThrow(() -> new DiaryNotFoundException("No such diary"));
        Entry entry = new Entry(diary, dto.title(), dto.content());
        return EntryMapper.INSTANCE.EntryToEntryInfoDto(entryRepo.save(entry));
    }

    @PreAuthorize("@entryAccess.isAllowed(#id, authentication.principal.id)")
    public EntryInfoDTO update(Long id, EntryUpdateDTO dto) {
        Entry entry = entryRepo.getByID(id)
                .map(e -> {
                    e.setTitle(dto.title());
                    e.setContent(dto.content());
                    return e;
                })
                .orElseThrow(() -> new EntryNotFoundException("No entry with such id"));
        return EntryMapper.INSTANCE.EntryToEntryInfoDto(entryRepo.save(entry));
    }

    @PreAuthorize("@entryAccess.isAllowed(#id, authentication.principal.id)")
    public void delete(Long id) {
        entryRepo.delete(id);
    }

    @PreAuthorize("@entryAccess.isDiaryBelongsOwner(#diaryId, authentication.principal.id)")
    public EntryPermission canCreateEntry(Long diaryId) {
        return new EntryPermission(true);
    }
}
