package org.srd.ediary.domain.repository;

import org.srd.ediary.domain.model.Mood;

import java.util.List;

public interface MoodRepository extends CrudRepository<Mood> {
    List<Mood> getAllByOwner(Long id);
}
