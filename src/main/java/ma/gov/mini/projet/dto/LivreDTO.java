package ma.gov.mini.projet.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class LivreDTO {

    private Long id;
    private String titre;
    private int pages;
    private LocalDate datePublication;
    private Long auteurId;
    private Long categorieId;
    private Long editeurId;

    /** Libellés pour affichage (lecture seule côté API). */
    private String auteurNom;
    private String categorieNom;
    private String editeurNom;
}
