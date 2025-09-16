package integration.service;

import integration.context.WithMockOwnerDetails;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.srd.ediary.application.dto.OwnerCreateDTO;
import org.srd.ediary.application.dto.OwnerInfoDTO;
import org.srd.ediary.application.exception.InvalidCredentialsException;
import org.srd.ediary.application.exception.OwnerAlreadyExistException;
import org.srd.ediary.application.service.OwnerService;
import org.srd.ediary.domain.model.Owner;
import utils.OwnerTestMother;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static utils.OwnerTestMother.*;

@Epic("Integration Tests")
@Feature("Service")
public class OwnerServiceIT extends BaseIT {
    @Autowired
    private OwnerService service;

    private final static long validOwnerId = 1L;
    private final static long invalidOwnerId = 2L;

    private OwnerCreateDTO bmstuDtoSaving;
    private OwnerInfoDTO bmstuDtoSaved;

    @BeforeEach
    void init_db() {
        Owner newOwner = OwnerTestMother.getOwnerBuilder()
                        .withLogin("bmstu")
                        .withPassword("123abc")
                        .build();
        bmstuDtoSaving = new OwnerCreateDTO(newOwner.getName(), newOwner.getBirthDate(),
                newOwner.getLogin(), newOwner.getPassword());
        bmstuDtoSaved = service.registerOwner(bmstuDtoSaving);
    }

    @Test
    @WithMockOwnerDetails(id = validOwnerId)
    void testLoginOwner_Bmstu() {
        OwnerInfoDTO expected = bmstuDtoSaved;

        OwnerInfoDTO actual = service.loginOwner("bmstu", "123abc");

        assertEquals(expected.login(), actual.login());
    }

    @Test
    void testLoginOwner_ExistingOwner() {
        OwnerInfoDTO expected = getOwnerInfoDTO();

        OwnerInfoDTO actual = service.loginOwner(login, password);

        assertEquals(expected.login(), actual.login());
    }

    @Test
    void testLoginOwner_NonExistingOwner() {
        String invalidLogin = "example321";

        assertThrows(InvalidCredentialsException.class,
                () -> service.loginOwner(invalidLogin, password));
    }

    @Test
    void testLoginOwner_IncorrectPassword() {
        String incorrectPassword = "pass321";

        assertThrows(InvalidCredentialsException.class,
                () -> service.loginOwner(login, incorrectPassword));
    }

    @Test
    void testRegisterOwner_NonExistingLogin() {
        OwnerCreateDTO createDto = new OwnerCreateDTO("Ivan",
                birthDate, "newLog", password);
        OwnerInfoDTO expected = new OwnerInfoDTO(3L, "Ivan",
                birthDate, "newLog", LocalDate.now());

        OwnerInfoDTO actual = service.registerOwner(createDto);

        assertEquals(expected, actual);
    }

    @Test
    void testRegisterOwner_AlreadyExistingLogin() {
        OwnerCreateDTO createDto = getOwnerCreateDTO();

        assertThrows(OwnerAlreadyExistException.class,
                () -> service.registerOwner(createDto));

    }

    @Test
    @WithMockOwnerDetails(id = validOwnerId)
    void testFetchOwner_ExistingOwner() {
        Long existingId = 1L;
        OwnerInfoDTO expected = getOwnerInfoDTO();

        OwnerInfoDTO actual = service.fetchOwner(existingId);

        assertEquals(expected.login(), actual.login());
    }

    @Test
    @WithMockOwnerDetails(id = invalidOwnerId)
    void testFetchOwner_NonExistingOwner() {
        Long nonExistingId = 0L;

        assertThrows(AuthorizationDeniedException.class,
                () -> service.fetchOwner(nonExistingId));
    }
}
