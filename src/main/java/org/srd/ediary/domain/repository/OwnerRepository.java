package org.srd.ediary.domain.repository;

import org.srd.ediary.domain.model.Owner;

import java.util.Optional;

public interface OwnerRepository extends CrudRepository<Owner>{
    Optional<Owner> getByLoginAndPassword(String login, String password);
}
