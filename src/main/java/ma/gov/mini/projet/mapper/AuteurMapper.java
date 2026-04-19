package ma.gov.mini.projet.mapper;

import ma.gov.mini.projet.dto.AuteurDTO;
import ma.gov.mini.projet.entity.Auteur;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuteurMapper {

    AuteurDTO toDto(Auteur entity);

    Auteur toEntity(AuteurDTO dto);
}
