package org.srd.ediary.domain.model;

import java.time.LocalDate;

public class Diary {
    private Long id;
    private Long ownerID;
    private String title;
    private String description;
    private int cntEntry;
    private LocalDate createdDate;

    public Diary(Long ownerID, String title, String description) {
        this.id = null;
        this.ownerID = ownerID;
        this.title = title;
        this.description = description;
        this.cntEntry = 0;
        this.createdDate = LocalDate.now();
    }

    public Long getId() {
        return id;
    }

    public Long getOwnerID() {
        return ownerID;
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

    public void setOwnerID(Long ownerID) {
        this.ownerID = ownerID;
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
