package tn.esprit.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.spring.domain.Reservation;
import tn.esprit.spring.domain.StatutReservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByClientIdClient(Long idClient);

    List<Reservation> findByVehiculeIdVehicule(Long idVehicule);

    List<Reservation> findByStatut(StatutReservation statut);

    List<Reservation> findByClientIdClientAndStatut(Long idClient, StatutReservation statut);

    List<Reservation> findByDateDebutBetween(LocalDate dateDebut, LocalDate dateFin);

    List<Reservation> findByDateFinBefore(LocalDate date);

    boolean existsByVehiculeIdVehiculeAndStatut(Long idVehicule, StatutReservation statut);

    Optional<Reservation> findByContratIdContrat(Long idContrat);

    long countByStatut(StatutReservation statut);
}
