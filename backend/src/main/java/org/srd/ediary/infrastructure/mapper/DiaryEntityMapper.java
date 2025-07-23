package org.srd.ediary.infrastructure.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.srd.ediary.domain.model.Diary;
import org.srd.ediary.infrastructure.entity.DiaryEntity;

@Mapper
public interface DiaryEntityMapper {
    DiaryEntityMapper INSTANCE = Mappers.getMapper(DiaryEntityMapper.class);

    DiaryEntity modelToEntity(Diary diary);
    Diary entityToModel(DiaryEntity entity);
}
