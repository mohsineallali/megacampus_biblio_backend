package ma.gov.mini.projet.service;

import ma.gov.mini.projet.dto.LivreDTO;
import ma.gov.mini.projet.entity.Auteur;
import ma.gov.mini.projet.entity.Categorie;
import ma.gov.mini.projet.entity.Editeur;
import ma.gov.mini.projet.entity.Livre;
import ma.gov.mini.projet.mapper.LivreMapper;
import ma.gov.mini.projet.repository.LivreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LivreServiceTest {

    @Mock
    private LivreRepository livreRepository;

    @Mock
    private AuteurService auteurService;

    @Mock
    private CategorieService categorieService;

    @Mock
    private EditeurService editeurService;

    @Mock
    private LivreMapper livreMapper;

    @InjectMocks
    private LivreService livreService;

    @Test
    void findAll_shouldReturnListOfDTOs() {
        Livre entity = new Livre();
        LivreDTO dto = new LivreDTO();
        when(livreRepository.findAll()).thenReturn(List.of(entity));
        when(livreMapper.toDto(entity)).thenReturn(dto);

        List<LivreDTO> result = livreService.findAll();

        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
    }

    @Test
    void findById_shouldReturnDTO_whenFound() {
        Livre entity = new Livre();
        LivreDTO dto = new LivreDTO();
        when(livreRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(livreMapper.toDto(entity)).thenReturn(dto);

        LivreDTO result = livreService.findById(1L);

        assertEquals(dto, result);
    }

    @Test
    void findById_shouldThrowException_whenNotFound() {
        when(livreRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> livreService.findById(1L));
    }

    @Test
    void save_shouldPersistAndReturnDTO() {
        LivreDTO in = new LivreDTO();
        in.setAuteurId(1L);
        in.setCategorieId(2L);
        in.setEditeurId(3L);
        Livre entity = new Livre();
        Livre saved = new Livre();
        LivreDTO out = new LivreDTO();
        Auteur auteur = new Auteur();
        Categorie categorie = new Categorie();
        Editeur editeur = new Editeur();

        when(livreMapper.toEntity(in)).thenReturn(entity);
        when(auteurService.getExistingById(1L)).thenReturn(auteur);
        when(categorieService.getExistingById(2L)).thenReturn(categorie);
        when(editeurService.getExistingById(3L)).thenReturn(editeur);
        when(livreRepository.save(entity)).thenReturn(saved);
        when(livreMapper.toDto(saved)).thenReturn(out);

        LivreDTO result = livreService.save(in);

        assertEquals(out, result);
        assertEquals(auteur, entity.getAuteur());
        assertEquals(categorie, entity.getCategorie());
        assertEquals(editeur, entity.getEditeur());
        verify(livreRepository).save(any(Livre.class));
    }

    @Test
    void update_shouldModifyAndReturnDTO() {
        LivreDTO dto = new LivreDTO();
        dto.setTitre("Germinal");
        dto.setPages(591);
        dto.setDatePublication(LocalDate.of(1885, 11, 25));
        dto.setAuteurId(1L);
        dto.setCategorieId(2L);
        dto.setEditeurId(3L);

        Livre entity = new Livre();
        Livre saved = new Livre();
        LivreDTO out = new LivreDTO();
        Auteur auteur = new Auteur();
        Categorie categorie = new Categorie();
        Editeur editeur = new Editeur();

        when(livreRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(auteurService.getExistingById(1L)).thenReturn(auteur);
        when(categorieService.getExistingById(2L)).thenReturn(categorie);
        when(editeurService.getExistingById(3L)).thenReturn(editeur);
        when(livreRepository.save(entity)).thenReturn(saved);
        when(livreMapper.toDto(saved)).thenReturn(out);

        LivreDTO result = livreService.update(1L, dto);

        assertEquals(out, result);
        assertEquals("Germinal", entity.getTitre());
        assertEquals(591, entity.getPages());
        assertEquals(LocalDate.of(1885, 11, 25), entity.getDatePublication());
    }

    @Test
    void deleteById_shouldCallRepository() {
        when(livreRepository.existsById(1L)).thenReturn(true);

        livreService.deleteById(1L);

        verify(livreRepository).deleteById(1L);
    }
}
