package ma.gov.mini.projet.mapper;

import ma.gov.mini.projet.dto.LivreDTO;
import ma.gov.mini.projet.entity.Livre;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LivreMapper {

    @Mapping(source = "auteur.id", target = "auteurId")
    @Mapping(source = "categorie.id", target = "categorieId")
    @Mapping(source = "editeur.id", target = "editeurId")
    @Mapping(source = "auteur.nom", target = "auteurNom")
    @Mapping(source = "categorie.nom", target = "categorieNom")
    @Mapping(source = "editeur.nom", target = "editeurNom")
    LivreDTO toDto(Livre entity);

    @Mapping(target = "auteur", ignore = true)
    @Mapping(target = "categorie", ignore = true)
    @Mapping(target = "editeur", ignore = true)
    Livre toEntity(LivreDTO dto);
}
