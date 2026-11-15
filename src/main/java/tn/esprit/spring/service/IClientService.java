package tn.esprit.spring.service;

import tn.esprit.spring.domain.Client;
import tn.esprit.spring.domain.Reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IClientService {

    Client creer(Client client);

    Client obtenirParId(long id);

    List<Client> listerTous();

    Optional<Client> rechercherParEmail(String email);

    List<Client> rechercherParNom(String valeur);

    boolean existeParNumPermis(String numPermis);

    List<Client> listerParDateInscriptionEntre(LocalDate dateDebut, LocalDate dateFin);

    List<Client> listerParDateInscriptionApres(LocalDate date);

    long compterParDateInscriptionApres(LocalDate date);

    Client modifier(long id, Client client);

    void supprimer(long id);

    Reservation affecterReservation(long idClient, long idReservation);

    Reservation desaffecterReservation(long idReservation);
}
