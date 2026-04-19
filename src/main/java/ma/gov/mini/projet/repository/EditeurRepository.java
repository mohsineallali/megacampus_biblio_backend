package ma.gov.mini.projet.repository;

import ma.gov.mini.projet.entity.Editeur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EditeurRepository extends JpaRepository<Editeur, Long> {
}
