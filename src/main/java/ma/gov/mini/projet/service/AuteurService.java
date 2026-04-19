package ma.gov.mini.projet.service;

import ma.gov.mini.projet.dto.AuteurDTO;
import ma.gov.mini.projet.entity.Auteur;
import ma.gov.mini.projet.exception.ResourceConflictException;
import ma.gov.mini.projet.exception.ResourceNotFoundException;
import ma.gov.mini.projet.mapper.AuteurMapper;
import ma.gov.mini.projet.repository.AuteurRepository;
import ma.gov.mini.projet.repository.LivreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuteurService {

    @Autowired
    private AuteurRepository auteurRepository;

    @Autowired
    private AuteurMapper auteurMapper;

    @Autowired
    private LivreRepository livreRepository;

    public List<AuteurDTO> findAll() {
        return auteurRepository.findAll().stream()
                .map(auteurMapper::toDto)
                .collect(Collectors.toList());
    }

    public AuteurDTO findById(Long id) {
        return auteurMapper.toDto(getExistingById(id));
    }

    public Auteur getExistingById(Long id) {
        return auteurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Auteur introuvable pour l'identifiant : " + id));
    }

    public AuteurDTO save(AuteurDTO dto) {
        Auteur entity = auteurMapper.toEntity(dto);
        entity.setId(null);
        return auteurMapper.toDto(auteurRepository.save(entity));
    }

    public AuteurDTO update(Long id, AuteurDTO dto) {
        Auteur entity = getExistingById(id);
        entity.setNom(dto.getNom());
        entity.setPrenom(dto.getPrenom());
        return auteurMapper.toDto(auteurRepository.save(entity));
    }

    public void deleteById(Long id) {
        if (!auteurRepository.existsById(id)) {
            throw new ResourceNotFoundException("Auteur introuvable pour l'identifiant : " + id);
        }
        if (livreRepository.existsByAuteurId(id)) {
            throw new ResourceConflictException(
                    "Impossible de supprimer cet auteur: des livres y sont encore rattaches.");
        }
        auteurRepository.deleteById(id);
    }
}
