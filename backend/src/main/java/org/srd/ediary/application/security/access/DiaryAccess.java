package org.srd.ediary.application.security.access;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.srd.ediary.domain.model.Diary;
import org.srd.ediary.domain.model.Owner;
import org.srd.ediary.domain.repository.DiaryRepository;
import org.srd.ediary.domain.repository.OwnerRepository;

import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
public class DiaryAccess {
    private final DiaryRepository diaryRepo;
    private final OwnerRepository ownerRepo;

    public boolean isAllowed(Long diaryId, Long ownerId) {
        try {
            Owner owner = ownerRepo.getByID(ownerId).orElseThrow();
            Diary diary = diaryRepo.getByID(diaryId).orElseThrow();
            return owner.getId().equals(diary.getOwner().getId());
        } catch (NoSuchElementException ex) {
            return false;
        }
    }
}
