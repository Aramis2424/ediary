package org.srd.ediary.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "owners")
public class DiaryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_id")
    private Long id;
    @Column(name = "owner_fid")
    private Long ownerID;
    @Column(length = 255)
    private String title;
    @Column(length = 255)
    private String description;
    @Column(name = "cnt_entries")
    private int cntEntry;
    @Column(name = "created_date")
    private LocalDate createdDate;
}
