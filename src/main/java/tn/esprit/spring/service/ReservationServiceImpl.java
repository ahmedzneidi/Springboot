package tn.esprit.spring.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.spring.domain.Client;
import tn.esprit.spring.domain.Reservation;
import tn.esprit.spring.domain.StatutReservation;
import tn.esprit.spring.domain.Vehicule;
import tn.esprit.spring.repository.ClientRepository;
import tn.esprit.spring.repository.ReservationRepository;
import tn.esprit.spring.repository.VehiculeRepository;

import java.util.List;

@Service
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ClientRepository clientRepository;
    private final VehiculeRepository vehiculeRepository;

    public ReservationService(ReservationRepository reservationRepository,
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
}
