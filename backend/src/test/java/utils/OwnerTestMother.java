package utils;

import org.srd.ediary.application.dto.OwnerCreateDTO;
import org.srd.ediary.application.dto.OwnerInfoDTO;
import org.srd.ediary.domain.model.Owner;

import java.time.LocalDate;

public class OwnerTestMother {
    public static final String login = "example123";
    public static final String password = "pass123";
    private static final LocalDate birthDate = LocalDate.of(2000, 1, 1);

    public static OwnerInfoDTO getOwnerInfoDTO() {
        return new OwnerInfoDTO(null,"Ivan", birthDate, login, LocalDate.now());
    }
    public static Owner getOwner() {
        return new OwnerTestBuilder().build();
    }
    public static OwnerTestBuilder getOwnerBuilder() {
        return new OwnerTestBuilder();
    }
    public static OwnerCreateDTO getOwnerCreateDTO() {
        return new OwnerCreateDTO("Ivan", birthDate, login, password);
    }
}
