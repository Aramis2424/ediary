package org.srd.ediary.infrastructure.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.srd.ediary.domain.model.Mood;
import org.srd.ediary.domain.model.Owner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class MoodRepositoryAdapterTest {
    @Autowired
    private SpringOwnerRepository springOwnerRepo;
    @Autowired
    private SpringMoodRepository springRepo;
    private MoodRepositoryAdapter repo;

    private final LocalDate birthDate = LocalDate.of(2000, 1, 1);
    private Owner owner = new Owner("Ivan", birthDate, "ivan01", "navi01");
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

        assertNotNull(savedMood.getId());
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
        Mood savedMood1 = repo.save(mood1);
        Mood savedMood2 = repo.save(mood2);


        List<Mood> moods = repo.getAllByOwner(owner.getId());

        assertNotNull(savedMood1.getId());
        assertNotNull(savedMood2.getId());
        assertEquals(7, savedMood1.getScoreMood());
        assertEquals(5, savedMood2.getScoreMood());
    }
}