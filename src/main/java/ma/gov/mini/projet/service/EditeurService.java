package ma.gov.mini.projet.service;

import ma.gov.mini.projet.dto.EditeurDTO;
import ma.gov.mini.projet.entity.Editeur;
import ma.gov.mini.projet.exception.ResourceConflictException;
import ma.gov.mini.projet.exception.ResourceNotFoundException;
import ma.gov.mini.projet.mapper.EditeurMapper;
import ma.gov.mini.projet.repository.EditeurRepository;
import ma.gov.mini.projet.repository.LivreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EditeurService {

    @Autowired
    private EditeurRepository editeurRepository;

    @Autowired
    private EditeurMapper editeurMapper;

    @Autowired
    private LivreRepository livreRepository;

    public List<EditeurDTO> findAll() {
        return editeurRepository.findAll().stream()
                .map(editeurMapper::toDto)
                .collect(Collectors.toList());
    }

    public EditeurDTO findById(Long id) {
        return editeurMapper.toDto(getExistingById(id));
    }

    public Editeur getExistingById(Long id) {
        return editeurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Éditeur introuvable pour l'identifiant : " + id));
    }

    public EditeurDTO save(EditeurDTO dto) {
        Editeur entity = editeurMapper.toEntity(dto);
        entity.setId(null);
        return editeurMapper.toDto(editeurRepository.save(entity));
    }

    public EditeurDTO update(Long id, EditeurDTO dto) {
        Editeur entity = getExistingById(id);
        entity.setNom(dto.getNom());
        entity.setAdresse(dto.getAdresse());
        return editeurMapper.toDto(editeurRepository.save(entity));
    }

    public void deleteById(Long id) {
        if (!editeurRepository.existsById(id)) {
            throw new ResourceNotFoundException("Éditeur introuvable pour l'identifiant : " + id);
        }
        if (livreRepository.existsByEditeurId(id)) {
            throw new ResourceConflictException(
                    "Impossible de supprimer cet editeur: des livres y sont encore rattaches.");
        }
        editeurRepository.deleteById(id);
    }
}
