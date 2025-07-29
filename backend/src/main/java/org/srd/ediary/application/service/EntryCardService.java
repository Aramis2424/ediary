package org.srd.ediary.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.srd.ediary.application.dto.EntryCardDTO;
import org.srd.ediary.application.dto.EntryInfoDTO;
import org.srd.ediary.application.dto.MoodInfoDTO;
import org.srd.ediary.application.security.utils.AuthHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class EntryCardService {
    private final AuthHelper authHelper;
    private final EntryService entryService;
    private final MoodService moodService;

    @PreAuthorize("@entryAccess.isDiaryBelongsOwner(#diaryId, authentication.principal.id)")
    public List<EntryCardDTO> getEntryCards(Long diaryId) {
        Long ownerId = authHelper.getCurrentUserId();

        List<EntryInfoDTO> diaryEntries = entryService.getAllEntriesByDiary(diaryId);
        List<MoodInfoDTO> ownerMoods = moodService.getMoodsByOwner(ownerId);
        List<EntryCardDTO> cards = new ArrayList<>(diaryEntries.size());

        for (var entry: diaryEntries) {
            Optional<MoodInfoDTO> mood = ownerMoods.stream().filter(
                        it -> it.createdAt().isEqual(entry.createdDate()))
                    .findFirst();
            EntryCardDTO card = new EntryCardDTO(entry.id(), diaryId, entry.title(),
                    mood.map(MoodInfoDTO::scoreMood).orElse(-1),
                    mood.map(MoodInfoDTO::scoreProductivity).orElse(-1),
                    entry.createdDate());
            cards.add(card);
        }
        return cards;
    }
}
