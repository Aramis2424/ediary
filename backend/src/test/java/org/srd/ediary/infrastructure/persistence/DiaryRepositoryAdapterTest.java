package org.srd.ediary.infrastructure.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;
import org.srd.ediary.domain.model.Diary;
import org.srd.ediary.domain.model.Owner;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static utils.DiaryTestMother.getDiary1;
import static utils.OwnerTestMother.getOwner;

@DataJpaTest
@ActiveProfiles("integration_test")
class DiaryRepositoryAdapterTest {
    @Autowired
    private SpringOwnerRepository springOwnerRepo;
    @Autowired
    private SpringDiaryRepository springDiaryRepo;
    private DiaryRepositoryAdapter repoDiary;

    private Owner owner = getOwner();

    @BeforeEach
    void setUp() {
        repoDiary = new DiaryRepositoryAdapter(springDiaryRepo);
        owner = new OwnerRepositoryAdapter(springOwnerRepo).save(owner);
    }

    @Test
    void testSave_Correct() {
        Diary diary = getDiary1();
        diary.setOwner(owner);

        Diary savedDiary = repoDiary.save(diary);

        assertNotNull(savedDiary.getId());
        assertEquals("d1", savedDiary.getTitle());
        assertEquals("about1", savedDiary.getDescription());
    }

    @Test
    void testSave_UnsavedTransientOwner() {
        Diary diary = getDiary1();

        assertThrows(InvalidDataAccessApiUsageException.class,
                () -> repoDiary.save(diary));
    }

    @Test
    void testGetByID_Exist() {
        Diary diary = getDiary1();
        diary.setOwner(owner);
        Diary savedDiary = repoDiary.save(diary);

        Optional<Diary> gotDiary = repoDiary.getByID(savedDiary.getId());

        assertTrue(gotDiary.isPresent());
        assertEquals("d1", gotDiary.get().getTitle());
        assertEquals("about1", gotDiary.get().getDescription());
    }

    @Test
    void testGetByID_NonExist() {
        Long nonExistingDiaryId = -1L;

        Optional<Diary> gotDiary = repoDiary.getByID(nonExistingDiaryId);

        assertTrue(gotDiary.isEmpty());
    }

    @Test
    void testDelete_Correct() {
        Diary diary = getDiary1();
        diary.setOwner(owner);
        Diary savedDiary = repoDiary.save(diary);

        repoDiary.delete(savedDiary.getId());

        Optional<Diary> gotDiary = repoDiary.getByID(savedDiary.getId());
        assertTrue(gotDiary.isEmpty());
    }

    @Test
    void testGetAllByOwner_Exist() {
        Diary diary1 = getDiary1();
        diary1.setOwner(owner);
        Diary diary2 = getDiary1();
        diary2.setOwner(owner);
        repoDiary.save(diary1);
        repoDiary.save(diary2);

        List<Diary> diaries = repoDiary.getAllByOwner(owner.getId());

        assertEquals(2, diaries.size());
    }

    @Test
    void testGetAllByOwner_nonExist() {
        Long ownerId = -1L;

        List<Diary> diaries = repoDiary.getAllByOwner(ownerId);

        assertEquals(0, diaries.size());
    }
}