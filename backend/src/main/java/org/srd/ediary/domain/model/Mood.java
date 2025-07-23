package org.srd.ediary.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Mood {
    private Long id;
    private Owner owner;
    private int scoreMood;
    private int scoreProductivity;
    private LocalDateTime bedtime;
    private LocalDateTime wakeUpTime;
    private LocalDate createdAt;

    public Mood() {}

    public Mood(Owner owner, int scoreMood, int scoreProductivity, LocalDateTime bedtime, LocalDateTime wakeUpTime) {
        this.id = null;
        this.owner = owner;
        this.scoreMood = scoreMood;
        this.scoreProductivity = scoreProductivity;
        this.bedtime = bedtime;
        this.wakeUpTime = wakeUpTime;
        this.createdAt = LocalDate.now();
    }

    public Long getId() {
        return id;
    }

    public Owner getOwner() {
        return owner;
    }

    public int getScoreMood() {
        return scoreMood;
    }

    public int getScoreProductivity() {
        return scoreProductivity;
    }

    public LocalDateTime getBedtime() {
        return bedtime;
    }

    public LocalDateTime getWakeUpTime() {
        return wakeUpTime;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public void setScoreMood(int scoreMood) {
        this.scoreMood = scoreMood;
    }

    public void setScoreProductivity(int scoreProductivity) {
        this.scoreProductivity = scoreProductivity;
    }

    public void setBedtime(LocalDateTime bedtime) {
        this.bedtime = bedtime;
    }

    public void setWakeUpTime(LocalDateTime wakeUpTime) {
        this.wakeUpTime = wakeUpTime;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }
}
