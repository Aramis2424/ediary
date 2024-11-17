package org.srd.ediary.domain.repository;

import org.srd.ediary.domain.model.Entry;

import java.util.List;

public interface EntryRepository extends CrudRepository<Entry> {
    List<Entry> getAllByDiary(Long id);
}
