package org.srd.ediary.domain.model;

import java.time.LocalDate;

public class Entry {
    private Long id;
    private Diary diary;
    private String title;
    private String content;
    private LocalDate createdDate;

    public Entry() {
    }

    public Entry(Diary diary, String title, String content) {
        this.id = null;
        this.diary = diary;
        this.title = title;
        this.content = content;
        this.createdDate = LocalDate.now();
    }

    public Long getId() {
        return id;
    }

    public Diary getDiary() {
        return diary;
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

    public void setDiary(Diary diary) {
        this.diary = diary;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }
}
