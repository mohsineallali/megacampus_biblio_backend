package ma.gov.mini.projet.config;

import ma.gov.mini.projet.entity.Auteur;
import ma.gov.mini.projet.entity.Categorie;
import ma.gov.mini.projet.entity.Editeur;
import ma.gov.mini.projet.entity.Livre;
import ma.gov.mini.projet.repository.AuteurRepository;
import ma.gov.mini.projet.repository.CategorieRepository;
import ma.gov.mini.projet.repository.EditeurRepository;
import ma.gov.mini.projet.repository.LivreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private AuteurRepository auteurRepository;

    @Autowired
    private CategorieRepository categorieRepository;

    @Autowired
    private EditeurRepository editeurRepository;

    @Autowired
    private LivreRepository livreRepository;

    @Override
    public void run(String... args) {
        Auteur hugo = auteurRepository.save(
                Auteur.builder().nom("Hugo").prenom("Victor").build());
        Auteur camus = auteurRepository.save(
                Auteur.builder().nom("Camus").prenom("Albert").build());

        Categorie roman = categorieRepository.save(
                Categorie.builder().nom("Roman").build());
        Categorie philosophie = categorieRepository.save(
                Categorie.builder().nom("Philosophie").build());

        Editeur gallimard = editeurRepository.save(
                Editeur.builder().nom("Gallimard").adresse("Paris").build());
        Editeur flammarion = editeurRepository.save(
                Editeur.builder().nom("Flammarion").adresse("Lyon").build());

        livreRepository.save(Livre.builder()
                .titre("Les Misérables")
                .pages(1900)
                .datePublication(LocalDate.of(1862, 1, 1))
                .auteur(hugo)
                .categorie(roman)
                .editeur(gallimard)
                .build());

        livreRepository.save(Livre.builder()
                .titre("Notre-Dame de Paris")
                .pages(940)
                .datePublication(LocalDate.of(1831, 3, 16))
                .auteur(hugo)
                .categorie(roman)
                .editeur(flammarion)
                .build());

        livreRepository.save(Livre.builder()
                .titre("L'Étranger")
                .pages(320)
                .datePublication(LocalDate.of(1942, 6, 1))
                .auteur(camus)
                .categorie(roman)
                .editeur(gallimard)
                .build());

        livreRepository.save(Livre.builder()
                .titre("Le Mythe de Sisyphe")
                .pages(250)
                .datePublication(LocalDate.of(1942, 10, 16))
                .auteur(camus)
                .categorie(philosophie)
                .editeur(flammarion)
                .build());
    }
}
