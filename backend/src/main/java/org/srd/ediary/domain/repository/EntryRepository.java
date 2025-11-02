package org.srd.ediary.domain.repository;

import org.srd.ediary.domain.model.Entry;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EntryRepository extends CrudRepository<Entry> {
    List<Entry> getAllByDiary(Long id);
    List<Entry> getByDiaryAndTitle(Long id, String title);
    Optional<Entry> getByDiaryIdAndCreatedDate(Long diaryId, LocalDate createdDate);
    List<Entry> getAllWithFilter(Long diaryId, String title, LocalDate dateFrom, LocalDate dateTo);
}
