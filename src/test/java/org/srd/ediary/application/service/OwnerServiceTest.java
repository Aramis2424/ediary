package org.srd.ediary.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.srd.ediary.application.dto.OwnerInfoDTO;
import org.srd.ediary.domain.model.Owner;
import org.srd.ediary.domain.repository.OwnerRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OwnerServiceTest {
    @Mock
    private OwnerRepository ownerRepo;
    @InjectMocks
    private OwnerService service;

    @Test
    void testLoginOwnerExisting() {
        String login = "example";
        String password = "abc123";
        Owner owner = new Owner("Ivan", LocalDate.of(2000, 1, 1), login, password);
        OwnerInfoDTO expected = new OwnerInfoDTO("Ivan", LocalDate.of(2000, 1, 1),
                login, LocalDate.now());
        Mockito.when(ownerRepo.getByLoginAndPassword(login, password)).thenReturn(Optional.of(owner));

        OwnerInfoDTO actual = service.loginOwner(login, password);

        assertEquals(expected, actual);
    }

    @Test
    void testLoginOwnerNonExisting() {

    }
}