package tn.esprit.spring.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.spring.domain.Contrat;
import tn.esprit.spring.domain.Paiement;
import tn.esprit.spring.domain.Reservation;
import tn.esprit.spring.repository.ContratRepository;
import tn.esprit.spring.repository.PaiementRepository;
import tn.esprit.spring.repository.ReservationRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ContratServiceImpl implements IContratService {

    private final ContratRepository contratRepository;
    private final ReservationRepository reservationRepository;
    private final PaiementRepository paiementRepository;

    public ContratServiceImpl(ContratRepository contratRepository,
                          ReservationRepository reservationRepository,
                          PaiementRepository paiementRepository) {
        this.contratRepository = contratRepository;
        this.reservationRepository = reservationRepository;
        this.paiementRepository = paiementRepository;
    }

    public Contrat creer(Contrat contrat) {
        return contratRepository.save(contrat);
    }

    public Contrat obtenirParId(long id) {
        return contratRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contrat introuvable avec l'identifiant " + id));
    }

    public List<Contrat> listerTous() {
        return contratRepository.findAll();
    }

    public List<Contrat> listerValides() {
        return contratRepository.findByValideTrue();
    }

    public Optional<Contrat> rechercherParReservation(long idReservation) {
        return contratRepository.findByReservationIdReservation(idReservation);
    }

    public List<Contrat> listerInvalides() {
        return contratRepository.findByValideFalse();
    }

    public List<Contrat> listerParDateSignatureEntre(LocalDate dateDebut, LocalDate dateFin) {
        return contratRepository.findByDateSignatureBetween(dateDebut, dateFin);
    }

    public List<Contrat> listerParMontantMinimum(BigDecimal montant) {
        return contratRepository.findByMontantTotalGreaterThanEqual(montant);
    }

    public long compterValides() {
        return contratRepository.countByValideTrue();
    }

    public long compterSignesApres(LocalDate date) {
        return contratRepository.countByDateSignatureAfter(date);
    }

    public Contrat modifier(long id, Contrat contrat) {
        Contrat existant = obtenirParId(id);
        existant.setDateSignature(contrat.getDateSignature());
        existant.setMontantTotal(contrat.getMontantTotal());
        existant.setValide(contrat.isValide());
        return contratRepository.save(existant);
    }

    public void supprimer(long id) {
        obtenirParId(id);
        contratRepository.deleteById(id);
    }

    public Contrat affecterReservation(long idContrat, long idReservation) {
        Contrat contrat = obtenirParId(idContrat);
        Reservation reservation = reservationRepository.findById(idReservation)
                .orElseThrow(() -> new EntityNotFoundException("Réservation introuvable avec l'identifiant " + idReservation));
        contrat.setReservation(reservation);
        reservation.setContrat(contrat);
        return contratRepository.save(contrat);
    }

    public Contrat desaffecterReservation(long idContrat) {
        Contrat contrat = obtenirParId(idContrat);
        if (contrat.getReservation() != null) {
            contrat.getReservation().setContrat(null);
            contrat.setReservation(null);
        }
        return contratRepository.save(contrat);
    }

    public Paiement affecterPaiement(long idContrat, long idPaiement) {
        Contrat contrat = obtenirParId(idContrat);
        Paiement paiement = paiementRepository.findById(idPaiement)
                .orElseThrow(() -> new EntityNotFoundException("Paiement introuvable avec l'identifiant " + idPaiement));
        paiement.setContrat(contrat);
        if (!contrat.getPaiements().contains(paiement)) {
            contrat.getPaiements().add(paiement);
        }
        return paiementRepository.save(paiement);
    }

    public Paiement desaffecterPaiement(long idPaiement) {
        Paiement paiement = paiementRepository.findById(idPaiement)
                .orElseThrow(() -> new EntityNotFoundException("Paiement introuvable avec l'identifiant " + idPaiement));
        Contrat contrat = paiement.getContrat();
        if (contrat != null) {
            contrat.getPaiements().remove(paiement);
        }
        paiement.setContrat(null);
        return paiementRepository.save(paiement);
    }
}
