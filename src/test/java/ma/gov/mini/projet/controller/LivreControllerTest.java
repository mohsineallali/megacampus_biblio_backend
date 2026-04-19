package ma.gov.mini.projet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ma.gov.mini.projet.config.RestExceptionHandler;
import ma.gov.mini.projet.dto.LivreDTO;
import ma.gov.mini.projet.exception.ResourceNotFoundException;
import ma.gov.mini.projet.service.LivreService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
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

@WebMvcTest(LivreController.class)
@Import(RestExceptionHandler.class)
class LivreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LivreService livreService;

    @Test
    void getAll_shouldReturnOk() throws Exception {
        LivreDTO dto = new LivreDTO();
        dto.setId(1L);
        dto.setTitre("Germinal");
        when(livreService.findAll()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/livres"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titre").value("Germinal"));
    }

    @Test
    void getById_shouldReturnOk_whenFound() throws Exception {
        LivreDTO dto = new LivreDTO();
        dto.setId(1L);
        dto.setTitre("Germinal");
        when(livreService.findById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/livres/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titre").value("Germinal"));
    }

    @Test
    void getById_shouldReturn404_whenNotFound() throws Exception {
        when(livreService.findById(99L)).thenThrow(new ResourceNotFoundException("introuvable"));

        mockMvc.perform(get("/api/livres/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_shouldReturn201() throws Exception {
        LivreDTO dto = new LivreDTO();
        dto.setTitre("Germinal");
        dto.setPages(591);
        dto.setDatePublication(LocalDate.of(1885, 11, 25));
        dto.setAuteurId(1L);
        dto.setCategorieId(1L);
        dto.setEditeurId(1L);
        LivreDTO saved = new LivreDTO();
        saved.setId(1L);
        when(livreService.save(any(LivreDTO.class))).thenReturn(saved);

        mockMvc.perform(post("/api/livres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void update_shouldReturnOk() throws Exception {
        LivreDTO dto = new LivreDTO();
        dto.setTitre("Germinal");
        dto.setPages(591);
        dto.setDatePublication(LocalDate.of(1885, 11, 25));
        dto.setAuteurId(1L);
        dto.setCategorieId(1L);
        dto.setEditeurId(1L);
        LivreDTO updated = new LivreDTO();
        updated.setId(1L);
        when(livreService.update(eq(1L), any(LivreDTO.class))).thenReturn(updated);

        mockMvc.perform(put("/api/livres/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void update_shouldReturn404_whenNotFound() throws Exception {
        LivreDTO dto = new LivreDTO();
        when(livreService.update(eq(99L), any(LivreDTO.class)))
                .thenThrow(new ResourceNotFoundException("introuvable"));

        mockMvc.perform(put("/api/livres/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_shouldReturn204() throws Exception {
        mockMvc.perform(delete("/api/livres/1"))
                .andExpect(status().isNoContent());

        verify(livreService).deleteById(1L);
    }

    @Test
    void delete_shouldReturn404_whenNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("introuvable")).when(livreService).deleteById(99L);

        mockMvc.perform(delete("/api/livres/99"))
                .andExpect(status().isNotFound());
    }
}
