package org.srd.ediary.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Mood {
    private Long id;
    private Long ownerID;
    private int scoreMood;
    private int scoreProductivity;
    private LocalDateTime bedtime;
    private LocalDateTime wakeUpTime;
    private LocalDate createdAt;
}
