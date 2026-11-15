package tn.esprit.spring.service;

import tn.esprit.spring.domain.Contrat;
import tn.esprit.spring.domain.Paiement;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IContratService {

    Contrat creer(Contrat contrat);

    Contrat obtenirParId(long id);

    List<Contrat> listerTous();

    List<Contrat> listerValides();

    Optional<Contrat> rechercherParReservation(long idReservation);

    List<Contrat> listerInvalides();

    List<Contrat> listerParDateSignatureEntre(LocalDate dateDebut, LocalDate dateFin);

    List<Contrat> listerParMontantMinimum(BigDecimal montant);

    long compterValides();

    long compterSignesApres(LocalDate date);

    Contrat modifier(long id, Contrat contrat);

    void supprimer(long id);

    Contrat affecterReservation(long idContrat, long idReservation);

    Contrat desaffecterReservation(long idContrat);

    Paiement affecterPaiement(long idContrat, long idPaiement);

    Paiement desaffecterPaiement(long idPaiement);
}
