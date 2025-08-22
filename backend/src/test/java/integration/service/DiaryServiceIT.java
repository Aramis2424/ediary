package integration.service;

import integration.context.WithMockOwnerDetails;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.srd.ediary.application.dto.DiaryCreateDTO;
import org.srd.ediary.application.dto.DiaryInfoDTO;
import org.srd.ediary.application.dto.DiaryUpdateDTO;
import org.srd.ediary.application.service.DiaryService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static utils.DiaryTestMother.*;

@Epic("Integration Tests")
@Feature("Service")
@WithMockOwnerDetails()
public class DiaryServiceIT extends BaseIT {
    @Autowired
    private DiaryService service;

    @Test
    void testGetDiary_ExistingDiary() {
        DiaryInfoDTO expected = getDiaryInfoDTO1();

        DiaryInfoDTO actual = service.getDiary(1L);

        assertEquals(expected.title(), actual.title());
    }

    @Test
    void testGetDiary_NonExistingDiary() {
        final Long diaryId = -1L;

        assertThrows(AuthorizationDeniedException.class,
                () -> service.getDiary(diaryId));
    }

    @Test
    void testGetOwnerDiaries_ExistingOwner() {
        final Long ownerId = 1L;

        List<DiaryInfoDTO> actual = service.getOwnerDiaries(ownerId);

        assertEquals(2, actual.size());
    }

    @Test
    void testGetOwnerDiaries_NonExistingOwner() {
        final Long ownerId = 2L;

        assertThrows(AuthorizationDeniedException.class,
                () -> service.getOwnerDiaries(ownerId));
    }

    @Test
    void testCreate_WithExistingOwner() {
        final Long ownerId = 1L;
        DiaryCreateDTO input = getDiaryCreateDTO(ownerId);
        DiaryInfoDTO expected = getDiaryInfoDTO1();

        DiaryInfoDTO actual = service.create(input);

        assertEquals(expected.title(), actual.title());
    }

    @Test
    void testCreate_WithNonExistingOwner() {
        final Long ownerId = -1L;
        DiaryCreateDTO input = getDiaryCreateDTO(ownerId);

        assertThrows(AuthorizationDeniedException.class,
                () -> service.create(input));
    }

    @Test
    void testUpdate_ExistingDiary() {
        final Long diaryId = 1L;
        DiaryUpdateDTO updateDto = getDiaryUpdateDTO();
        DiaryInfoDTO expected = getDiaryInfoDTO2();

        DiaryInfoDTO actual = service.update(diaryId, updateDto);

        assertEquals(expected.title(), actual.title());
    }

    @Test
    void testUpdate_NonExistingDiary() {
        final Long diaryId = -1L;
        DiaryUpdateDTO updateDto = getDiaryUpdateDTO();

        assertThrows(AuthorizationDeniedException.class,
                () -> service.update(diaryId, updateDto));
    }

    @Test
    void testRemove_ExistingDiaryWithEntries() {
        final Long diaryId = 1L;

        service.remove(diaryId);

        assertThrows(AuthorizationDeniedException.class,
                () -> service.getDiary(diaryId));
    }

    @Test
    void testRemove_ExistingDiaryWithoutEntries() {
        final Long diaryId = 1L;

        service.remove(diaryId);

        assertThrows(AuthorizationDeniedException.class,
                () -> service.getDiary(diaryId));
    }

    @Test
    void testRemove_NonExistingDiary() {
        final Long diaryId = 1L;

        service.remove(diaryId);

        assertThrows(AuthorizationDeniedException.class,
                () -> service.getDiary(diaryId));
    }
}
