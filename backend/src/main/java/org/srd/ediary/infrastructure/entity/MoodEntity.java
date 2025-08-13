package org.srd.ediary.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.srd.ediary.domain.model.Owner;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "moods")
public class MoodEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mood_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_fk", nullable = false)
    private OwnerEntity owner;
    @Column(name = "score_mood", nullable = false)
    private int scoreMood;
    @Column(name = "score_productivity", nullable = false)
    private int scoreProductivity;
    @Column(name = "bedtime", nullable = false)
    private LocalDateTime bedtime;
    @Column(name = "wake_up_time", nullable = false)
    private LocalDateTime wakeUpTime;
    @Column(name = "created_date", nullable = false)
    private LocalDate createdAt;
}
