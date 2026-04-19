package ma.gov.mini.projet.service;

import ma.gov.mini.projet.dto.LivreDTO;
import ma.gov.mini.projet.entity.Livre;
import ma.gov.mini.projet.exception.ResourceNotFoundException;
import ma.gov.mini.projet.mapper.LivreMapper;
import ma.gov.mini.projet.repository.LivreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LivreService {

    @Autowired
    private LivreRepository livreRepository;

    @Autowired
    private AuteurService auteurService;

    @Autowired
    private CategorieService categorieService;

    @Autowired
    private EditeurService editeurService;

    @Autowired
    private LivreMapper livreMapper;

    @Transactional(readOnly = true)
    public List<LivreDTO> findAll() {
        return livreRepository.findAll().stream()
                .map(livreMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LivreDTO findById(Long id) {
        Livre livre = livreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livre introuvable pour l'identifiant : " + id));
        return livreMapper.toDto(livre);
    }

    public LivreDTO save(LivreDTO dto) {
        Livre entity = livreMapper.toEntity(dto);
        entity.setId(null);
        entity.setAuteur(auteurService.getExistingById(dto.getAuteurId()));
        entity.setCategorie(categorieService.getExistingById(dto.getCategorieId()));
        entity.setEditeur(editeurService.getExistingById(dto.getEditeurId()));
        return livreMapper.toDto(livreRepository.save(entity));
    }

    public LivreDTO update(Long id, LivreDTO dto) {
        Livre entity = livreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livre introuvable pour l'identifiant : " + id));
        entity.setTitre(dto.getTitre());
        entity.setPages(dto.getPages());
        entity.setDatePublication(dto.getDatePublication());
        entity.setAuteur(auteurService.getExistingById(dto.getAuteurId()));
        entity.setCategorie(categorieService.getExistingById(dto.getCategorieId()));
        entity.setEditeur(editeurService.getExistingById(dto.getEditeurId()));
        return livreMapper.toDto(livreRepository.save(entity));
    }

    public void deleteById(Long id) {
        if (!livreRepository.existsById(id)) {
            throw new ResourceNotFoundException("Livre introuvable pour l'identifiant : " + id);
        }
        livreRepository.deleteById(id);
    }
}
