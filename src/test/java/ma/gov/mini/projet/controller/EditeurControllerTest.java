package ma.gov.mini.projet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ma.gov.mini.projet.config.RestExceptionHandler;
import ma.gov.mini.projet.dto.EditeurDTO;
import ma.gov.mini.projet.exception.ResourceConflictException;
import ma.gov.mini.projet.exception.ResourceNotFoundException;
import ma.gov.mini.projet.service.EditeurService;
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

@WebMvcTest(EditeurController.class)
@Import(RestExceptionHandler.class)
class EditeurControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EditeurService editeurService;

    @Test
    void getAll_shouldReturnOk() throws Exception {
        EditeurDTO dto = new EditeurDTO();
        dto.setId(1L);
        dto.setNom("Gallimard");
        when(editeurService.findAll()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/editeurs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nom").value("Gallimard"));
    }

    @Test
    void getById_shouldReturnOk_whenFound() throws Exception {
        EditeurDTO dto = new EditeurDTO();
        dto.setId(1L);
        dto.setNom("Gallimard");
        when(editeurService.findById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/editeurs/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Gallimard"));
    }

    @Test
    void getById_shouldReturn404_whenNotFound() throws Exception {
        when(editeurService.findById(99L)).thenThrow(new ResourceNotFoundException("introuvable"));

        mockMvc.perform(get("/api/editeurs/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_shouldReturn201() throws Exception {
        EditeurDTO dto = new EditeurDTO();
        dto.setNom("Hachette");
        dto.setAdresse("Casablanca");
        EditeurDTO saved = new EditeurDTO();
        saved.setId(1L);
        when(editeurService.save(any(EditeurDTO.class))).thenReturn(saved);

        mockMvc.perform(post("/api/editeurs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void update_shouldReturnOk() throws Exception {
        EditeurDTO dto = new EditeurDTO();
        dto.setNom("Hachette");
        dto.setAdresse("Casablanca");
        EditeurDTO updated = new EditeurDTO();
        updated.setId(1L);
        when(editeurService.update(eq(1L), any(EditeurDTO.class))).thenReturn(updated);

        mockMvc.perform(put("/api/editeurs/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void update_shouldReturn404_whenNotFound() throws Exception {
        EditeurDTO dto = new EditeurDTO();
        when(editeurService.update(eq(99L), any(EditeurDTO.class)))
                .thenThrow(new ResourceNotFoundException("introuvable"));

        mockMvc.perform(put("/api/editeurs/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_shouldReturn204() throws Exception {
        mockMvc.perform(delete("/api/editeurs/1"))
                .andExpect(status().isNoContent());

        verify(editeurService).deleteById(1L);
    }

    @Test
    void delete_shouldReturn404_whenNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("introuvable")).when(editeurService).deleteById(99L);

        mockMvc.perform(delete("/api/editeurs/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_shouldReturn409_whenReferencedByLivres() throws Exception {
        doThrow(new ResourceConflictException("Impossible de supprimer cet editeur."))
                .when(editeurService).deleteById(2L);

        mockMvc.perform(delete("/api/editeurs/2"))
                .andExpect(status().isConflict());
    }
}
