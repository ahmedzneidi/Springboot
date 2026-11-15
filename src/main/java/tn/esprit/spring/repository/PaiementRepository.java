package tn.esprit.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.spring.domain.ModePaiement;
import tn.esprit.spring.domain.Paiement;

import java.time.LocalDate;
import java.util.List;

public interface PaiementRepository extends JpaRepository<Paiement, Long> {

    List<Paiement> findByContratIdContrat(Long idContrat);

    List<Paiement> findByModePaiement(ModePaiement modePaiement);

    List<Paiement> findByDatePaiementBetween(LocalDate dateDebut, LocalDate dateFin);

    List<Paiement> findByDatePaiementAfter(LocalDate date);

    long countByContratIdContrat(Long idContrat);

    void deleteByContratIdContrat(Long idContrat);
}
