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
    @Column(name = "diary_fk")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_fk")
    private Diary diary;
    @Column(name = "mood_fk")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mood_fk")
    private Mood mood;

    private String title;
    private String content;
    @Column(name = "created_date")
    private LocalDate createdDate;
}
