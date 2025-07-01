package org.srd.ediary.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.srd.ediary.application.dto.DiaryCreateDTO;
import org.srd.ediary.application.dto.DiaryInfoDTO;
import org.srd.ediary.application.dto.DiaryUpdateDTO;
import org.srd.ediary.application.exception.DiaryNotFoundException;
import org.srd.ediary.application.exception.OwnerNotFoundException;
import org.srd.ediary.application.security.jwt.JwtFilter;
import org.srd.ediary.application.service.DiaryService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DiaryController.class)
class DiaryControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JwtFilter jwtFilter;
    @MockBean
    private DiaryService diaryService;
    private JacksonTester<DiaryCreateDTO> creationJson;
    private JacksonTester<DiaryUpdateDTO> updateJson;
    private final DiaryInfoDTO output = new DiaryInfoDTO(1L,"d1", "of1", 10, LocalDate.now());
    private final List<DiaryInfoDTO> listOutput = List.of(output);

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.findAndRegisterModules();
        JacksonTester.initFields(this, objectMapper);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new DiaryController(diaryService))
                .build();
    }
    @Test
    void testGetDiary_ExistingDiary() throws Exception {
        Long diaryId = 1L;
        when(diaryService.getDiary(diaryId)).thenReturn(output);

        mockMvc.perform(get("/api/v1/diaries/" + diaryId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(String.valueOf(diaryId))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.cntEntry").value(10));
    }

    @Test
    void testGetDiary_NonExistingDiary() throws Exception {
        Long diaryId = 1L;
        when(diaryService.getDiary(diaryId)).thenThrow(DiaryNotFoundException.class);

        mockMvc.perform(get("/api/v1/diaries/" + diaryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(diaryId))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetDiariesByOwner_ExistingOwner() throws Exception{
        Long ownerId = 5L;
        when(diaryService.getOwnerDiaries(ownerId)).thenReturn(listOutput);

        mockMvc.perform(get("/api/v1/owners/" + ownerId + "/diaries")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(String.valueOf(ownerId))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].cntEntry").value(10));
    }

    @Test
    void testGetDiariesByOwner_NonExistingOwner() throws Exception{
        Long ownerId = 5L;
        when(diaryService.getOwnerDiaries(ownerId)).thenThrow(OwnerNotFoundException.class);

        mockMvc.perform(get("/api/v1/owners/" + ownerId + "/diaries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(ownerId))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateDiary_WithExistingOwner() throws Exception {
        Long ownerId = 1L;
        DiaryCreateDTO input = new DiaryCreateDTO(ownerId, "d1", "of1");
        when(diaryService.create(input)).thenReturn(output);

        mockMvc.perform(post("/api/v1/diaries/")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .accept(MediaType.APPLICATION_JSON)
                .content(creationJson.write(input).getJson())
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.cntEntry").value(10));
    }

    @Test
    void testCreateDiary_WithNonExistingOwner() throws Exception {
        Long ownerId = 1L;
        DiaryCreateDTO input = new DiaryCreateDTO(ownerId, "d1", "of1");
        when(diaryService.create(input)).thenThrow(OwnerNotFoundException.class);

        mockMvc.perform(post("/api/v1/diaries/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(creationJson.write(input).getJson())
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateDiary_ExistingDiary() throws Exception {
        Long diaryId = 1L;
        DiaryUpdateDTO input = new DiaryUpdateDTO("d2", "of2");
        when(diaryService.update(diaryId, input)).thenReturn(output);

        mockMvc.perform(put("/api/v1/diaries/" + diaryId)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .accept(MediaType.APPLICATION_JSON)
                .content(updateJson.write(input).getJson())
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.cntEntry").value(10));
    }

    @Test
    void testUpdateDiary_NonExistingDiary() throws Exception {
        Long diaryId = 1L;
        DiaryUpdateDTO input = new DiaryUpdateDTO("d2", "of2");
        when(diaryService.update(diaryId, input)).thenThrow(DiaryNotFoundException.class);

        mockMvc.perform(put("/api/v1/diaries/" + diaryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(updateJson.write(input).getJson())
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteDiary() throws Exception {
        Long diaryId = 1L;
        doNothing().when(diaryService).remove(diaryId);

        mockMvc.perform(delete("/api/v1/diaries/" + diaryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(diaryId))
                )
                .andExpect(status().isOk());
    }
}