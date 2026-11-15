package tn.esprit.spring.service;

import tn.esprit.spring.domain.ModePaiement;
import tn.esprit.spring.domain.Paiement;

import java.time.LocalDate;
import java.util.List;

public interface IPaiementService {

    Paiement creer(Paiement paiement);

    Paiement obtenirParId(long id);

    List<Paiement> listerTous();

    Paiement modifier(long id, Paiement paiement);

    void supprimer(long id);

    List<Paiement> listerParContrat(long idContrat);

    List<Paiement> listerParMode(ModePaiement modePaiement);

    List<Paiement> listerParDatePaiementEntre(LocalDate dateDebut, LocalDate dateFin);

    List<Paiement> listerParDatePaiementApres(LocalDate date);

    long compterParContrat(long idContrat);

    Paiement affecterContrat(long idPaiement, long idContrat);

    Paiement desaffecterContrat(long idPaiement);
}
