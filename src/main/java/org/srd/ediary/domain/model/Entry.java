package org.srd.ediary.domain.model;

import java.time.LocalDate;

public class Entry {
    private Long id;
    private Long diaryID;
    private Long moodID;
    private String title;
    private String content;
    private LocalDate createdDate;
}
