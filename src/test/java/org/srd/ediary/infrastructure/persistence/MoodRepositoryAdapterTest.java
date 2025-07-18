package org.srd.ediary.infrastructure.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.srd.ediary.domain.model.Entry;
import org.srd.ediary.domain.model.Mood;
import org.srd.ediary.domain.model.Owner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static utils.OwnerTestFactory.getOwner;

@DataJpaTest
@ActiveProfiles("test")
class MoodRepositoryAdapterTest {
    @Autowired
    private SpringOwnerRepository springOwnerRepo;
    @Autowired
    private SpringMoodRepository springRepo;
    private MoodRepositoryAdapter repo;

    private Owner owner = getOwner();
    private final LocalDateTime bedtime = LocalDateTime
            .of(2020, 1,1, 22,30);
    private final LocalDateTime wakeUpTime = LocalDateTime
            .of(2020, 1,2, 8,30);

    @BeforeEach
    void setUp() {
        repo = new MoodRepositoryAdapter(springRepo);
        owner = new OwnerRepositoryAdapter(springOwnerRepo).save(owner);
    }

    @Test
    void testSave() {
        Mood mood = new Mood(owner, 7, 8, bedtime, wakeUpTime);

        Mood savedMood = repo.save(mood);

        assertNotNull(savedMood.getId());
        assertEquals(7, savedMood.getScoreMood());
        assertEquals(8, savedMood.getScoreProductivity());
    }

    @Test
    void testGetByID() {
        Mood mood = new Mood(owner, 7, 8, bedtime, wakeUpTime);
        Mood savedMood = repo.save(mood);

        Optional<Mood> gotMood = repo.getByID(savedMood.getId());

        assertTrue(gotMood.isPresent());
        assertEquals(7, savedMood.getScoreMood());
        assertEquals(8, savedMood.getScoreProductivity());
    }

    @Test
    void testDelete() {
        Mood mood = new Mood(owner, 7, 8, bedtime, wakeUpTime);
        Mood savedMood = repo.save(mood);

        repo.delete(savedMood.getId());

        Optional<Mood> gotMood = repo.getByID(savedMood.getId());
        assertTrue(gotMood.isEmpty());
    }

    @Test
    void testGetAllByOwner() {
        Mood mood1 = new Mood(owner, 7, 8, bedtime, wakeUpTime);
        Mood mood2 = new Mood(owner, 5, 6, bedtime, wakeUpTime);
        repo.save(mood1);
        repo.save(mood2);

        List<Mood> moods = repo.getAllByOwner(owner.getId());

        assertEquals(2, moods.size());
    }

    @Test
    void testGetByOwnerIdAndCreatedDate_Exist() {
        LocalDate date = LocalDate.now();
        Mood mood = new Mood(owner, 7, 8, bedtime, wakeUpTime);
        repo.save(mood);

        Optional<Mood> gotMood = repo.getByOwnerIdAndCreatedDate(owner.getId(), date);

        assertTrue(gotMood.isPresent());
        assertEquals(7, gotMood.get().getScoreMood());
        assertEquals(8, gotMood.get().getScoreProductivity());
    }

    @Test
    void testGetByDiaryIdAndCreatedDate_NonExist() {
        LocalDate date = LocalDate.now();

        Optional<Mood> gotMood = repo.getByOwnerIdAndCreatedDate(owner.getId(), date);

        assertTrue(gotMood.isEmpty());
    }
}
