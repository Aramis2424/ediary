package org.srd.ediary.domain.repository;

import java.util.Optional;

public interface CrudRepository<T> {
    T save(T model);
    Optional<T> getByID(Long id);
    T update(T model);
    void delete(Long id);
}
