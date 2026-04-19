package ma.gov.mini.projet.repository;

import ma.gov.mini.projet.entity.Livre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LivreRepository extends JpaRepository<Livre, Long> {

    boolean existsByAuteurId(Long auteurId);

    boolean existsByCategorieId(Long categorieId);

    boolean existsByEditeurId(Long editeurId);
}
