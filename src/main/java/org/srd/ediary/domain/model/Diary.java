package org.srd.ediary.domain.model;

import java.time.LocalDate;

public class Diary {
    private Long id;
    private Owner owner;
    private String title;
    private String description;
    private int cntEntry;
    private LocalDate createdDate;

    public Diary(Owner owner, String title, String description) {
        this.id = null;
        this.owner = owner;
        this.title = title;
        this.description = description;
        this.cntEntry = 0;
        this.createdDate = LocalDate.now();
    }

    public Long getId() {
        return id;
    }

    public Owner getOwner() {
        return owner;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getCntEntry() {
        return cntEntry;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCntEntry(int cntEntry) {
        this.cntEntry = cntEntry;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }
}
