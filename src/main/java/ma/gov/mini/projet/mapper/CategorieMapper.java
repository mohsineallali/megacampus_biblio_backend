package ma.gov.mini.projet.mapper;

import ma.gov.mini.projet.dto.CategorieDTO;
import ma.gov.mini.projet.entity.Categorie;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategorieMapper {

    CategorieDTO toDto(Categorie entity);

    Categorie toEntity(CategorieDTO dto);
}
