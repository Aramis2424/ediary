package org.srd.ediary.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.srd.ediary.application.dto.EntryInfoDTO;
import org.srd.ediary.domain.model.Entry;

@Mapper
public interface EntryMapper {
    EntryMapper INSTANCE = Mappers.getMapper(EntryMapper.class);

    EntryInfoDTO EntryToEntryInfoDto(Entry entry);
}
