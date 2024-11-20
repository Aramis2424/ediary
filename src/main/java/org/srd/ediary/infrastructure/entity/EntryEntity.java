package org.srd.ediary.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.srd.ediary.domain.model.Diary;
import org.srd.ediary.domain.model.Mood;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "entries")
public class EntryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "entry_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_fk")
    private DiaryEntity diary;
    private String title;
    private String content;
    @Column(name = "created_date")
    private LocalDate createdDate;
}
