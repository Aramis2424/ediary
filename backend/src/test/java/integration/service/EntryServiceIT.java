package integration.service;

import integration.context.WithMockOwnerDetails;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.srd.ediary.application.dto.EntryCreateDTO;
import org.srd.ediary.application.dto.EntryInfoDTO;
import org.srd.ediary.application.dto.EntryPermission;
import org.srd.ediary.application.dto.EntryUpdateDTO;
import org.srd.ediary.application.service.EntryService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static utils.EntryTestMother.*;

@Epic("Integration Tests")
@Feature("Service")
@WithMockOwnerDetails()
public class EntryServiceIT extends BaseIT {
    @Autowired
    private EntryService service;

    @Test
    void testGetEntry_ExistingEntry() {
        final Long entryId = 1L;
        EntryInfoDTO expected = getEntryInfoDTO1();

        EntryInfoDTO actual = service.getEntry(entryId);

        assertEquals(expected.title(), actual.title());
    }

    @Test
    void testGetEntry_NonExistingEntry() {
        final Long entryId = -1L;

        assertThrows(AuthorizationDeniedException.class,
                () -> service.getEntry(entryId));
    }

    @Test
    void testGetAllEntriesByDiary_ExistingDiary() {
        final Long diaryId = 1L;

        List<EntryInfoDTO> actual = service.getAllEntriesByDiary(diaryId);

        assertEquals(2, actual.size());
    }

    @Test
    void testGetAllEntriesByDiary_NonExistingDiary() {
        final Long diaryId = -1L;

        assertThrows(AuthorizationDeniedException.class,
                () -> service.getAllEntriesByDiary(diaryId));
    }

    @Test
    void testCreate_WithExistingDiary() {
        final Long diaryId = 1L;
        EntryCreateDTO input = getEntryCreateDTO(diaryId);
        EntryInfoDTO expected = getEntryInfoDTO2();

        EntryInfoDTO actual = service.create(input);

        assertEquals(expected.title(), actual.title());
    }

    @Test
    void testCreate_WithNonExistingDiary() {
        final Long diaryId = -1L;
        EntryCreateDTO input = getEntryCreateDTO(diaryId);

        assertThrows(AuthorizationDeniedException.class,
                () ->service.create(input));
    }

    @Test
    void testUpdate_ExistingMood() {
        final Long entryId = 1L;
        EntryUpdateDTO updateDTO = getEntryUpdateDTO();
        EntryInfoDTO expected = getEntryInfoDTO2();

        EntryInfoDTO actual = service.update(entryId, updateDTO);

        assertEquals(expected.title(), actual.title());
    }

    @Test
    void testUpdate_NonExistingMood() {
        final Long entryId = -1L;
        EntryUpdateDTO updateDTO = getEntryUpdateDTO();

        assertThrows(AuthorizationDeniedException.class,
                () -> service.update(entryId, updateDTO));

    }

    @Test
    void testDelete_ExistingEntry() {
        final Long entryId = 1L;

        service.delete(entryId);

        assertThrows(AuthorizationDeniedException.class,
                () -> service.getEntry(entryId));
    }

    @Test
    void testCanCreateEntry_True() {
        final Long diaryId = 1L;
        final LocalDate date = LocalDate.of(2020, 1, 1);
        EntryPermission expected = new EntryPermission(true);

        EntryPermission actual = service.canCreateEntry(diaryId, date);

        assertEquals(expected, actual);
    }

    @Test
    void testCanCreateEntry_False() {
        final Long diaryId = 1L;
        final LocalDate date = LocalDate.of(2021, 1, 1);
        EntryPermission expected = new EntryPermission(false);

        EntryPermission actual = service.canCreateEntry(diaryId, date);

        assertEquals(expected, actual);
    }
}
