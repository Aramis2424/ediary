package org.srd.ediary.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.srd.ediary.application.dto.OwnerCreateDTO;
import org.srd.ediary.application.dto.OwnerInfoDTO;
import org.srd.ediary.domain.model.Owner;

@Mapper(componentModel = "spring")
public interface OwnerMapper {
    OwnerMapper INSTANCE = Mappers.getMapper(OwnerMapper.class);

//    @Mapping(target = "id" , ignore = true)
//    @Mapping(target = "createdDate" , defaultExpression = "java(java.time.LocalDate.now())")
//    Owner ownerCreateDtoToOwner(OwnerCreateDTO dto);
    OwnerInfoDTO OwnerToOwnerInfoDto(Owner owner);
}
