package org.srd.ediary.application.service;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.srd.ediary.application.dto.*;
import org.srd.ediary.application.exception.MoodNotFoundException;
import org.srd.ediary.application.exception.OwnerNotFoundException;
import org.srd.ediary.domain.model.Mood;
import org.srd.ediary.domain.repository.MoodRepository;
import org.srd.ediary.domain.repository.OwnerRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static utils.MoodTestMother.*;

@Epic("Unit Tests")
@Feature("Business logic")
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("unit_test")
class MoodServiceTest {
    @Mock
    private OwnerRepository ownerRepo;
    @Mock
    private MoodRepository moodRepo;
    @InjectMocks
    private MoodService service;

    @Test
    void testGetMood_ExistingMood() {
        final Long moodId = 1L;
        Mood gotMood = getMood1();
        MoodInfoDTO expected = getMoodInfoDTO1();
        when(moodRepo.getByID(moodId)).thenReturn(Optional.of(gotMood));

        MoodInfoDTO actual = service.getMood(moodId);

        assertEquals(expected, actual);
    }

    @Test
    void testGetMood_NonExistingMood() {
        final Long moodId = 1L;
        when(moodRepo.getByID(moodId)).thenReturn(Optional.empty());

        assertThrows(MoodNotFoundException.class,
                () -> service.getMood(moodId));
    }

    @Test
    void testGetMoodsByOwner_ExistingOwner() {
        final Long ownerId = 1L;
        List<Mood> gotMoods = List.of(
                getMood1(),
                getMood2()
        );
        List<MoodInfoDTO> expected = List.of(
                getMoodInfoDTO1(),
                getMoodInfoDTO2()
        );
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
        MoodCreateDTO input = getMoodCreateDTO(ownerId);
        MoodInfoDTO expected = getMoodInfoDTO1();
        Mood createdMood = getMood1();
        when(ownerRepo.getByID(ownerId)).thenReturn(Optional.of(owner));
        when(moodRepo.save(any(Mood.class))).thenReturn(createdMood);

        MoodInfoDTO actual = service.create(input);

        assertEquals(expected, actual);
    }

    @Test
    void testCreate_WithNonExistingOwner() {
        final Long ownerId = 1L;
        MoodCreateDTO input = getMoodCreateDTO(ownerId);
        when(ownerRepo.getByID(ownerId)).thenReturn(Optional.empty());

        assertThrows(OwnerNotFoundException.class,
                () -> service.create(input));

        verify(moodRepo, never()).save(any(Mood.class));
    }

    @Test
    void testUpdate_ExistingMood() {
        final Long moodId = 1L;
        Mood oldMood = getMood1();
        MoodUpdateDTO updateDTO = getMoodUpdateDTO();
        Mood updatedMood = getMood2();
        MoodInfoDTO expected = getMoodInfoDTO2();
        when(moodRepo.getByID(moodId)).thenReturn(Optional.of(oldMood));
        when(moodRepo.save(any(Mood.class))).thenReturn(updatedMood);

        MoodInfoDTO actual = service.update(moodId, updateDTO);

        assertEquals(expected, actual);
    }

    @Test
    void testUpdate_NonExistingMood() {
        final Long moodId = 1L;
        MoodUpdateDTO updateDTO = getMoodUpdateDTO();
        when(moodRepo.getByID(moodId)).thenReturn(Optional.empty());

        assertThrows(MoodNotFoundException.class,
                () -> service.update(moodId, updateDTO));
    }

    @Test
    void testDelete_ExistingMood() {
        Long moodId = 1L;
        doNothing().when(moodRepo).delete(moodId);

        service.delete(moodId);

        verify(moodRepo, times(1)).delete(moodId);
    }

    @Test
    void testCanCreateMood_True() {
        final Long ownerId = 1L;
        final LocalDate date = LocalDate.of(2020, 1, 1);
        Mood gotMood = getMood1();
        MoodPermission expected = new MoodPermission(false);
        when(moodRepo.getByOwnerIdAndCreatedDate(ownerId, date))
                .thenReturn(Optional.of(gotMood));

        MoodPermission actual = service.canCreateMood(ownerId, date);

        assertEquals(expected, actual);
    }

    @Test
    void testCanCreateMood_False() {
        final Long ownerId = 1L;
        final LocalDate date = LocalDate.of(2020, 1, 1);
        MoodPermission expected = new MoodPermission(true);
        when(moodRepo.getByOwnerIdAndCreatedDate(ownerId, date))
                .thenReturn(Optional.empty());

        MoodPermission actual = service.canCreateMood(ownerId, date);

        assertEquals(expected, actual);
    }
}
