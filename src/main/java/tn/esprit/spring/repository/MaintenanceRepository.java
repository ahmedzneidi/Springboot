package tn.esprit.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.spring.domain.Maintenance;

import java.time.LocalDate;
import java.util.List;

public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {

    List<Maintenance> findByVehiculeIdVehicule(Long idVehicule);

    List<Maintenance> findByVehiculeImmatriculationIgnoreCase(String immatriculation);

    List<Maintenance> findByDateDebutBetween(LocalDate dateDebut, LocalDate dateFin);

    List<Maintenance> findByDateFinIsNull();

    List<Maintenance> findByDateFinIsNotNull();

    long countByVehiculeIdVehicule(Long idVehicule);
}
