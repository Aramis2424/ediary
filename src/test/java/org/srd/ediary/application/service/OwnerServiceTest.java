package org.srd.ediary.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.srd.ediary.application.dto.OwnerCreateDTO;
import org.srd.ediary.application.dto.OwnerInfoDTO;
import org.srd.ediary.application.exception.InvalidCredentialsException;
import org.srd.ediary.application.exception.OwnerAlreadyExistException;
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

    private final LocalDate birthDate = LocalDate.of(2000, 1, 1);

    @Test
    void testLoginOwner_ExistingOwner() {
        String login = "example";
        String password = "abc123";
        Owner gotOwner = new Owner("Ivan", birthDate, login, password);
        OwnerInfoDTO expected = new OwnerInfoDTO(null,"Ivan", birthDate, login, LocalDate.now());
        when(encoder.matches(password, password)).thenReturn(true);
        when(ownerRepo.getByLogin(login)).thenReturn(Optional.of(gotOwner));

        OwnerInfoDTO actual = service.loginOwner(login, password);

        assertEquals(expected, actual);
    }

    @Test
    void testLoginOwner_NonExistingOwner() {
        String login = "example";
        String password = "abc123";
        when(ownerRepo.getByLogin(login)).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class, () -> service.loginOwner(login, password));
    }

    @Test
    void testLoginOwner_IncorrectPassword() {
        String login = "example";
        String password = "abc123";
        Owner gotOwner = new Owner("Ivan", birthDate, login, password);
        when(encoder.matches(password, password)).thenReturn(false);
        when(ownerRepo.getByLogin(login)).thenReturn(Optional.of(gotOwner));

        assertThrows(InvalidCredentialsException.class, () -> service.loginOwner(login, password));
    }

    @Test
    void testRegisterOwner_NonExistingLogin() {
        String login = "example";
        String password = "abc123";
        OwnerCreateDTO createDto = new OwnerCreateDTO("Ivan", birthDate, login, password);
        Owner createdOwner = new Owner("Ivan", birthDate, login, password);
        OwnerInfoDTO expected = new OwnerInfoDTO(null,"Ivan", birthDate, login, LocalDate.now());
        when(encoder.encode(anyString())).thenReturn(anyString());
        when(ownerRepo.getByLogin(login)).thenReturn(Optional.empty());
        when(ownerRepo.save(Mockito.any(Owner.class))).thenReturn(createdOwner);

        OwnerInfoDTO actual = service.registerOwner(createDto);

        verify(ownerRepo, times(1)).save(Mockito.any(Owner.class));
        assertEquals(expected, actual);
    }

    @Test
    void testRegisterOwner_AlreadyExistingLogin() {
        String login = "example";
        String password = "abc123";
        OwnerCreateDTO createDto = new OwnerCreateDTO("Ivan", birthDate, login, password);
        Owner existingOwner = new Owner("Ivan", birthDate, login, password);
        when(encoder.encode(anyString())).thenReturn(anyString());
        when(ownerRepo.getByLogin(login)).thenReturn(Optional.of(existingOwner));

        assertThrows(OwnerAlreadyExistException.class, () -> service.registerOwner(createDto));

        verify(ownerRepo, never()).save(Mockito.any(Owner.class));
    }
}