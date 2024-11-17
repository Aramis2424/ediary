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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_fk")
    private OwnerEntity owner;
    @Column(name = "score_mood")
    private int scoreMood;
    @Column(name = "score_productivity")
    private int scoreProductivity;
    @Column(name = "bedtime")
    private LocalDateTime bedtime;
    @Column(name = "wake_up_time")
    private LocalDateTime wakeUpTime;
    @Column(name = "created_date")
    private LocalDate createdAt;
}
