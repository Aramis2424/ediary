package org.srd.ediary.infrastructure.persistence;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.srd.ediary.domain.model.Owner;
import org.srd.ediary.infrastructure.exception.OwnerDeletionRestrictException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static utils.OwnerTestMother.getOwner;
import static utils.OwnerTestMother.getOwnerBuilder;

@Epic("Integration Tests")
@Feature("Database")
@DataJpaTest
@ActiveProfiles("integration_test")
class OwnerRepositoryAdapterTest {
    @Autowired
    private SpringOwnerRepository springOwnerRepo;
    private OwnerRepositoryAdapter repoOwner;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        repoOwner = new OwnerRepositoryAdapter(springOwnerRepo);
    }

    @Test
    void testSave_Correct() {
        Owner owner = getOwner();

        Owner savedOwner = repoOwner.save(owner);

        assertEquals("Ivan", savedOwner.getName());
    }

    @Test
    void testSave_NonUniqueLogin() {
        Owner owner = getOwnerBuilder()
                .withLogin("log1")
                .build();
        repoOwner.save(owner);

        assertThrows(DataIntegrityViolationException.class,
                () -> repoOwner.save(owner));
    }

    @Test
    void testGetByID_Existing() {
        Owner owner = getOwner();
        Owner savedOwner = repoOwner.save(owner);

        Optional<Owner> gotOwner = repoOwner.getByID(savedOwner.getId());

        assertTrue(gotOwner.isPresent());
        assertEquals("Ivan", gotOwner.get().getName());
    }

    @Test
    void testGetByID_NonExisting() {
        Long ownerId = -1L;

        Optional<Owner> gotOwner = repoOwner.getByID(ownerId);

        assertTrue(gotOwner.isEmpty());
    }

    @Test
    void testDelete_Correct() {
        Owner owner = getOwner();
        Owner savedOwner = repoOwner.save(owner);

        assertThrows(OwnerDeletionRestrictException.class,
                () -> repoOwner.delete(savedOwner.getId()));

        assertTrue(repoOwner.getByID(savedOwner.getId()).isPresent());
    }

    @Test
    void testGetByLoginAndPassword_Existing() {
        Owner owner = getOwner();
        repoOwner.save(owner);

        Optional<Owner> gotOwner = repoOwner.getByLoginAndPassword("example123", "pass123");

        assertTrue(gotOwner.isPresent());
        assertEquals("Ivan", gotOwner.get().getName());
    }

    @Test
    void testGetByLoginAndPassword_NonExisting() {
        Owner owner = getOwner();
        repoOwner.save(owner);

        Optional<Owner> gotOwner = repoOwner.getByLoginAndPassword("log1", "pass1");

        assertTrue(gotOwner.isEmpty());
    }

    @Test
    void testGetByLogin_Existing() {
        Owner owner = getOwner();
        repoOwner.save(owner);

        Optional<Owner> gotOwner = repoOwner.getByLogin("example123");

        assertTrue(gotOwner.isPresent());
        assertEquals("Ivan", gotOwner.get().getName());
    }

    @Test
    void testGetByLogin_NonExisting() {
        Owner owner = getOwner();
        repoOwner.save(owner);

        Optional<Owner> gotOwner = repoOwner.getByLogin("log1");

        assertTrue(gotOwner.isEmpty());
    }
}
