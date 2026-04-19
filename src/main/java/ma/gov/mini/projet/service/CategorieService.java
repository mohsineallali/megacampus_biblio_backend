package ma.gov.mini.projet.service;

import ma.gov.mini.projet.dto.CategorieDTO;
import ma.gov.mini.projet.entity.Categorie;
import ma.gov.mini.projet.exception.ResourceConflictException;
import ma.gov.mini.projet.exception.ResourceNotFoundException;
import ma.gov.mini.projet.mapper.CategorieMapper;
import ma.gov.mini.projet.repository.CategorieRepository;
import ma.gov.mini.projet.repository.LivreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategorieService {

    @Autowired
    private CategorieRepository categorieRepository;

    @Autowired
    private CategorieMapper categorieMapper;

    @Autowired
    private LivreRepository livreRepository;

    public List<CategorieDTO> findAll() {
        return categorieRepository.findAll().stream()
                .map(categorieMapper::toDto)
                .collect(Collectors.toList());
    }

    public CategorieDTO findById(Long id) {
        return categorieMapper.toDto(getExistingById(id));
    }

    public Categorie getExistingById(Long id) {
        return categorieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Catégorie introuvable pour l'identifiant : " + id));
    }

    public CategorieDTO save(CategorieDTO dto) {
        Categorie entity = categorieMapper.toEntity(dto);
        entity.setId(null);
        return categorieMapper.toDto(categorieRepository.save(entity));
    }

    public CategorieDTO update(Long id, CategorieDTO dto) {
        Categorie entity = getExistingById(id);
        entity.setNom(dto.getNom());
        return categorieMapper.toDto(categorieRepository.save(entity));
    }

    public void deleteById(Long id) {
        if (!categorieRepository.existsById(id)) {
            throw new ResourceNotFoundException("Catégorie introuvable pour l'identifiant : " + id);
        }
        if (livreRepository.existsByCategorieId(id)) {
            throw new ResourceConflictException(
                    "Impossible de supprimer cette categorie: des livres y sont encore rattaches.");
        }
        categorieRepository.deleteById(id);
    }
}
