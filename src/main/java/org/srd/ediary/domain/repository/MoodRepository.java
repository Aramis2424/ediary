package org.srd.ediary.domain.repository;

import org.srd.ediary.domain.model.Mood;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MoodRepository extends CrudRepository<Mood> {
    List<Mood> getAllByOwner(Long id);
    Optional<Mood> getByOwnerIdAndCreatedDate(Long ownerId, LocalDate createdDate);
}
