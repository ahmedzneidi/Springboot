package tn.esprit.spring.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.spring.domain.Client;
import tn.esprit.spring.domain.Reservation;
import tn.esprit.spring.domain.StatutReservation;
import tn.esprit.spring.domain.Vehicule;
import tn.esprit.spring.repository.ClientRepository;
import tn.esprit.spring.repository.ReservationRepository;
import tn.esprit.spring.repository.VehiculeRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class    ReservationServiceImpl implements IReservationService {

    private final ReservationRepository reservationRepository;
    private final ClientRepository clientRepository;
    private final VehiculeRepository vehiculeRepository;

    public ReservationServiceImpl(ReservationRepository reservationRepository,
                              ClientRepository clientRepository,
                              VehiculeRepository vehiculeRepository) {
        this.reservationRepository = reservationRepository;
        this.clientRepository = clientRepository;
        this.vehiculeRepository = vehiculeRepository;
    }

    public Reservation creer(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public Reservation obtenirParId(long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reservation introuvable avec l'identifiant " + id));
    }

    public List<Reservation> listerTous() {
        return reservationRepository.findAll();
    }

    public List<Reservation> listerParClient(long idClient) {
        return reservationRepository.findByClientIdClient(idClient);
    }

    public List<Reservation> listerParVehicule(long idVehicule) {
        return reservationRepository.findByVehiculeIdVehicule(idVehicule);
    }

    public List<Reservation> listerParStatut(StatutReservation statut) {
        return reservationRepository.findByStatut(statut);
    }

    public List<Reservation> listerParClientEtStatut(long idClient, StatutReservation statut) {
        return reservationRepository.findByClientIdClientAndStatut(idClient, statut);
    }

    public List<Reservation> listerParDateDebutEntre(LocalDate dateDebut, LocalDate dateFin) {
        return reservationRepository.findByDateDebutBetween(dateDebut, dateFin);
    }

    public List<Reservation> listerTermineesAvant(LocalDate date) {
        return reservationRepository.findByDateFinBefore(date);
    }

    public boolean existeActivePourVehicule(long idVehicule) {
        return reservationRepository.existsByVehiculeIdVehiculeAndStatut(idVehicule, StatutReservation.CONFIRMEE);
    }

    public Reservation rechercherParContrat(long idContrat) {
        return reservationRepository.findByContratIdContrat(idContrat)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Reservation introuvable pour le contrat " + idContrat));
    }

    public long compterParStatut(StatutReservation statut) {
        return reservationRepository.countByStatut(statut);
    }

    public Reservation modifier(long id, Reservation reservation) {
        Reservation existante = obtenirParId(id);
        existante.setDateDebut(reservation.getDateDebut());
        existante.setDateFin(reservation.getDateFin());
        existante.setStatut(reservation.getStatut());
        return reservationRepository.save(existante);
    }

    public void supprimer(long id) {
        obtenirParId(id);
        reservationRepository.deleteById(id);
    }

    public Reservation affecterClient(long idReservation, long idClient) {
        Reservation reservation = obtenirParId(idReservation);
        Client client = clientRepository.findById(idClient)
                .orElseThrow(() -> new EntityNotFoundException("Client introuvable avec l'identifiant " + idClient));
        reservation.setClient(client);
        return reservationRepository.save(reservation);
    }

    public Reservation desaffecterClient(long idReservation) {
        Reservation reservation = obtenirParId(idReservation);
        reservation.setClient(null);
        return reservationRepository.save(reservation);
    }

    public Reservation affecterVehicule(long idReservation, long idVehicule) {
        Reservation reservation = obtenirParId(idReservation);
        Vehicule vehicule = vehiculeRepository.findById(idVehicule)
                .orElseThrow(() -> new EntityNotFoundException("Véhicule introuvable avec l'identifiant " + idVehicule));
        reservation.setVehicule(vehicule);
        return reservationRepository.save(reservation);
    }

    public Reservation desaffecterVehicule(long idReservation) {
        Reservation reservation = obtenirParId(idReservation);
        reservation.setVehicule(null);
        return reservationRepository.save(reservation);
    }

    @Scheduled(cron = "0 1 0 * * *")
    @Override
    public void verifierReservationExpirees() {
        LocalDate today = LocalDate.now();
        List<Reservation> reservations =
                reservationRepository.findByStatutAndDateFinBefore(StatutReservation.CONFIRMEE, today);
        for (Reservation reservation : reservations) {
            reservation.setStatut(StatutReservation.TERMINEE);
        }
        reservationRepository.saveAll(reservations);
    }
}

