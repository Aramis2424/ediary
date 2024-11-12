package org.srd.ediary.domain.model;

import java.time.LocalDate;

public class Owner {
    private final Long id;
    private final String name;
    private final LocalDate birthDate;
    private final String login;
    private final String password;
    private final LocalDate createdDate;

    public Owner(String name, LocalDate birthDate, String login, String password) {
        this.id = -1L;
        this.name = name;
        this.birthDate = birthDate;
        this.login = login;
        this.password = password;
        this.createdDate = LocalDate.now();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }
}
