package org.srd.ediary.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.srd.ediary.application.dto.*;
import org.srd.ediary.application.exception.MoodNotFoundException;
import org.srd.ediary.application.exception.OwnerNotFoundException;
import org.srd.ediary.application.security.jwt.JwtFilter;
import org.srd.ediary.application.service.MoodService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static utils.MoodTestMother.*;

@Epic("Unit Tests")
@Feature("Controllers")
@WebMvcTest(MoodController.class)
@ActiveProfiles("unit_test")
class MoodControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JwtFilter jwtFilter;
    @MockBean
    private MoodService moodService;

    private JacksonTester<MoodCreateDTO> creationJson;
    private JacksonTester<MoodUpdateDTO> updateJson;

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.findAndRegisterModules();
        JacksonTester.initFields(this, objectMapper);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new MoodController(moodService))
                .build();
    }

    @Test
    void testGetMood_ExistingMood() throws Exception {
        Long moodId = 1L;
        MoodInfoDTO output = getMoodInfoDTO1();
        when(moodService.getMood(moodId)).thenReturn(output);

        mockMvc.perform(get("/api/v1/moods/" + moodId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(String.valueOf(moodId))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.scoreMood").value(1));
    }

    @Test
    void testGetMood_NonExistingMood() throws Exception {
        Long moodId = 1L;
        when(moodService.getMood(moodId)).thenThrow(MoodNotFoundException.class);

        mockMvc.perform(get("/api/v1/moods/" + moodId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(moodId))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetPermissionToCreateMood_Allowed() throws Exception {
        Long ownerId = 1L;
        LocalDate date = LocalDate.of(2020, 1, 1);
        when(moodService.canCreateMood(ownerId, date)).thenReturn(
                new MoodPermission(true));

        mockMvc.perform(get("/api/v1/owners/" + ownerId + "/can-create-mood")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(ownerId))
                        .param("date", "2020-01-01")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.allowed").value("true"));
    }

    @Test
    void testGetPermissionToCreateMood_NotAllowed() throws Exception {
        Long ownerId = 1L;
        LocalDate date = LocalDate.of(2020, 1, 1);
        when(moodService.canCreateMood(ownerId, date)).thenReturn(
                new MoodPermission(false));

        mockMvc.perform(get("/api/v1/owners/" + ownerId + "/can-create-mood")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(ownerId))
                        .param("date", "2020-01-01")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.allowed").value("false"));
    }

    @Test
    void testGetMoodByOwner_ExistingOwner() throws Exception {
        Long ownerId = 1L;
        List<MoodInfoDTO> listOutput = List.of(getMoodInfoDTO1());
        when(moodService.getMoodsByOwner(ownerId)).thenReturn(listOutput);

        mockMvc.perform(get("/api/v1/owners/" + ownerId + "/moods")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(ownerId))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].scoreMood").value(1));
    }

    @Test
    void testGetMoodByOwner_NonExistingOwner() throws Exception {
        Long ownerId = 1L;
        when(moodService.getMoodsByOwner(ownerId)).thenThrow(OwnerNotFoundException.class);

        mockMvc.perform(get("/api/v1/owners/" + ownerId + "/moods")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(ownerId))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateMood_WithExistingOwner() throws Exception {
        Long ownerId = 1L;
        MoodCreateDTO input = getMoodCreateDTO(ownerId);
        MoodInfoDTO output = getMoodInfoDTO1();
        when(moodService.create(input)).thenReturn(output);

        mockMvc.perform(post("/api/v1/moods/")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .accept(MediaType.APPLICATION_JSON)
                .content(creationJson.write(input).getJson())
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.scoreMood").value(1));
    }

    @Test
    void testCreateMood_WithNonExistingOwner() throws Exception {
        Long ownerId = 1L;
        MoodCreateDTO input = getMoodCreateDTO(ownerId);
        when(moodService.create(input)).thenThrow(OwnerNotFoundException.class);

        mockMvc.perform(post("/api/v1/moods/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(creationJson.write(input).getJson())
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateMood_ExistingMood() throws Exception {
        Long moodId = 1L;
        MoodUpdateDTO input = getMoodUpdateDTO();
        MoodInfoDTO output = getMoodInfoDTO1();
        when(moodService.update(moodId, input)).thenReturn(output);

        mockMvc.perform(put("/api/v1/moods/" + moodId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(updateJson.write(input).getJson())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.scoreMood").value(1));
    }

    @Test
    void testUpdateMood_NonExistingMood() throws Exception {
        Long moodId = 1L;
        MoodUpdateDTO input = getMoodUpdateDTO();
        when(moodService.update(moodId, input)).thenThrow(MoodNotFoundException.class);

        mockMvc.perform(put("/api/v1/moods/" + moodId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(updateJson.write(input).getJson())
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteMood() throws Exception {
        Long moodId = 1L;
        doNothing().when(moodService).delete(moodId);

        mockMvc.perform(delete("/api/v1/moods/" + moodId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(moodId))
                )
                .andExpect(status().isOk());
    }
}