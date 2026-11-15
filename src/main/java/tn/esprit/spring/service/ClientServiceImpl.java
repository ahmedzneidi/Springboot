package tn.esprit.spring.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.spring.domain.Client;
import tn.esprit.spring.domain.Reservation;
import tn.esprit.spring.repository.ClientRepository;
import tn.esprit.spring.repository.ReservationRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClientServiceImpl implements IClientService {

    private final ClientRepository clientRepository;
    private final ReservationRepository reservationRepository;

    public ClientServiceImpl(ClientRepository clientRepository,
                         ReservationRepository reservationRepository) {
        this.clientRepository = clientRepository;
        this.reservationRepository = reservationRepository;
    }

    public Client creer(Client client) {
        return clientRepository.save(client);
    }

    public Client obtenirParId(long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client introuvable avec l'identifiant " + id));
    }

    public List<Client> listerTous() {
        return clientRepository.findAll();
    }

    public Optional<Client> rechercherParEmail(String email) {
        return clientRepository.findByEmailIgnoreCase(email);
    }

    public List<Client> rechercherParNom(String valeur) {
        return clientRepository.findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(valeur, valeur);
    }

    public boolean existeParNumPermis(String numPermis) {
        return clientRepository.existsByNumPermis(numPermis);
    }

    public List<Client> listerParDateInscriptionEntre(LocalDate dateDebut, LocalDate dateFin) {
        return clientRepository.findByDateInscriptionBetween(dateDebut, dateFin);
    }

    public List<Client> listerParDateInscriptionApres(LocalDate date) {
        return clientRepository.findByDateInscriptionAfter(date);
    }

    public long compterParDateInscriptionApres(LocalDate date) {
        return clientRepository.countByDateInscriptionAfter(date);
    }

    public Client modifier(long id, Client client) {
        Client existant = obtenirParId(id);
        existant.setNom(client.getNom());
        existant.setPrenom(client.getPrenom());
        existant.setEmail(client.getEmail());
        existant.setTelephone(client.getTelephone());
        existant.setNumPermis(client.getNumPermis());
        existant.setDateInscription(client.getDateInscription());
        return clientRepository.save(existant);
    }

    public void supprimer(long id) {
        obtenirParId(id);
        clientRepository.deleteById(id);
    }

    public Reservation affecterReservation(long idClient, long idReservation) {
        Client client = obtenirParId(idClient);
        Reservation reservation = reserver(idReservation);
        reservation.setClient(client);
        return reservationRepository.save(reservation);
    }

    public Reservation desaffecterReservation(long idReservation) {
        Reservation reservation = reserver(idReservation);
        reservation.setClient(null);
        return reservationRepository.save(reservation);
    }

    private Reservation reserver(long idReservation) {
        return reservationRepository.findById(idReservation)
                .orElseThrow(() -> new EntityNotFoundException("Réservation introuvable avec l'identifiant " + idReservation));
    }
}
