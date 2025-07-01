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
import org.srd.ediary.application.dto.*;
import org.srd.ediary.application.exception.DiaryNotFoundException;
import org.srd.ediary.application.exception.EntryNotFoundException;
import org.srd.ediary.application.security.jwt.JwtFilter;
import org.srd.ediary.application.service.EntryService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EntryController.class)
class EntryControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JwtFilter jwtFilter;
    @MockBean
    private EntryService entryService;
    private JacksonTester<EntryCreateDTO> creationJson;
    private JacksonTester<EntryUpdateDTO> updateJson;

    private final EntryInfoDTO output = new EntryInfoDTO(1L, "day1", "Good day", LocalDate.now());
    private final List<EntryInfoDTO> outputList = List.of(output);

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.findAndRegisterModules();
        JacksonTester.initFields(this, objectMapper);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new EntryController(entryService))
                .build();
    }

    @Test
    void testGetEntry_ExistingEntry() throws Exception{
        Long entryId = 1L;
        when(entryService.getEntry(entryId)).thenReturn(output);

        mockMvc.perform(get("/api/v1/entries/" + entryId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(String.valueOf(entryId))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("day1"));
    }

    @Test
    void testGetEntry_NonExistingEntry() throws Exception{
        Long entryId = 1L;
        when(entryService.getEntry(entryId)).thenThrow(EntryNotFoundException.class);

        mockMvc.perform(get("/api/v1/entries/" + entryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(entryId))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetEntriesByDiary_ExistingDiary() throws Exception{
        Long diaryId = 3L;
        when(entryService.getAllEntriesByDiary(diaryId)).thenReturn(outputList);

        mockMvc.perform(get("/api/v1/diaries/" + diaryId + "/entries")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(String.valueOf(diaryId))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("day1"));
    }

    @Test
    void testGetEntriesByDiary_NonExistingDiary() throws Exception{
        Long diaryId = 3L;
        when(entryService.getAllEntriesByDiary(diaryId)).thenThrow(DiaryNotFoundException.class);

        mockMvc.perform(get("/api/v1/diaries/" + diaryId + "/entries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(diaryId))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateEntry_WithExistingDiary() throws Exception{
        EntryCreateDTO input = new EntryCreateDTO(1L, "day1", "Good day");
        when(entryService.create(input)).thenReturn(output);

        mockMvc.perform(post("/api/v1/entries/")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .accept(MediaType.APPLICATION_JSON)
                .content(creationJson.write(input).getJson())
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("day1"));
    }

    @Test
    void testCreateEntry_WithNonExistingDiary() throws Exception{
        EntryCreateDTO input = new EntryCreateDTO(1L, "day1", "Good day");
        when(entryService.create(input)).thenThrow(EntryNotFoundException.class);

        mockMvc.perform(post("/api/v1/entries/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(creationJson.write(input).getJson())
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateEntry_ExistingEntry() throws Exception {
        Long entryId = 1L;
        EntryUpdateDTO input = new EntryUpdateDTO("day1", "Good day");
        when(entryService.update(entryId, input)).thenReturn(output);

        mockMvc.perform(put("/api/v1/entries/" + entryId)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .accept(MediaType.APPLICATION_JSON)
                .content(updateJson.write(input).getJson())
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("day1"));
    }

    @Test
    void testUpdateEntry_NonExistingEntry() throws Exception {
        Long entryId = 1L;
        EntryUpdateDTO input = new EntryUpdateDTO("day1", "Good day");
        when(entryService.update(entryId, input)).thenThrow(EntryNotFoundException.class);

        mockMvc.perform(put("/api/v1/entries/" + entryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(updateJson.write(input).getJson())
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteEntry() throws Exception {
        Long entryId = 1L;
        doNothing().when(entryService).delete(entryId);

        mockMvc.perform(delete("/api/v1/entries/" + entryId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(String.valueOf(entryId))
        )
                .andExpect(status().isOk());
    }
}