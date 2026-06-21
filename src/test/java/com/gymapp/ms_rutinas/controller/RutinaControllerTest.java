package com.gymapp.ms_rutinas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gymapp.ms_rutinas.dto.RutinaRequestDTO;
import com.gymapp.ms_rutinas.dto.RutinaResponseDTO;
import com.gymapp.ms_rutinas.service.RutinaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class RutinaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RutinaService rutinaService;

    @InjectMocks
    private RutinaController rutinaController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(rutinaController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void obtenerTodas_RetornaListaConEstatus200() throws Exception {

        RutinaResponseDTO rutina = RutinaResponseDTO.builder().id(1L).nombre("Rutina Base").build();
        when(rutinaService.listarTodas()).thenReturn(List.of(rutina));


        mockMvc.perform(get("/api/rutinas")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Rutina Base"));
    }

    @Test
    void crear_PeticionValida_Retorna201() throws Exception {

        RutinaRequestDTO request = new RutinaRequestDTO(1L, 2L, "Hipertrofia", "AVANZADO", LocalDate.now(), 12, "Detalle 1");
        RutinaResponseDTO response = RutinaResponseDTO.builder().id(5L).nombre("Hipertrofia").build();

        when(rutinaService.crear(any(RutinaRequestDTO.class))).thenReturn(response);


        mockMvc.perform(post("/api/rutinas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.nombre").value("Hipertrofia"));
    }

    @Test
    void crear_DuracionInvalida_Retorna400BadRequest() throws Exception {

        RutinaRequestDTO request = new RutinaRequestDTO(1L, 2L, "Error", "PRINCIPIANTE", LocalDate.now(), 0, "Detalle");


        mockMvc.perform(post("/api/rutinas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}