package tn.esprit.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.spring.domain.Contrat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ContratRepository extends JpaRepository<Contrat, Long> {

    Optional<Contrat> findByReservationIdReservation(Long idReservation);

    List<Contrat> findByValideTrue();

    List<Contrat> findByValideFalse();

    List<Contrat> findByDateSignatureBetween(LocalDate dateDebut, LocalDate dateFin);

    List<Contrat> findByMontantTotalGreaterThanEqual(BigDecimal montant);

    long countByValideTrue();

    long countByDateSignatureAfter(LocalDate date);
}
