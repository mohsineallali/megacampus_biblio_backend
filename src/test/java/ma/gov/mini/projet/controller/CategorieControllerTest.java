package ma.gov.mini.projet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ma.gov.mini.projet.config.RestExceptionHandler;
import ma.gov.mini.projet.dto.CategorieDTO;
import ma.gov.mini.projet.exception.ResourceConflictException;
import ma.gov.mini.projet.exception.ResourceNotFoundException;
import ma.gov.mini.projet.service.CategorieService;
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

@WebMvcTest(CategorieController.class)
@Import(RestExceptionHandler.class)
class CategorieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategorieService categorieService;

    @Test
    void getAll_shouldReturnOk() throws Exception {
        CategorieDTO dto = new CategorieDTO();
        dto.setId(1L);
        dto.setNom("Roman");
        when(categorieService.findAll()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nom").value("Roman"));
    }

    @Test
    void getById_shouldReturnOk_whenFound() throws Exception {
        CategorieDTO dto = new CategorieDTO();
        dto.setId(1L);
        dto.setNom("Roman");
        when(categorieService.findById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Roman"));
    }

    @Test
    void getById_shouldReturn404_whenNotFound() throws Exception {
        when(categorieService.findById(99L)).thenThrow(new ResourceNotFoundException("introuvable"));

        mockMvc.perform(get("/api/categories/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_shouldReturn201() throws Exception {
        CategorieDTO dto = new CategorieDTO();
        dto.setNom("Science-Fiction");
        CategorieDTO saved = new CategorieDTO();
        saved.setId(1L);
        when(categorieService.save(any(CategorieDTO.class))).thenReturn(saved);

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void update_shouldReturnOk() throws Exception {
        CategorieDTO dto = new CategorieDTO();
        dto.setNom("Science-Fiction");
        CategorieDTO updated = new CategorieDTO();
        updated.setId(1L);
        when(categorieService.update(eq(1L), any(CategorieDTO.class))).thenReturn(updated);

        mockMvc.perform(put("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void update_shouldReturn404_whenNotFound() throws Exception {
        CategorieDTO dto = new CategorieDTO();
        when(categorieService.update(eq(99L), any(CategorieDTO.class)))
                .thenThrow(new ResourceNotFoundException("introuvable"));

        mockMvc.perform(put("/api/categories/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_shouldReturn204() throws Exception {
        mockMvc.perform(delete("/api/categories/1"))
                .andExpect(status().isNoContent());

        verify(categorieService).deleteById(1L);
    }

    @Test
    void delete_shouldReturn404_whenNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("introuvable")).when(categorieService).deleteById(99L);

        mockMvc.perform(delete("/api/categories/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_shouldReturn409_whenReferencedByLivres() throws Exception {
        doThrow(new ResourceConflictException("Impossible de supprimer cette categorie."))
                .when(categorieService).deleteById(2L);

        mockMvc.perform(delete("/api/categories/2"))
                .andExpect(status().isConflict());
    }
}
