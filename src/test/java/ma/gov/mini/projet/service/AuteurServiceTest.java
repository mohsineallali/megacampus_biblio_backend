package ma.gov.mini.projet.service;

import ma.gov.mini.projet.dto.AuteurDTO;
import ma.gov.mini.projet.entity.Auteur;
import ma.gov.mini.projet.exception.ResourceConflictException;
import ma.gov.mini.projet.mapper.AuteurMapper;
import ma.gov.mini.projet.repository.AuteurRepository;
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
class AuteurServiceTest {

    @Mock
    private AuteurRepository auteurRepository;

    @Mock
    private AuteurMapper auteurMapper;

    @Mock
    private LivreRepository livreRepository;

    @InjectMocks
    private AuteurService auteurService;

    @Test
    void findAll_shouldReturnListOfDTOs() {
        Auteur entity = new Auteur();
        AuteurDTO dto = new AuteurDTO();
        when(auteurRepository.findAll()).thenReturn(List.of(entity));
        when(auteurMapper.toDto(entity)).thenReturn(dto);

        List<AuteurDTO> result = auteurService.findAll();

        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
    }

    @Test
    void findById_shouldReturnDTO_whenFound() {
        Auteur entity = new Auteur();
        AuteurDTO dto = new AuteurDTO();
        when(auteurRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(auteurMapper.toDto(entity)).thenReturn(dto);

        AuteurDTO result = auteurService.findById(1L);

        assertEquals(dto, result);
    }

    @Test
    void findById_shouldThrowException_whenNotFound() {
        when(auteurRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> auteurService.findById(1L));
    }

    @Test
    void save_shouldPersistAndReturnDTO() {
        AuteurDTO in = new AuteurDTO();
        Auteur entity = new Auteur();
        Auteur saved = new Auteur();
        AuteurDTO out = new AuteurDTO();
        when(auteurMapper.toEntity(in)).thenReturn(entity);
        when(auteurRepository.save(entity)).thenReturn(saved);
        when(auteurMapper.toDto(saved)).thenReturn(out);

        AuteurDTO result = auteurService.save(in);

        assertEquals(out, result);
        verify(auteurRepository).save(any(Auteur.class));
    }

    @Test
    void update_shouldModifyAndReturnDTO() {
        AuteurDTO dto = new AuteurDTO();
        dto.setNom("X");
        dto.setPrenom("Y");
        Auteur entity = new Auteur();
        Auteur saved = new Auteur();
        AuteurDTO out = new AuteurDTO();
        when(auteurRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(auteurRepository.save(entity)).thenReturn(saved);
        when(auteurMapper.toDto(saved)).thenReturn(out);

        AuteurDTO result = auteurService.update(1L, dto);

        assertEquals(out, result);
        assertEquals("X", entity.getNom());
        assertEquals("Y", entity.getPrenom());
    }

    @Test
    void deleteById_shouldCallRepository() {
        when(auteurRepository.existsById(1L)).thenReturn(true);
        when(livreRepository.existsByAuteurId(1L)).thenReturn(false);

        auteurService.deleteById(1L);

        verify(auteurRepository).deleteById(1L);
    }

    @Test
    void deleteById_shouldThrowConflict_whenLivresReferenceAuteur() {
        when(auteurRepository.existsById(1L)).thenReturn(true);
        when(livreRepository.existsByAuteurId(1L)).thenReturn(true);

        assertThrows(ResourceConflictException.class, () -> auteurService.deleteById(1L));
    }
}
