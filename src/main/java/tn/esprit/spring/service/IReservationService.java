package tn.esprit.spring.service;

import tn.esprit.spring.domain.Reservation;
import tn.esprit.spring.domain.StatutReservation;

import java.time.LocalDate;
import java.util.List;

public interface IReservationService {

    Reservation creer(Reservation reservation);

    Reservation obtenirParId(long id);

    List<Reservation> listerTous();

    List<Reservation> listerParClient(long idClient);

    List<Reservation> listerParVehicule(long idVehicule);

    List<Reservation> listerParStatut(StatutReservation statut);

    List<Reservation> listerParClientEtStatut(long idClient, StatutReservation statut);

    List<Reservation> listerParDateDebutEntre(LocalDate dateDebut, LocalDate dateFin);

    List<Reservation> listerTermineesAvant(LocalDate date);

    boolean existeActivePourVehicule(long idVehicule);

    Reservation rechercherParContrat(long idContrat);

    long compterParStatut(StatutReservation statut);

    Reservation modifier(long id, Reservation reservation);

    void supprimer(long id);

    Reservation affecterClient(long idReservation, long idClient);

    Reservation desaffecterClient(long idReservation);

    Reservation affecterVehicule(long idReservation, long idVehicule);

    Reservation desaffecterVehicule(long idReservation);

    void verifierReservationExpirees();
}
