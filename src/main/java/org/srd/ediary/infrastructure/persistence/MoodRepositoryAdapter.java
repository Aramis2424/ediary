package org.srd.ediary.infrastructure.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.srd.ediary.domain.model.Mood;
import org.srd.ediary.domain.repository.MoodRepository;
import org.srd.ediary.infrastructure.entity.MoodEntity;
import org.srd.ediary.infrastructure.mapper.MoodEntityMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

interface SpringMoodRepository extends CrudRepository<MoodEntity, Long> {
    List<MoodEntity> findAllByOwnerId(Long id);
}

@Repository
@RequiredArgsConstructor
public class MoodRepositoryAdapter implements MoodRepository {
    private final SpringMoodRepository repo;

    @Override
    public Mood save(Mood model) {
        MoodEntity entity = MoodEntityMapper.INSTANCE.modelToEntity(model);
        MoodEntity saved = repo.save(entity);
        return MoodEntityMapper.INSTANCE.entityToModel(saved);
    }

    @Override
    public Optional<Mood> getByID(Long id) {
        return repo.findById(id).map(MoodEntityMapper.INSTANCE::entityToModel);
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }

    @Override
    public List<Mood> getAllByOwner(Long id) {
        List<MoodEntity> moodEntities = repo.findAllByOwnerId(id);
        List<Mood> moods = new ArrayList<>(moodEntities.size());
        for (var mood : moodEntities)
            moods.add(MoodEntityMapper.INSTANCE.entityToModel(mood));
        return moods;
    }
}
