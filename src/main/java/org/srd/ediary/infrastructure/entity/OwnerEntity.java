package org.srd.ediary.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "owners")
public class OwnerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "owner_id")
    private Long id;
    @Column(length = 255)
    private String name;
    @Column(name = "birth_date")
    private LocalDate birthDate;
    @Column(length = 255)
    private String login;
    @Column(length = 255)
    private String password;
    @Column(name = "created_date")
    private LocalDate createdDate;
}
