package org.srd.ediary.domain.model;

import java.time.LocalDate;

public class Diary {
    private Long id;
    private Long ownerID;
    private String title;
    private String description;
    private int cntEntry;
    private LocalDate createdDate;

    public Diary(Long ownerID, String title, String description, int cntEntry) {
        this.ownerID = ownerID;
        this.title = title;
        this.description = description;
        this.cntEntry = cntEntry;
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
}
