package org.srd.ediary.infrastructure.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.srd.ediary.domain.model.Mood;
import org.srd.ediary.infrastructure.entity.MoodEntity;

@Mapper
public interface MoodEntityMapper {
    MoodEntityMapper INSTANCE = Mappers.getMapper(MoodEntityMapper.class);

    MoodEntity modelToEntity(Mood model);
    Mood entityToModel(MoodEntity entity);
}
