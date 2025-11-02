package org.srd.ediary.infrastructure.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.srd.ediary.domain.model.Entry;
import org.srd.ediary.domain.repository.EntryRepository;
import org.srd.ediary.infrastructure.entity.EntryEntity;
import org.srd.ediary.infrastructure.mapper.EntryEntityMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

interface SpringEntryRepository extends CrudRepository<EntryEntity, Long> {
    List<EntryEntity> getAllByDiaryId(Long id);
    Optional<EntryEntity> getByDiaryIdAndCreatedDate(Long diaryId, LocalDate createdDate);
    @Query("""
       SELECT e
       FROM EntryEntity e
       WHERE e.diary.id = :diaryId
         AND (:title IS NULL OR :title = '' OR LOWER(e.title) LIKE LOWER(CONCAT('%', :title, '%')))
       """)
    List<EntryEntity> getAllByDiaryIdAndTitle(
            @Param("diaryId") Long diaryId,
            @Param("title") String title
    );

    @Query("""
        SELECT e FROM EntryEntity e
        WHERE (:title IS NULL OR LOWER(e.title) LIKE LOWER(CONCAT('%', COALESCE(:title, ''), '%')))
          AND (e.createdDate >= :dateFrom)
          AND (e.createdDate <= :dateTo)
          AND (:diaryId IS NULL OR e.diary.id = :diaryId)
        ORDER BY e.createdDate DESC
    """)
    List<EntryEntity> searchEntries(
            @Param("diaryId") Long diaryId,
            @Param("title") String title,
            @Param("dateFrom") LocalDate dateFrom,
            @Param("dateTo") LocalDate dateTo
    );
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

    @Override
    public List<Entry> getByDiaryAndTitle(Long id, String title) {
        List<EntryEntity> entityList = repo.getAllByDiaryIdAndTitle(id, title);
        return entityList.stream()
                .map(EntryEntityMapper.INSTANCE::entityToModel)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Entry> getByDiaryIdAndCreatedDate(Long diaryId, LocalDate createdDate) {
        return repo.getByDiaryIdAndCreatedDate(diaryId, createdDate).map(EntryEntityMapper.INSTANCE::entityToModel);
    }

    @Override
    public List<Entry> getAllWithFilter(Long diaryId, String title, LocalDate dateFrom, LocalDate dateTo) {
        System.out.println(dateFrom);
        System.out.println(dateTo);
        List<EntryEntity> entityList = repo.searchEntries(diaryId, title, dateFrom, dateTo);
        return entityList.stream()
                .map(EntryEntityMapper.INSTANCE::entityToModel)
                .collect(Collectors.toList());
    }
}
