package org.srd.ediary.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.srd.ediary.application.dto.DiaryInfoDTO;
import org.srd.ediary.domain.model.Diary;

@Mapper
public interface DiaryMapper {
    DiaryMapper INSTANCE = Mappers.getMapper(DiaryMapper.class);

    DiaryInfoDTO diaryToDiaryInfoDto(Diary diary);
}
