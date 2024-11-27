package org.srd.ediary.application.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.srd.ediary.application.dto.MoodCreateDTO;
import org.srd.ediary.application.dto.MoodInfoDTO;
import org.srd.ediary.application.dto.MoodUpdateDTO;
import org.srd.ediary.application.exception.MoodNotFoundException;
import org.srd.ediary.application.exception.OwnerNotFoundException;
import org.srd.ediary.application.mapper.MoodMapper;
import org.srd.ediary.domain.model.Mood;
import org.srd.ediary.domain.model.Owner;
import org.srd.ediary.domain.repository.MoodRepository;
import org.srd.ediary.domain.repository.OwnerRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MoodService {
    private final MoodRepository moodRepo;
    private final OwnerRepository ownerRepo;

    @PreAuthorize("@moodAccess.isAllowed(#id, authentication.principal.id)")
    public MoodInfoDTO getMood(Long id) {
        Mood mood = moodRepo.getByID(id)
                .orElseThrow(() -> new MoodNotFoundException("No mood with such id"));
        return MoodMapper.INSTANCE.moodToMoodInfoDto(mood);
    }

    @PreAuthorize("#ownerID.equals(authentication.principal.id)")
    public List<MoodInfoDTO> getMoodsByOwner(Long ownerID) {
        List<Mood> moods = moodRepo.getAllByOwner(ownerID);
        List<MoodInfoDTO> dtoList = new ArrayList<>(moods.size());
        for (var mood : moods)
            dtoList.add(MoodMapper.INSTANCE.moodToMoodInfoDto(mood));
        return dtoList;
    }

    @PreAuthorize("#dto.ownerID().equals(authentication.principal.id)")
    public MoodInfoDTO create(MoodCreateDTO dto) {
        Owner owner = ownerRepo.getByID(dto.ownerID()).orElseThrow(() -> new OwnerNotFoundException("No such user"));
        Mood mood = new Mood(owner, dto.scoreMood(), dto.scoreProductivity(), dto.bedtime(), dto.wakeUpTime());
        return MoodMapper.INSTANCE.moodToMoodInfoDto(moodRepo.save(mood));
    }

    @PreAuthorize("@moodAccess.isAllowed(#id, authentication.principal.id)")
    public MoodInfoDTO update(Long id, MoodUpdateDTO dto) {
        Mood mood = moodRepo.getByID(id)
                .map(m -> {
                    m.setWakeUpTime(dto.wakeUpTime());
                    m.setBedtime(dto.bedtime());
                    m.setScoreMood(dto.scoreMood());
                    m.setScoreProductivity(dto.scoreProductivity());
                    return m;
                })
                .orElseThrow(() -> new MoodNotFoundException("No such mood id"));
        return MoodMapper.INSTANCE.moodToMoodInfoDto(moodRepo.save(mood));
    }

    @PreAuthorize("@moodAccess.isAllowed(#id, authentication.principal.id)")
    public void delete(Long id) {
        moodRepo.delete(id);
    }
}
