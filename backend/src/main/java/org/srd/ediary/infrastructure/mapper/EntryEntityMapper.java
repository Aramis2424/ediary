package org.srd.ediary.infrastructure.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.srd.ediary.domain.model.Entry;
import org.srd.ediary.infrastructure.entity.EntryEntity;

@Mapper
public interface EntryEntityMapper {
    EntryEntityMapper INSTANCE = Mappers.getMapper(EntryEntityMapper.class);

    EntryEntity modelToEntity(Entry model);
    Entry entityToModel(EntryEntity entity);
}
