package org.srd.ediary.domain.repository;

import java.util.Optional;

public interface CrudRepository<T> {
    T save(T model);
    Optional<T> getByID(Long id);
    void delete(Long id);
}
