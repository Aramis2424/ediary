package org.srd.ediary.infrastructure.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.srd.ediary.domain.model.Diary;
import org.srd.ediary.domain.model.Entry;
import org.srd.ediary.domain.model.Owner;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static utils.DiaryTestFactory.getDiary1;
import static utils.OwnerTestFactory.getOwner;

@DataJpaTest
@ActiveProfiles("test")
class EntryRepositoryAdapterTest {
    @Autowired
    private SpringOwnerRepository springOwnerRepository;
    @Autowired
    private SpringDiaryRepository springDiaryRepo;
    @Autowired
    private SpringEntryRepository springRepo;
    private EntryRepositoryAdapter repo;

    private Owner owner = getOwner();
    private Diary diary;

    @BeforeEach
    void setUp() {
        owner = new OwnerRepositoryAdapter(springOwnerRepository).save(owner);
        diary = new Diary(owner, "diary", "about");
        diary = new DiaryRepositoryAdapter(springDiaryRepo).save(diary);

        repo = new EntryRepositoryAdapter(springRepo);
    }

    @Test
    void testSave() {
        Entry entry = new Entry(diary, "entry", "day day");

        Entry savedEntry = repo.save(entry);

        assertNotNull(savedEntry.getId());
        assertEquals("entry", savedEntry.getTitle());
        assertEquals("day day", savedEntry.getContent());
    }

    @Test
    void testGetByID() {
        Entry entry = new Entry(diary, "entry", "day day");
        Entry savedEntry = repo.save(entry);

        Optional<Entry> gotEntry = repo.getByID(savedEntry.getId());

        assertTrue(gotEntry.isPresent());
        assertEquals("entry", gotEntry.get().getTitle());
        assertEquals("day day", gotEntry.get().getContent());
    }

    @Test
    void testDelete() {
        Entry entry = new Entry(diary, "entry", "day day");
        Entry savedEntry = repo.save(entry);

        repo.delete(savedEntry.getId());

        Optional<Entry> gotEntry = repo.getByID(savedEntry.getId());
        assertTrue(gotEntry.isEmpty());
    }

    @Test
    void testGetAllByDiary() {
        Entry entry1 = new Entry(diary, "entry1", "day day1");
        Entry entry2 = new Entry(diary, "entry2", "day day2");
        repo.save(entry1);
        repo.save(entry2);

        List<Entry> entries = repo.getAllByDiary(diary.getId());

        assertEquals(2, entries.size());
    }

    @Test
    void testGetByDiaryIdAndCreatedDate_Exist() {
        LocalDate date = LocalDate.now();
        Entry entry = new Entry(diary, "entry", "day day");
        repo.save(entry);

        Optional<Entry> gotEntry = repo.getByDiaryIdAndCreatedDate(diary.getId(), date);

        assertTrue(gotEntry.isPresent());
        assertEquals("entry", gotEntry.get().getTitle());
        assertEquals("day day", gotEntry.get().getContent());
    }

    @Test
    void testGetByDiaryIdAndCreatedDate_NonExist() {
        LocalDate date = LocalDate.now();

        Optional<Entry> gotEntry = repo.getByDiaryIdAndCreatedDate(diary.getId(), date);

        assertTrue(gotEntry.isEmpty());
    }
}
