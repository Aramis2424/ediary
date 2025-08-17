package integration.service;

import integration.context.WithMockOwnerDetails;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.srd.ediary.application.dto.EntryCardDTO;
import org.srd.ediary.application.service.EntryCardService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.EntryCardTestMother.*;

@WithMockOwnerDetails()
public class EntryCardServiceIT extends BaseIT {
    @Autowired
    private EntryCardService service;

    @Test
    void testGetEntryCards_UsualTest() {
        Long diaryId = 1L;
        List<EntryCardDTO> expectedCards = List.of(getEntryCardDto1It(diaryId), getEntryCardDto2It(diaryId));

        List<EntryCardDTO> actualCards = service.getEntryCards(diaryId);

        assertEquals(expectedCards, actualCards);
    }
}
