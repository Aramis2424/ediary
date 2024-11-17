package org.srd.ediary.infrastructure.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.srd.ediary.domain.model.Owner;
import org.srd.ediary.infrastructure.entity.OwnerEntity;

@Mapper
public interface OwnerEntityMapper {
    OwnerEntityMapper INSTANCE = Mappers.getMapper(OwnerEntityMapper.class);

    OwnerEntity modelToEntity(Owner model);
    Owner entityToModel(OwnerEntity entity);
}
