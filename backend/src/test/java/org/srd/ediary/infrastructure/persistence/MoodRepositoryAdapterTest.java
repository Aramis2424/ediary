package org.srd.ediary.infrastructure.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.srd.ediary.domain.model.Mood;
import org.srd.ediary.domain.model.Owner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static utils.MoodTestMother.getMood1;
import static utils.OwnerTestMother.getOwner;

@DataJpaTest
@ActiveProfiles("integration_test")
class MoodRepositoryAdapterTest {
    @Autowired
    private SpringOwnerRepository springOwnerRepo;
    @Autowired
    private SpringMoodRepository springMoodRepo;
    private MoodRepositoryAdapter repoMood;

    private Owner owner = getOwner();

    @BeforeEach
    void setUp() {
        repoMood = new MoodRepositoryAdapter(springMoodRepo);
        owner = new OwnerRepositoryAdapter(springOwnerRepo).save(owner);
    }

    @Test
    void testSave_Correct() {
        Mood mood = getMood1();
        mood.setOwner(owner);

        Mood savedMood = repoMood.save(mood);

        assertNotNull(savedMood.getId());
        assertEquals(1, savedMood.getScoreMood());
        assertEquals(10, savedMood.getScoreProductivity());
    }

    @Test
    void testSave_NullOwner() {
        Mood mood = getMood1();
        mood.setOwner(null);

        assertThrows(DataIntegrityViolationException.class,
                () -> repoMood.save(mood));
    }

    @Test
    void testGetByID_Exist() {
        Mood mood = getMood1();
        mood.setOwner(owner);
        Mood savedMood = repoMood.save(mood);

        Optional<Mood> gotMood = repoMood.getByID(savedMood.getId());

        assertTrue(gotMood.isPresent());
        assertEquals(1, savedMood.getScoreMood());
        assertEquals(10, savedMood.getScoreProductivity());
    }

    @Test
    void testGetByID_NonExist() {
        Long nonExistingMoodId = -1L;

        Optional<Mood> gotMood = repoMood.getByID(nonExistingMoodId);

        assertTrue(gotMood.isEmpty());
    }

    @Test
    void testDelete_Correct() {
        Mood mood = getMood1();
        mood.setOwner(owner);
        Mood savedMood = repoMood.save(mood);

        repoMood.delete(savedMood.getId());

        Optional<Mood> gotMood = repoMood.getByID(savedMood.getId());
        assertTrue(gotMood.isEmpty());
    }

    @Test
    void testGetAllByOwner_ExistingOwner() {
        Mood mood1 = getMood1();
        mood1.setOwner(owner);
        Mood mood2 = getMood1();
        mood2.setOwner(owner);
        repoMood.save(mood1);
        repoMood.save(mood2);

        List<Mood> moods = repoMood.getAllByOwner(owner.getId());

        assertEquals(2, moods.size());
    }

    @Test
    void testGetAllByOwner_NonExistingOwner() {
        Long ownerId = -1L;

        List<Mood> moods = repoMood.getAllByOwner(ownerId);

        assertEquals(0, moods.size());
    }

    @Test
    void testGetByOwnerIdAndCreatedDate_Exist() {
        LocalDate date = LocalDate.now();
        Mood mood = getMood1();
        mood.setOwner(owner);
        repoMood.save(mood);

        Optional<Mood> gotMood = repoMood.getByOwnerIdAndCreatedDate(owner.getId(), date);

        assertTrue(gotMood.isPresent());
        assertEquals(1, gotMood.get().getScoreMood());
        assertEquals(10, gotMood.get().getScoreProductivity());
    }

    @Test
    void testGetByDiaryIdAndCreatedDate_NonExist() {
        LocalDate date = LocalDate.now();

        Optional<Mood> gotMood = repoMood.getByOwnerIdAndCreatedDate(owner.getId(), date);

        assertTrue(gotMood.isEmpty());
    }
}
