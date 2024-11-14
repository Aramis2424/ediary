package org.srd.ediary.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Entry {
    private Long id;
    private Long diaryID;
    private Long moodID;
    private String title;
    private String content;
    private LocalDate createdDate;

    public Entry(Long diaryID, String title, String content) {
        this.id = null;
        this.diaryID = diaryID;
        this.moodID = null;
        this.title = title;
        this.content = content;
        this.createdDate = LocalDate.now();
    }

    public Long getId() {
        return id;
    }

    public Long getDiaryID() {
        return diaryID;
    }

    public Long getMoodID() {
        return moodID;
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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
