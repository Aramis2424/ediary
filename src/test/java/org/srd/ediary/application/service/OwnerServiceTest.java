package org.srd.ediary.application.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.srd.ediary.application.dto.OwnerCreateDTO;
import org.srd.ediary.application.dto.OwnerInfoDTO;
import org.srd.ediary.application.exception.ExistingLoginException;
import org.srd.ediary.domain.model.Owner;
import org.srd.ediary.domain.repository.OwnerRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OwnerServiceTest {
    @Mock
    private OwnerRepository ownerRepo;
    @Mock
    private PasswordEncoder encoder;
    @InjectMocks
    private OwnerService service;

    @Test
    void testLoginOwnerExisting() {
        String login = "example";
        String password = "abc123";
        Owner owner = new Owner("Ivan", LocalDate.of(2000, 1, 1), login, password);
        OwnerInfoDTO expected = new OwnerInfoDTO(null,"Ivan", LocalDate.of(2000, 1, 1),
                login, LocalDate.now());
        when(encoder.encode(anyString())).thenReturn("encodedPassword");
        when(ownerRepo.getByLoginAndPassword(login, "encodedPassword")).thenReturn(Optional.of(owner));

        OwnerInfoDTO actual = service.loginOwner(login, password);

        assertEquals(expected, actual);
    }

    @Test
    void testLoginOwnerNonExisting() {
        String login = "example";
        String password = "abc123";
        when(encoder.encode(anyString())).thenReturn("encodedPassword");
        when(ownerRepo.getByLoginAndPassword(login, "encodedPassword")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.loginOwner(login, password));
    }

    @Test
    void testRegisterOwnerNonExistingLogin() {
        String login = "example";
        String password = "abc123";
        OwnerCreateDTO createDto = new OwnerCreateDTO("Ivan",
                LocalDate.of(2000, 1, 1), login, password);
        OwnerInfoDTO expected = new OwnerInfoDTO(null,"Ivan", LocalDate.of(2000, 1, 1),
                login, LocalDate.now());
        when(encoder.encode(anyString())).thenReturn("encodedPassword");
        when(ownerRepo.getByLogin(login)).thenReturn(Optional.empty());
        when(ownerRepo.save(Mockito.any(Owner.class))).thenReturn(Mockito.any(Owner.class));

        OwnerInfoDTO actual = service.registerOwner(createDto);

        verify(ownerRepo).save(Mockito.any(Owner.class));
        assertEquals(expected, actual);
    }

    @Test
    void testRegisterOwnerExistingLogin() {
        String login = "example";
        String password = "abc123";
        OwnerCreateDTO createDto = new OwnerCreateDTO("Ivan",
                LocalDate.of(2000, 1, 1), login, password);
        Owner owner = new Owner("Ivan", LocalDate.of(2000, 1, 1), login, password);
        when(encoder.encode(anyString())).thenReturn("encodedPassword");
        when(ownerRepo.getByLogin(login)).thenReturn(Optional.of(owner));

        assertThrows(ExistingLoginException.class, () -> service.registerOwner(createDto));

        verify(ownerRepo, never()).save(Mockito.any(Owner.class));
    }
}