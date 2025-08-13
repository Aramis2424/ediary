package org.srd.ediary.infrastructure.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.srd.ediary.domain.model.Owner;
import org.srd.ediary.infrastructure.exception.OwnerDeletionRestrictException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static utils.OwnerTestMother.getOwner;

@DataJpaTest
@ActiveProfiles("integration_test")
class OwnerRepositoryAdapterTest {
    @Autowired
    private SpringOwnerRepository springOwnerRepo;
    private OwnerRepositoryAdapter repoOwner;

    @BeforeEach
    void setUp() {
        repoOwner = new OwnerRepositoryAdapter(springOwnerRepo);
    }

    @Test
    void testSave() {
        Owner owner = getOwner();

        Owner savedOwner = repoOwner.save(owner);

        assertEquals("Ivan", savedOwner.getName());
    }

    @Test
    void testGetByID() {
        Owner owner = getOwner();
        Owner savedOwner = repoOwner.save(owner);

        Optional<Owner> gotOwner = repoOwner.getByID(savedOwner.getId());

        assertTrue(gotOwner.isPresent());
        assertEquals("Ivan", gotOwner.get().getName());
    }

    @Test
    void testDelete() {
        Owner owner = getOwner();
        Owner savedOwner = repoOwner.save(owner);

        assertThrows(OwnerDeletionRestrictException.class,
                () -> repoOwner.delete(savedOwner.getId()));

        assertTrue(repoOwner.getByID(savedOwner.getId()).isPresent());
    }

    @Test
    void testGetByLoginAndPassword() {
        Owner owner = getOwner();
        repoOwner.save(owner);

        Optional<Owner> gotOwner = repoOwner.getByLoginAndPassword("example123", "pass123");

        assertTrue(gotOwner.isPresent());
        assertEquals("Ivan", gotOwner.get().getName());
    }

    @Test
    void testGetByLogin() {
        Owner owner = getOwner();
        repoOwner.save(owner);

        Optional<Owner> gotOwner = repoOwner.getByLogin("example123");

        assertTrue(gotOwner.isPresent());
        assertEquals("Ivan", gotOwner.get().getName());
    }
}
