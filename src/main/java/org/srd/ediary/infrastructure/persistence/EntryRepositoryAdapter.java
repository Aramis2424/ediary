package org.srd.ediary.infrastructure.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.srd.ediary.domain.model.Entry;
import org.srd.ediary.domain.repository.EntryRepository;
import org.srd.ediary.infrastructure.entity.EntryEntity;
import org.srd.ediary.infrastructure.mapper.EntryEntityMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

interface SpringEntryRepository extends CrudRepository<EntryEntity, Long> {
    List<EntryEntity> getAllByDiaryId(Long id);
}

@Repository
@RequiredArgsConstructor
public class EntryRepositoryAdapter implements EntryRepository {
    private final SpringEntryRepository repo;

    @Override
    public Entry save(Entry model) {
        EntryEntity entity = EntryEntityMapper.INSTANCE.modelToEntity(model);
        EntryEntity saved = repo.save(entity);
        return EntryEntityMapper.INSTANCE.entityToModel(saved);
    }

    @Override
    public Optional<Entry> getByID(Long id) {
        return repo.findById(id).map(EntryEntityMapper.INSTANCE::entityToModel);
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }

    @Override
    public List<Entry> getAllByDiary(Long id) {
        List<EntryEntity> entityList = repo.getAllByDiaryId(id);
        return entityList.stream()
                .map(EntryEntityMapper.INSTANCE::entityToModel)
                .collect(Collectors.toList());
    }
}
