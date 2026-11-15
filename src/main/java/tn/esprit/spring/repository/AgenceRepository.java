package tn.esprit.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.spring.domain.Agence;

import java.util.List;
import java.util.Optional;

public interface AgenceRepository extends JpaRepository<Agence, Long> {

    Optional<Agence> findByNomIgnoreCase(String nom);

    boolean existsByNomIgnoreCase(String nom);

    List<Agence> findByVilleIgnoreCase(String ville);

    List<Agence> findByNomContainingOrderByVilleAsc(String nom);

    List<Agence> findByVilleAndTelephoneIsNotNull(String ville);

    long countByVille(String ville);

    void deleteByVille(String ville);
}
