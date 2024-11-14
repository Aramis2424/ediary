package org.srd.ediary.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.srd.ediary.application.dto.MoodInfoDTO;
import org.srd.ediary.domain.model.Mood;

@Mapper
public interface MoodMapper {
    MoodMapper INSTANCE = Mappers.getMapper(MoodMapper.class);

    MoodInfoDTO moodToMoodInfoDto(Mood mood);
}
