package org.srd.ediary.infrastructure.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.srd.ediary.domain.model.Owner;
import org.srd.ediary.domain.repository.OwnerRepository;
import org.srd.ediary.infrastructure.entity.OwnerEntity;
import org.srd.ediary.infrastructure.exception.OwnerDeletionRestrictException;
import org.srd.ediary.infrastructure.mapper.OwnerEntityMapper;

import java.util.Optional;

interface SpringOwnerRepository extends CrudRepository<OwnerEntity, Long> {
    Optional<OwnerEntity> findByLoginAndPassword(String login, String password);
    Optional<OwnerEntity> findByLogin(String login);
}

@Repository
@RequiredArgsConstructor
public class OwnerRepositoryAdapter implements OwnerRepository {
    private final SpringOwnerRepository repo;

    @Override
    public Owner save(Owner model) {
        OwnerEntity entity = OwnerEntityMapper.INSTANCE.modelToEntity(model);
        OwnerEntity saved = repo.save(entity);
        return OwnerEntityMapper.INSTANCE.entityToModel(saved);
    }

    @Override
    public Optional<Owner> getByID(Long id) {
        return repo.findById(id).map(OwnerEntityMapper.INSTANCE::entityToModel);
    }

    @Override
    public void delete(Long id) {
        throw new OwnerDeletionRestrictException();
    }

    @Override
    public Optional<Owner> getByLoginAndPassword(String login, String password) {
        return repo.findByLoginAndPassword(login, password)
                .map(OwnerEntityMapper.INSTANCE::entityToModel);
    }

    @Override
    public Optional<Owner> getByLogin(String login) {
        return repo.findByLogin(login)
                .map(OwnerEntityMapper.INSTANCE::entityToModel);
    }
}
