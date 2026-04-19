package ma.gov.mini.projet.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "auteurs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Auteur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String prenom;

    @OneToMany(mappedBy = "auteur")
    @JsonManagedReference("livre-auteur")
    @Builder.Default
    private List<Livre> livres = new ArrayList<>();
}
