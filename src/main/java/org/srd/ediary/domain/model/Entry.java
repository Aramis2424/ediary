package org.srd.ediary.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Entry {
    private Long id;
    private Diary diary;
    private Mood mood;
    private String title;
    private String content;
    private final LocalDate createdDate;

    public Entry(Diary diary, String title, String content) {
        this.id = null;
        this.diary = diary;
        this.mood = null;
        this.title = title;
        this.content = content;
        this.createdDate = LocalDate.now();
    }

    public Long getId() {
        return id;
    }

    public Diary getDiaryID() {
        return diary;
    }

    public Mood getMoodID() {
        return mood;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDiaryID(Diary diary) {
        this.diary = diary;
    }

    public void setMoodID(Mood mood) {
        this.mood = mood;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
