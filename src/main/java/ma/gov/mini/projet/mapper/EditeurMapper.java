package ma.gov.mini.projet.mapper;

import ma.gov.mini.projet.dto.EditeurDTO;
import ma.gov.mini.projet.entity.Editeur;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EditeurMapper {

    EditeurDTO toDto(Editeur entity);

    Editeur toEntity(EditeurDTO dto);
}
