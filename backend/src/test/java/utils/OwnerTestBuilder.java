package utils;

import org.srd.ediary.domain.model.Owner;

import java.time.LocalDate;

public class OwnerTestBuilder {
    private String name = "Ivan";
    private LocalDate birthDate = LocalDate.of(2000, 1, 1);
    private String login = "example123";
    private String password = "pass123";

    public OwnerTestBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public OwnerTestBuilder withBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public OwnerTestBuilder withLogin(String login) {
        this.login = login;
        return this;
    }

    public OwnerTestBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public Owner build() {
        return new Owner(name, birthDate, login, password);
    }
}
