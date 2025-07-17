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
import static utils.OwnerTestFactory.*;

@ExtendWith(MockitoExtension.class)
class OwnerServiceTest {
    @Mock
    private OwnerRepository ownerRepo;
    @Mock
    private PasswordEncoder encoder;
    @InjectMocks
    private OwnerService service;

    @Test
    void testLoginOwner_ExistingOwner() {
        Owner gotOwner = getOwner();
        OwnerInfoDTO expected = getOwnerInfoDTO();
        when(encoder.matches(password, password)).thenReturn(true);
        when(ownerRepo.getByLogin(login)).thenReturn(Optional.of(gotOwner));

        OwnerInfoDTO actual = service.loginOwner(login, password);

        assertEquals(expected, actual);
    }

    @Test
    void testLoginOwner_NonExistingOwner() {
        when(ownerRepo.getByLogin(login)).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class,
                () -> service.loginOwner(login, password));
    }

    @Test
    void testLoginOwner_IncorrectPassword() {
        Owner gotOwner = getOwner();
        when(encoder.matches(password, password)).thenReturn(false);
        when(ownerRepo.getByLogin(login)).thenReturn(Optional.of(gotOwner));

        assertThrows(InvalidCredentialsException.class,
                () -> service.loginOwner(login, password));
    }

    @Test
    void testRegisterOwner_NonExistingLogin() {
        OwnerCreateDTO createDto = getOwnerCreateDTO();
        Owner createdOwner = getOwner();
        OwnerInfoDTO expected = getOwnerInfoDTO();
        when(encoder.encode(anyString())).thenReturn(anyString());
        when(ownerRepo.getByLogin(login)).thenReturn(Optional.empty());
        when(ownerRepo.save(Mockito.any(Owner.class))).thenReturn(createdOwner);

        OwnerInfoDTO actual = service.registerOwner(createDto);

        verify(ownerRepo, times(1)).save(Mockito.any(Owner.class));
        assertEquals(expected, actual);
    }

    @Test
    void testRegisterOwner_AlreadyExistingLogin() {
        OwnerCreateDTO createDto = getOwnerCreateDTO();
        Owner existingOwner = getOwner();
        when(encoder.encode(anyString())).thenReturn(anyString());
        when(ownerRepo.getByLogin(login)).thenReturn(Optional.of(existingOwner));

        assertThrows(OwnerAlreadyExistException.class, () -> service.registerOwner(createDto));

        verify(ownerRepo, never()).save(Mockito.any(Owner.class));
    }

    @Test
    void testFetchOwner_ExistingOwner() {
        Long existingId = 1L;
        Owner gotOwner = getOwner();
        OwnerInfoDTO expected = getOwnerInfoDTO();
        when(ownerRepo.getByID(existingId)).thenReturn(Optional.of(gotOwner));

        OwnerInfoDTO actual = service.fetchOwner(existingId);

        assertEquals(expected, actual);
    }

    @Test
    void testFetchOwner_NonExistingOwner() {
        Long nonExistingId = 0L;
        when(ownerRepo.getByID(nonExistingId)).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class, () -> service.fetchOwner(nonExistingId));
    }
}
