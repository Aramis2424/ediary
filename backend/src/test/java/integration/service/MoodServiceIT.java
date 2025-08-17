package integration.service;

import integration.context.WithMockOwnerDetails;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.srd.ediary.application.dto.MoodCreateDTO;
import org.srd.ediary.application.dto.MoodInfoDTO;
import org.srd.ediary.application.dto.MoodPermission;
import org.srd.ediary.application.dto.MoodUpdateDTO;
import org.srd.ediary.application.service.MoodService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static utils.MoodTestMother.*;

@WithMockOwnerDetails()
public class MoodServiceIT extends BaseIT {
    @Autowired
    private MoodService service;

    @Test
    void testGetMood_ExistingMood() {
        final Long moodId = 1L;
        MoodInfoDTO expected = getMoodInfoDTO1();

        MoodInfoDTO actual = service.getMood(moodId);

        assertEquals(expected.scoreMood(), actual.scoreMood());
    }

    @Test
    void testGetMood_NonExistingMood() {
        final Long moodId = -1L;

        assertThrows(AuthorizationDeniedException.class,
                () -> service.getMood(moodId));
    }

    @Test
    void testGetMoodsByOwner_ExistingOwner() {
        final Long ownerId = 1L;

        List<MoodInfoDTO> actual = service.getMoodsByOwner(ownerId);

        assertEquals(2, actual.size());
    }

    @Test
    void testGetMoodsByOwner_NonExistingOwner() {
        final Long ownerId = -1L;

        assertThrows(AuthorizationDeniedException.class,
                () -> service.getMoodsByOwner(ownerId));

    }

    @Test
    void testCreate_WithExistingOwner() {
        final Long ownerId = 1L;
        MoodCreateDTO input = getMoodCreateDTO(ownerId);
        MoodInfoDTO expected = getMoodInfoDTO1();

        MoodInfoDTO actual = service.create(input);

        assertEquals(expected.scoreMood(), actual.scoreMood());
    }

    @Test
    void testCreate_WithNonExistingOwner() {
        final Long ownerId = -1L;
        MoodCreateDTO input = getMoodCreateDTO(ownerId);

        assertThrows(AuthorizationDeniedException.class,
                () -> service.create(input));
    }

    @Test
    void testUpdate_ExistingMood() {
        final Long moodId = 1L;
        MoodUpdateDTO updateDTO = getMoodUpdateDTO();
        MoodInfoDTO expected = getMoodInfoDTO1();

        MoodInfoDTO actual = service.update(moodId, updateDTO);

        assertEquals(expected.scoreMood(), actual.scoreMood());
    }

    @Test
    void testUpdate_NonExistingMood() {
        final Long moodId = -1L;
        MoodUpdateDTO updateDTO = getMoodUpdateDTO();

        assertThrows(AuthorizationDeniedException.class,
                () -> service.update(moodId, updateDTO));
    }

    @Test
    void testDelete_ExistingMood() {
        Long moodId = 1L;

        service.delete(moodId);

        assertThrows(AuthorizationDeniedException.class,
                () -> service.getMood(moodId));
    }

    @Test
    void testCanCreateMood_True() {
        final Long ownerId = 1L;
        final LocalDate date = LocalDate.of(2020, 1, 1);
        MoodPermission expected = new MoodPermission(true);

        MoodPermission actual = service.canCreateMood(ownerId, date);

        assertEquals(expected, actual);
    }

    @Test
    void testCanCreateMood_False() {
        final Long ownerId = 1L;
        final LocalDate date = LocalDate.of(2021, 1, 1);
        MoodPermission expected = new MoodPermission(false);

        MoodPermission actual = service.canCreateMood(ownerId, date);

        assertEquals(expected, actual);
    }
}
