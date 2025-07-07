package org.srd.ediary.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.srd.ediary.application.dto.EntryCardDTO;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class EntryCardService {
    @PreAuthorize("@entryAccess.isDiaryBelongsOwner(#diaryId, authentication.principal.id)")
    public List<EntryCardDTO> getEntryCards(Long diaryId) {
        return List.of(new EntryCardDTO(1L, 2L, "Title01", 8, 7, LocalDate.now()));
    }
}
