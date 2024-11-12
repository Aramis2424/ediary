package org.srd.ediary.domain.repository;

import org.srd.ediary.domain.model.Diary;

import java.util.List;

public interface DiaryRepository extends CrudRepository<Diary> {
    List<Diary> getAllByOwnerID(Long id);
}
