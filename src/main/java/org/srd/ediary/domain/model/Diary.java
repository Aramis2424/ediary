package org.srd.ediary.domain.model;

import java.time.LocalDate;

public class Diary {
    private Long id;
    private Long ownerID;
    private String title;
    private String description;
    private int cntEntry;
    private LocalDate createdDate;
}
