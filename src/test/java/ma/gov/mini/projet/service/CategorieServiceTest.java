package ma.gov.mini.projet.service;

import ma.gov.mini.projet.dto.CategorieDTO;
import ma.gov.mini.projet.entity.Categorie;
import ma.gov.mini.projet.exception.ResourceConflictException;
import ma.gov.mini.projet.mapper.CategorieMapper;
import ma.gov.mini.projet.repository.CategorieRepository;
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
class CategorieServiceTest {

    @Mock
    private CategorieRepository categorieRepository;

    @Mock
    private CategorieMapper categorieMapper;

    @Mock
    private LivreRepository livreRepository;

    @InjectMocks
    private CategorieService categorieService;

    @Test
    void findAll_shouldReturnListOfDTOs() {
        Categorie entity = new Categorie();
        CategorieDTO dto = new CategorieDTO();
        when(categorieRepository.findAll()).thenReturn(List.of(entity));
        when(categorieMapper.toDto(entity)).thenReturn(dto);

        List<CategorieDTO> result = categorieService.findAll();

        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
    }

    @Test
    void findById_shouldReturnDTO_whenFound() {
        Categorie entity = new Categorie();
        CategorieDTO dto = new CategorieDTO();
        when(categorieRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(categorieMapper.toDto(entity)).thenReturn(dto);

        CategorieDTO result = categorieService.findById(1L);

        assertEquals(dto, result);
    }

    @Test
    void findById_shouldThrowException_whenNotFound() {
        when(categorieRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> categorieService.findById(1L));
    }

    @Test
    void save_shouldPersistAndReturnDTO() {
        CategorieDTO in = new CategorieDTO();
        Categorie entity = new Categorie();
        Categorie saved = new Categorie();
        CategorieDTO out = new CategorieDTO();
        when(categorieMapper.toEntity(in)).thenReturn(entity);
        when(categorieRepository.save(entity)).thenReturn(saved);
        when(categorieMapper.toDto(saved)).thenReturn(out);

        CategorieDTO result = categorieService.save(in);

        assertEquals(out, result);
        verify(categorieRepository).save(any(Categorie.class));
    }

    @Test
    void update_shouldModifyAndReturnDTO() {
        CategorieDTO dto = new CategorieDTO();
        dto.setNom("Science-Fiction");
        Categorie entity = new Categorie();
        Categorie saved = new Categorie();
        CategorieDTO out = new CategorieDTO();
        when(categorieRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(categorieRepository.save(entity)).thenReturn(saved);
        when(categorieMapper.toDto(saved)).thenReturn(out);

        CategorieDTO result = categorieService.update(1L, dto);

        assertEquals(out, result);
        assertEquals("Science-Fiction", entity.getNom());
    }

    @Test
    void deleteById_shouldCallRepository() {
        when(categorieRepository.existsById(1L)).thenReturn(true);
        when(livreRepository.existsByCategorieId(1L)).thenReturn(false);

        categorieService.deleteById(1L);

        verify(categorieRepository).deleteById(1L);
    }

    @Test
    void deleteById_shouldThrowConflict_whenLivresReferenceCategorie() {
        when(categorieRepository.existsById(1L)).thenReturn(true);
        when(livreRepository.existsByCategorieId(1L)).thenReturn(true);

        assertThrows(ResourceConflictException.class, () -> categorieService.deleteById(1L));
    }
}
