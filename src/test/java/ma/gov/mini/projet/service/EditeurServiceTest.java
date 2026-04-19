package ma.gov.mini.projet.service;

import ma.gov.mini.projet.dto.EditeurDTO;
import ma.gov.mini.projet.entity.Editeur;
import ma.gov.mini.projet.exception.ResourceConflictException;
import ma.gov.mini.projet.mapper.EditeurMapper;
import ma.gov.mini.projet.repository.EditeurRepository;
import ma.gov.mini.projet.repository.LivreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EditeurServiceTest {

    @Mock
    private EditeurRepository editeurRepository;

    @Mock
    private EditeurMapper editeurMapper;

    @Mock
    private LivreRepository livreRepository;

    @InjectMocks
    private EditeurService editeurService;

    @Test
    void findAll_shouldReturnListOfDTOs() {
        Editeur entity = new Editeur();
        EditeurDTO dto = new EditeurDTO();
        when(editeurRepository.findAll()).thenReturn(List.of(entity));
        when(editeurMapper.toDto(entity)).thenReturn(dto);

        List<EditeurDTO> result = editeurService.findAll();

        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
    }

    @Test
    void findById_shouldReturnDTO_whenFound() {
        Editeur entity = new Editeur();
        EditeurDTO dto = new EditeurDTO();
        when(editeurRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(editeurMapper.toDto(entity)).thenReturn(dto);

        EditeurDTO result = editeurService.findById(1L);

        assertEquals(dto, result);
    }

    @Test
    void findById_shouldThrowException_whenNotFound() {
        when(editeurRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> editeurService.findById(1L));
    }

    @Test
    void save_shouldPersistAndReturnDTO() {
        EditeurDTO in = new EditeurDTO();
        Editeur entity = new Editeur();
        Editeur saved = new Editeur();
        EditeurDTO out = new EditeurDTO();
        when(editeurMapper.toEntity(in)).thenReturn(entity);
        when(editeurRepository.save(entity)).thenReturn(saved);
        when(editeurMapper.toDto(saved)).thenReturn(out);

        EditeurDTO result = editeurService.save(in);

        assertEquals(out, result);
        verify(editeurRepository).save(any(Editeur.class));
    }

    @Test
    void update_shouldModifyAndReturnDTO() {
        EditeurDTO dto = new EditeurDTO();
        dto.setNom("Hachette");
        dto.setAdresse("Casablanca");
        Editeur entity = new Editeur();
        Editeur saved = new Editeur();
        EditeurDTO out = new EditeurDTO();
        when(editeurRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(editeurRepository.save(entity)).thenReturn(saved);
        when(editeurMapper.toDto(saved)).thenReturn(out);

        EditeurDTO result = editeurService.update(1L, dto);

        assertEquals(out, result);
        assertEquals("Hachette", entity.getNom());
        assertEquals("Casablanca", entity.getAdresse());
    }

    @Test
    void deleteById_shouldCallRepository() {
        when(editeurRepository.existsById(1L)).thenReturn(true);
        when(livreRepository.existsByEditeurId(1L)).thenReturn(false);

        editeurService.deleteById(1L);

        verify(editeurRepository).deleteById(1L);
    }

    @Test
    void deleteById_shouldThrowConflict_whenLivresReferenceEditeur() {
        when(editeurRepository.existsById(1L)).thenReturn(true);
        when(livreRepository.existsByEditeurId(1L)).thenReturn(true);

        assertThrows(ResourceConflictException.class, () -> editeurService.deleteById(1L));
    }
}
