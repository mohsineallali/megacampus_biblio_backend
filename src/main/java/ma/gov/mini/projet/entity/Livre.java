package ma.gov.mini.projet.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "livres")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Livre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;
    private int pages;
    private LocalDate datePublication;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auteur_id")
    @JsonBackReference("livre-auteur")
    private Auteur auteur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categorie_id")
    @JsonBackReference("livre-categorie")
    private Categorie categorie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "editeur_id")
    @JsonBackReference("livre-editeur")
    private Editeur editeur;
}
