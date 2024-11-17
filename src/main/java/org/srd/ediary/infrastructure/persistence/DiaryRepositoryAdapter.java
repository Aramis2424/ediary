package org.srd.ediary.infrastructure.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.srd.ediary.domain.model.Diary;
import org.srd.ediary.domain.model.Owner;
import org.srd.ediary.domain.repository.DiaryRepository;
import org.srd.ediary.infrastructure.entity.DiaryEntity;
import org.srd.ediary.infrastructure.mapper.DiaryEntityMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

interface SpringDiaryRepository extends CrudRepository<DiaryEntity, Long> {
    List<DiaryEntity> findAllByOwnerId(Long id);
}

@Repository
@RequiredArgsConstructor
public class DiaryRepositoryAdapter implements DiaryRepository {
    private final SpringDiaryRepository repo;
    @Override
    public Diary save(Diary model) {
        DiaryEntity entity = DiaryEntityMapper.INSTANCE.modelToEntity(model);
        DiaryEntity saved = repo.save(entity);
        return DiaryEntityMapper.INSTANCE.entityToModel(saved);
    }

    @Override
    public Optional<Diary> getByID(Long id) {
        return repo.findById(id).map(DiaryEntityMapper.INSTANCE::entityToModel);
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }

    @Override
    public List<Diary> getAllByOwner(Long id) {
        List<DiaryEntity> diaryEntities = repo.findAllByOwnerId(id);
        List<Diary> diaries = new ArrayList<>(diaryEntities.size());
        for (var diary : diaryEntities)
            diaries.add(DiaryEntityMapper.INSTANCE.entityToModel(diary));
        return diaries;
    }
}
