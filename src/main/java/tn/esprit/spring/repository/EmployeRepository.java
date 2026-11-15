package tn.esprit.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.spring.domain.Employe;
import tn.esprit.spring.domain.RoleEmploye;

import java.util.List;
import java.util.Optional;

public interface EmployeRepository extends JpaRepository<Employe, Long> {

    List<Employe> findByAgenceIdAgence(Long idAgence);

    List<Employe> findByRole(RoleEmploye role);

    List<Employe> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(String nom, String prenom);

    Optional<Employe> findByNomIgnoreCaseAndPrenomIgnoreCase(String nom, String prenom);

    boolean existsByNomIgnoreCaseAndPrenomIgnoreCase(String nom, String prenom);

    long countByAgenceIdAgence(Long idAgence);

    void deleteByAgenceIdAgence(Long idAgence);
}
