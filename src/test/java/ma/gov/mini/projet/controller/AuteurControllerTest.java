package ma.gov.mini.projet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ma.gov.mini.projet.config.RestExceptionHandler;
import ma.gov.mini.projet.dto.AuteurDTO;
import ma.gov.mini.projet.exception.ResourceConflictException;
import ma.gov.mini.projet.exception.ResourceNotFoundException;
import ma.gov.mini.projet.service.AuteurService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuteurController.class)
@Import(RestExceptionHandler.class)
class AuteurControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuteurService auteurService;

    @Test
    void getAll_shouldReturnOk() throws Exception {
        AuteurDTO dto = new AuteurDTO();
        dto.setId(1L);
        dto.setNom("Hugo");
        dto.setPrenom("Victor");
        when(auteurService.findAll()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/auteurs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nom").value("Hugo"));
    }

    @Test
    void getById_shouldReturnOk_whenFound() throws Exception {
        AuteurDTO dto = new AuteurDTO();
        dto.setId(1L);
        dto.setNom("Hugo");
        when(auteurService.findById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/auteurs/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Hugo"));
    }

    @Test
    void getById_shouldReturn404_whenNotFound() throws Exception {
        when(auteurService.findById(99L)).thenThrow(new ResourceNotFoundException("introuvable"));

        mockMvc.perform(get("/api/auteurs/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_shouldReturn201() throws Exception {
        AuteurDTO dto = new AuteurDTO();
        dto.setNom("Hugo");
        dto.setPrenom("Victor");
        AuteurDTO saved = new AuteurDTO();
        saved.setId(1L);
        saved.setNom("Hugo");
        when(auteurService.save(any(AuteurDTO.class))).thenReturn(saved);

        mockMvc.perform(post("/api/auteurs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void update_shouldReturnOk() throws Exception {
        AuteurDTO dto = new AuteurDTO();
        dto.setNom("Hugo");
        dto.setPrenom("Victor");
        AuteurDTO updated = new AuteurDTO();
        updated.setId(1L);
        when(auteurService.update(eq(1L), any(AuteurDTO.class))).thenReturn(updated);

        mockMvc.perform(put("/api/auteurs/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void update_shouldReturn404_whenNotFound() throws Exception {
        AuteurDTO dto = new AuteurDTO();
        when(auteurService.update(eq(99L), any(AuteurDTO.class)))
                .thenThrow(new ResourceNotFoundException("introuvable"));

        mockMvc.perform(put("/api/auteurs/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_shouldReturn204() throws Exception {
        mockMvc.perform(delete("/api/auteurs/1"))
                .andExpect(status().isNoContent());

        verify(auteurService).deleteById(1L);
    }

    @Test
    void delete_shouldReturn404_whenNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("introuvable")).when(auteurService).deleteById(99L);

        mockMvc.perform(delete("/api/auteurs/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_shouldReturn409_whenReferencedByLivres() throws Exception {
        doThrow(new ResourceConflictException("Impossible de supprimer cet auteur."))
                .when(auteurService).deleteById(2L);

        mockMvc.perform(delete("/api/auteurs/2"))
                .andExpect(status().isConflict());
    }
}
