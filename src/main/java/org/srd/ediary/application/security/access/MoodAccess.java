package org.srd.ediary.application.security.access;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.srd.ediary.domain.model.Mood;
import org.srd.ediary.domain.model.Owner;
import org.srd.ediary.domain.repository.MoodRepository;
import org.srd.ediary.domain.repository.OwnerRepository;

import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
public class MoodAccess {
    private final MoodRepository moodRepo;
    private final OwnerRepository ownerRepo;

    public boolean isAllowed(Long moodId, Long ownerId) {
        try {
            Owner owner = ownerRepo.getByID(ownerId).orElseThrow();
            Mood mood = moodRepo.getByID(moodId).orElseThrow();
            return owner.getId().equals(mood.getOwner().getId());
        } catch (NoSuchElementException ex) {
            return false;
        }
    }
}
