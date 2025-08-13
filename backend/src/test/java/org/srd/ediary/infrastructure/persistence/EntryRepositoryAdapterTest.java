package org.srd.ediary.infrastructure.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;
import org.srd.ediary.domain.model.Diary;
import org.srd.ediary.domain.model.Entry;
import org.srd.ediary.domain.model.Owner;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static utils.DiaryTestMother.getDiary1;
import static utils.EntryTestMother.getEntry1;
import static utils.OwnerTestMother.getOwner;

@DataJpaTest
@ActiveProfiles("integration_test")
class EntryRepositoryAdapterTest {
    @Autowired
    private SpringOwnerRepository springOwnerRepository;
    @Autowired
    private SpringDiaryRepository springDiaryRepo;
    @Autowired
    private SpringEntryRepository springEntryRepo;
    private EntryRepositoryAdapter repoEntry;

    private Owner owner = getOwner();
    private Diary diary = getDiary1();

    @BeforeEach
    void setUp() {
        owner = new OwnerRepositoryAdapter(springOwnerRepository).save(owner);
        diary.setOwner(owner);
        diary = new DiaryRepositoryAdapter(springDiaryRepo).save(diary);

        repoEntry = new EntryRepositoryAdapter(springEntryRepo);
    }

    @Test
    void testSave_Correct() {
        Entry entry = getEntry1();
        entry.setDiary(diary);

        Entry savedEntry = repoEntry.save(entry);

        assertNotNull(savedEntry.getId());
        assertEquals("Day1", savedEntry.getTitle());
        assertEquals("Good day 1", savedEntry.getContent());
    }

    @Test
    void testSave_NullTitle() {
        Entry entry = getEntry1();
        entry.setDiary(diary);
        entry.setTitle(null);

        assertThrows(DataIntegrityViolationException.class,
                () -> repoEntry.save(entry));
    }

    @Test
    void testGetByID_Exist() {
        Entry entry = getEntry1();
        entry.setDiary(diary);
        Entry savedEntry = repoEntry.save(entry);

        Optional<Entry> gotEntry = repoEntry.getByID(savedEntry.getId());

        assertTrue(gotEntry.isPresent());
        assertEquals("Day1", gotEntry.get().getTitle());
        assertEquals("Good day 1", gotEntry.get().getContent());
    }

    @Test
    void testGetByID_NonExist() {
        Long nonExistingEntryId = -1L;

        Optional<Entry> gotEntry = repoEntry.getByID(nonExistingEntryId);

        assertTrue(gotEntry.isEmpty());
    }

    @Test
    void testDelete_Correct() {
        Entry entry = getEntry1();
        entry.setDiary(diary);
        Entry savedEntry = repoEntry.save(entry);

        repoEntry.delete(savedEntry.getId());

        Optional<Entry> gotEntry = repoEntry.getByID(savedEntry.getId());
        assertTrue(gotEntry.isEmpty());
    }

    @Test
    void testGetAllByDiary_Exist() {
        Entry entry1 = getEntry1();
        entry1.setDiary(diary);
        Entry entry2 = getEntry1();
        entry2.setDiary(diary);
        repoEntry.save(entry1);
        repoEntry.save(entry2);

        List<Entry> entries = repoEntry.getAllByDiary(diary.getId());

        assertEquals(2, entries.size());
    }

    @Test
    void testGetAllByDiary_NonExist() {
        Long diaryId = -1L;

        List<Entry> entries = repoEntry.getAllByDiary(diaryId);

        assertEquals(0, entries.size());
    }

    @Test
    void testGetByDiaryIdAndCreatedDate_Exist() {
        LocalDate date = LocalDate.now();
        Entry entry = getEntry1();
        entry.setDiary(diary);
        repoEntry.save(entry);

        Optional<Entry> gotEntry = repoEntry.getByDiaryIdAndCreatedDate(diary.getId(), date);

        assertTrue(gotEntry.isPresent());
        assertEquals("Day1", gotEntry.get().getTitle());
        assertEquals("Good day 1", gotEntry.get().getContent());
    }

    @Test
    void testGetByDiaryIdAndCreatedDate_NonExist() {
        LocalDate date = LocalDate.now();

        Optional<Entry> gotEntry = repoEntry.getByDiaryIdAndCreatedDate(diary.getId(), date);

        assertTrue(gotEntry.isEmpty());
    }
}
