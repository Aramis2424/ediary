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
import org.srd.ediary.application.dto.MoodCreateDTO;
import org.srd.ediary.application.dto.MoodInfoDTO;
import org.srd.ediary.application.dto.MoodUpdateDTO;
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

@WebMvcTest(MoodController.class)
class MoodControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JwtFilter jwtFilter;
    @MockBean
    private MoodService moodService;
    private JacksonTester<MoodCreateDTO> creationJson;
    private JacksonTester<MoodUpdateDTO> updateJson;

    private final LocalDateTime bedtime = LocalDateTime
            .of(2020, 1,1, 22,30);
    private final LocalDateTime wakeUpTime = LocalDateTime
            .of(2020, 1,2, 8,30);
    private final MoodInfoDTO output = new MoodInfoDTO(1L, 7, 8,
            bedtime, wakeUpTime, LocalDate.now());
    private final List<MoodInfoDTO> listOutput = List.of(output);

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
        when(moodService.getMood(moodId)).thenReturn(output);

        mockMvc.perform(get("/moods/" + moodId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(String.valueOf(moodId))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.scoreMood").value(7));
    }

    @Test
    void testGetMood_NonExistingMood() throws Exception {
        Long moodId = 1L;
        when(moodService.getMood(moodId)).thenThrow(MoodNotFoundException.class);

        mockMvc.perform(get("/moods/" + moodId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(moodId))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetMoodByOwner_ExistingOwner() throws Exception {
        Long ownerId = 5L;
        when(moodService.getMoodsByOwner(ownerId)).thenReturn(listOutput);

        mockMvc.perform(get("/moods/owner/" + ownerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(ownerId))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].scoreMood").value(7));
    }

    @Test
    void testGetMoodByOwner_NonExistingOwner() throws Exception {
        Long ownerId = 5L;
        when(moodService.getMoodsByOwner(ownerId)).thenThrow(OwnerNotFoundException.class);

        mockMvc.perform(get("/moods/owner/" + ownerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(ownerId))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateMood_WithExistingOwner() throws Exception {
        MoodCreateDTO input = new MoodCreateDTO(5L, 7, 8, bedtime, wakeUpTime);
        when(moodService.create(input)).thenReturn(output);

        mockMvc.perform(post("/moods/")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .accept(MediaType.APPLICATION_JSON)
                .content(creationJson.write(input).getJson())
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.scoreMood").value(7));
    }

    @Test
    void testCreateMood_WithNonExistingOwner() throws Exception {
        MoodCreateDTO input = new MoodCreateDTO(6L, 7, 8, bedtime, wakeUpTime);
        when(moodService.create(input)).thenThrow(OwnerNotFoundException.class);

        mockMvc.perform(post("/moods/")
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
        MoodUpdateDTO input = new MoodUpdateDTO(7, 8, bedtime, wakeUpTime);
        when(moodService.update(moodId, input)).thenReturn(output);

        mockMvc.perform(put("/moods/" + moodId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(updateJson.write(input).getJson())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.scoreMood").value(7));
    }

    @Test
    void testUpdateMood_NonExistingMood() throws Exception {
        Long moodId = 1L;
        MoodUpdateDTO input = new MoodUpdateDTO(7, 8, bedtime, wakeUpTime);
        when(moodService.update(moodId, input)).thenThrow(MoodNotFoundException.class);

        mockMvc.perform(put("/moods/" + moodId)
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

        mockMvc.perform(delete("/moods/" + moodId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(moodId))
                )
                .andExpect(status().isOk());
    }
}