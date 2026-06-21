package com.gymapp.ms_rutinas.service;

import com.gymapp.ms_rutinas.assembler.RutinaAssembler;
import com.gymapp.ms_rutinas.client.GamificacionClient;
import com.gymapp.ms_rutinas.client.MiembroClient;
import com.gymapp.ms_rutinas.client.NotificacionClient;
import com.gymapp.ms_rutinas.dto.RutinaRequestDTO;
import com.gymapp.ms_rutinas.dto.RutinaResponseDTO;
import com.gymapp.ms_rutinas.exception.BusinessException;
import com.gymapp.ms_rutinas.model.Rutina;
import com.gymapp.ms_rutinas.repository.RutinaRepository;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RutinaServiceImplTest {

    @Mock
    private RutinaRepository repository;
    @Mock
    private MiembroClient miembroClient;
    @Mock
    private GamificacionClient gamificacionClient;
    @Mock
    private NotificacionClient notificacionClient;
    @Mock
    private RutinaAssembler assembler;

    @InjectMocks
    private RutinaServiceImpl rutinaService;

    @Test
    void crear_MiembroValido_RetornaRutinaDTO() {

        RutinaRequestDTO request = new RutinaRequestDTO(1L, 2L, "Fuerza 5x5", "INTERMEDIO", LocalDate.now(), 8, "Sentadillas 5x5");
        Rutina entity = new Rutina(null, 1L, 2L, "Fuerza 5x5", "INTERMEDIO", LocalDate.now(), 8, "Sentadillas 5x5", true);
        Rutina guardada = new Rutina(1L, 1L, 2L, "Fuerza 5x5", "INTERMEDIO", LocalDate.now(), 8, "Sentadillas 5x5", true);
        RutinaResponseDTO response = RutinaResponseDTO.builder().id(1L).nombre("Fuerza 5x5").build();


        when(miembroClient.obtenerPorId(1L)).thenReturn(new Object());
        when(assembler.toEntity(request)).thenReturn(entity);
        when(repository.save(any(Rutina.class))).thenReturn(guardada);
        when(assembler.toResponseDTO(guardada)).thenReturn(response);


        RutinaResponseDTO resultado = rutinaService.crear(request);


        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Fuerza 5x5", resultado.getNombre());


        verify(gamificacionClient, times(1)).enviarEvento(anyMap());
        verify(notificacionClient, times(1)).enviarNotificacion(anyMap());
    }

    @Test
    void crear_MiembroInvalido_LanzaBusinessException() {

        RutinaRequestDTO request = new RutinaRequestDTO(99L, 2L, "Error", "PRINCIPIANTE", LocalDate.now(), 4, "Detalle");


        FeignException.NotFound notFoundException = mock(FeignException.NotFound.class);
        when(miembroClient.obtenerPorId(99L)).thenThrow(notFoundException);


        BusinessException exception = assertThrows(BusinessException.class, () -> rutinaService.crear(request));
        assertEquals("Validación fallida: El miembro asignado no existe.", exception.getMessage());
        verify(repository, never()).save(any());
    }
}