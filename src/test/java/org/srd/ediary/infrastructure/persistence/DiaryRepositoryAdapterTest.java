package org.srd.ediary.infrastructure.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.srd.ediary.domain.model.Diary;
import org.srd.ediary.domain.model.Owner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class DiaryRepositoryAdapterTest {
    @Autowired
    private SpringOwnerRepository springOwnerRepo;
    @Autowired
    private SpringDiaryRepository springRepo;
    private DiaryRepositoryAdapter repo;

    private final LocalDate birthDate = LocalDate.of(2000, 1, 1);
    private Owner owner = new Owner("Ivan", birthDate, "ivan01", "navi01");
    private final LocalDateTime bedtime = LocalDateTime
            .of(2020, 1,1, 22,30);
    private final LocalDateTime wakeUpTime = LocalDateTime
            .of(2020, 1,2, 8,30);

    @BeforeEach
    void setUp() {
        repo = new DiaryRepositoryAdapter(springRepo);
        owner = new OwnerRepositoryAdapter(springOwnerRepo).save(owner);
    }

    @Test
    void testSave() {
        Diary diary = new Diary(owner, "diary", "about");

        Diary savedDiary = repo.save(diary);

        assertNotNull(savedDiary.getId());
        assertEquals("diary", savedDiary.getTitle());
        assertEquals("about", savedDiary.getDescription());
    }

    @Test
    void testGetByID() {
        Diary diary = new Diary(owner, "diary", "about");
        Diary savedDiary = repo.save(diary);

        Optional<Diary> gotDiary = repo.getByID(savedDiary.getId());

        assertTrue(gotDiary.isPresent());
        assertEquals("diary", gotDiary.get().getTitle());
        assertEquals("about", gotDiary.get().getDescription());
    }

    @Test
    void testDelete() {
        Diary diary = new Diary(owner, "diary", "about");
        Diary savedDiary = repo.save(diary);

        repo.delete(savedDiary.getId());

        Optional<Diary> gotDiary = repo.getByID(savedDiary.getId());
        assertTrue(gotDiary.isEmpty());
    }

    @Test
    void testGetAllByOwner() {
        Diary diary1 = new Diary(owner, "diary1", "about1");
        Diary diary2 = new Diary(owner, "diary2", "about2");
        repo.save(diary1);
        repo.save(diary2);

        List<Diary> diaries = repo.getAllByOwner(owner.getId());

        assertEquals(2, diaries.size());
    }
}