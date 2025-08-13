package org.srd.ediary.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.srd.ediary.domain.model.Owner;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "diaries")
public class DiaryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_fk", nullable = false)
    private OwnerEntity owner;
    @Column(length = 63, nullable = false)
    private String title;
    @Column(length = 127)
    private String description;
    @Column(name = "cnt_entries", nullable = false)
    private int cntEntry;
    @Column(name = "created_date", nullable = false)
    private LocalDate createdDate;
}
