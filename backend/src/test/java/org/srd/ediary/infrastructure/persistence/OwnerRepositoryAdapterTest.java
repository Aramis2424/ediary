package org.srd.ediary.infrastructure.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.srd.ediary.domain.model.Owner;
import org.srd.ediary.infrastructure.exception.OwnerDeletionRestrictException;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class OwnerRepositoryAdapterTest {
    @Autowired
    private SpringOwnerRepository springRepo;
    private OwnerRepositoryAdapter repo;

    @BeforeEach
    void setUp() {
        repo = new OwnerRepositoryAdapter(springRepo);
    }

    private final LocalDate birthDate = LocalDate.of(2000, 1, 1);

    @Test
    void testSave() {
        Owner owner = new Owner("Ivan", birthDate, "ivan01", "navi01");

        Owner savedOwner = repo.save(owner);

        assertNotNull(savedOwner.getId());
        assertEquals("Ivan", savedOwner.getName());
    }

    @Test
    void testGetByID() {
        Owner owner = new Owner("Ivan", birthDate, "ivan01", "navi01");
        Owner savedOwner = repo.save(owner);

        Optional<Owner> gotOwner = repo.getByID(savedOwner.getId());

        assertTrue(gotOwner.isPresent());
        assertEquals("Ivan", gotOwner.get().getName());
    }

    @Test
    void testDelete() {
        Owner owner = new Owner("Ivan", birthDate, "ivan01", "navi01");
        Owner savedOwner = repo.save(owner);

        assertThrows(OwnerDeletionRestrictException.class, () -> repo.delete(savedOwner.getId()));

        assertTrue(repo.getByID(savedOwner.getId()).isPresent());
    }

    @Test
    void testGetByLoginAndPassword() {
        Owner owner = new Owner("Ivan", birthDate, "ivan01", "navi01");
        repo.save(owner);

        Optional<Owner> gotOwner = repo.getByLoginAndPassword("ivan01", "navi01");

        assertTrue(gotOwner.isPresent());
        assertEquals("Ivan", gotOwner.get().getName());
    }

    @Test
    void testGetByLogin() {
        Owner owner = new Owner("Ivan", birthDate, "ivan01", "navi01");
        repo.save(owner);

        Optional<Owner> gotOwner = repo.getByLogin("ivan01");

        assertTrue(gotOwner.isPresent());
        assertEquals("Ivan", gotOwner.get().getName());
    }
}
