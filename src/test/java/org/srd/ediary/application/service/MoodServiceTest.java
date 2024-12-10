package org.srd.ediary.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.srd.ediary.application.dto.MoodCreateDTO;
import org.srd.ediary.application.dto.MoodInfoDTO;
import org.srd.ediary.application.dto.MoodUpdateDTO;
import org.srd.ediary.application.exception.MoodNotFoundException;
import org.srd.ediary.application.exception.OwnerNotFoundException;
import org.srd.ediary.domain.model.Mood;
import org.srd.ediary.domain.model.Owner;
import org.srd.ediary.domain.repository.MoodRepository;
import org.srd.ediary.domain.repository.OwnerRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MoodServiceTest {
    @Mock
    private OwnerRepository ownerRepo;
    @Mock
    private MoodRepository moodRepo;
    @InjectMocks
    private MoodService service;

    private final LocalDate birthdate = LocalDate.of(2000, 1, 1);
    private final Owner owner = new Owner("Ivan", birthdate, "example", "abc123");
    private final LocalDateTime bedtime = LocalDateTime
            .of(2020, 1,1, 22,30);
    private final LocalDateTime wakeUpTime = LocalDateTime
            .of(2020, 1,2, 8,30);

    @Test
    void testGetMood_ExistingMood() {
        final Long moodId = 1L;
        Mood gotMood = new Mood(owner, 7, 7, bedtime, wakeUpTime);
        MoodInfoDTO expected = new MoodInfoDTO(null, 7, 7,
                bedtime, wakeUpTime, LocalDate.now());
        when(moodRepo.getByID(moodId)).thenReturn(Optional.of(gotMood));

        MoodInfoDTO actual = service.getMood(moodId);

        assertEquals(expected, actual);
    }

    @Test
    void testGetMood_NonExistingMood() {
        final Long moodId = 1L;
        when(moodRepo.getByID(moodId)).thenReturn(Optional.empty());

        assertThrows(MoodNotFoundException.class, () -> service.getMood(moodId));
    }

    @Test
    void testGetMoodsByOwner_ExistingOwner() {
        final Long ownerId = 1L;
        Mood m1 = new Mood(owner, 5, 5, bedtime, wakeUpTime);
        Mood m2 = new Mood(owner, 7, 7, bedtime, wakeUpTime);
        List<Mood> gotMoods = List.of(m1, m2);
        MoodInfoDTO moodDto1 = new MoodInfoDTO(null, 5, 5,
                bedtime, wakeUpTime, LocalDate.now());
        MoodInfoDTO moodDto2 = new MoodInfoDTO(null, 7, 7,
                bedtime, wakeUpTime, LocalDate.now());
        List<MoodInfoDTO> expected = List.of(moodDto1, moodDto2);
        when(moodRepo.getAllByOwner(ownerId)).thenReturn(gotMoods);

        List<MoodInfoDTO> actual = service.getMoodsByOwner(ownerId);

        assertEquals(expected, actual);
    }

    @Test
    void testGetMoodsByOwner_NonExistingOwner() {
        final Long ownerId = 1L;
        List<MoodInfoDTO> expected = List.of();
        when(moodRepo.getAllByOwner(ownerId)).thenReturn(List.of());

        List<MoodInfoDTO> actual = service.getMoodsByOwner(ownerId);

        assertEquals(expected, actual);
    }

    @Test
    void testCreate_WithExistingOwner() {
        final Long ownerId = 1L;
        MoodCreateDTO input = new MoodCreateDTO(ownerId, 7, 7, bedtime, wakeUpTime);
        MoodInfoDTO expected = new MoodInfoDTO(null, 7, 7,
                bedtime, wakeUpTime, LocalDate.now());
        Mood createdMood = new Mood(owner, 7, 7, bedtime, wakeUpTime);
        when(ownerRepo.getByID(ownerId)).thenReturn(Optional.of(owner));
        when(moodRepo.save(any(Mood.class))).thenReturn(createdMood);

        MoodInfoDTO actual = service.create(input);

        assertEquals(expected, actual);
    }

    @Test
    void testCreate_WithNonExistingOwner() {
        final Long ownerId = 1L;
        MoodCreateDTO input = new MoodCreateDTO(ownerId, 7, 7, bedtime, wakeUpTime);
        when(ownerRepo.getByID(ownerId)).thenReturn(Optional.empty());

        assertThrows(OwnerNotFoundException.class, () -> service.create(input));

        verify(moodRepo, never()).save(any(Mood.class));
    }

    @Test
    void testUpdate_ExistingMood() {
        final Long moodId = 1L;
        Mood oldMood = new Mood(owner, 7, 7, bedtime, wakeUpTime);
        MoodUpdateDTO updateDTO = new MoodUpdateDTO(8, 8, bedtime, wakeUpTime);
        Mood updatedMood = new Mood(owner, 8, 8, bedtime, wakeUpTime);
        MoodInfoDTO expected = new MoodInfoDTO(null, 8, 8,
                bedtime, wakeUpTime, LocalDate.now());
        when(moodRepo.getByID(moodId)).thenReturn(Optional.of(oldMood));
        when(moodRepo.save(any(Mood.class))).thenReturn(updatedMood);

        MoodInfoDTO actual = service.update(moodId, updateDTO);

        assertEquals(expected, actual);
    }

    @Test
    void testUpdate_NonExistingMood() {
        final Long moodId = 1L;
        MoodUpdateDTO updateDTO = new MoodUpdateDTO(8, 8, bedtime, wakeUpTime);
        when(moodRepo.getByID(moodId)).thenReturn(Optional.empty());

        assertThrows(MoodNotFoundException.class, () -> service.update(moodId, updateDTO));
    }

    @Test
    void testDelete_ExistingMood() {
        Long moodId = 1L;
        doNothing().when(moodRepo).delete(moodId);

        service.delete(moodId);

        verify(moodRepo, times(1)).delete(moodId);
    }
}