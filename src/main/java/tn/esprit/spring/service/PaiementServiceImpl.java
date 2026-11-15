package tn.esprit.spring.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.spring.domain.Contrat;
import tn.esprit.spring.domain.ModePaiement;
import tn.esprit.spring.domain.Paiement;
import tn.esprit.spring.repository.ContratRepository;
import tn.esprit.spring.repository.PaiementRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class PaiementServiceImpl implements IPaiementService {

    private final PaiementRepository paiementRepository;
    private final ContratRepository contratRepository;

    public PaiementServiceImpl(PaiementRepository paiementRepository,
                           ContratRepository contratRepository) {
        this.paiementRepository = paiementRepository;
        this.contratRepository = contratRepository;
    }

    @Override
    public Paiement creer(Paiement paiement) {
        return paiementRepository.save(paiement);
    }

    @Override
    public Paiement obtenirParId(long id) {
        return paiementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Paiement introuvable avec l'identifiant " + id));
    }

    @Override
    public List<Paiement> listerTous() {
        return paiementRepository.findAll();
    }

    @Override
    public List<Paiement> listerParContrat(long idContrat) {
        return paiementRepository.findByContratIdContrat(idContrat);
    }

    @Override
    public List<Paiement> listerParMode(ModePaiement modePaiement) {
        return paiementRepository.findByModePaiement(modePaiement);
    }

    @Override
    public List<Paiement> listerParDatePaiementEntre(LocalDate dateDebut, LocalDate dateFin) {
        return paiementRepository.findByDatePaiementBetween(dateDebut, dateFin);
    }

    @Override
    public List<Paiement> listerParDatePaiementApres(LocalDate date) {
        return paiementRepository.findByDatePaiementAfter(date);
    }

    @Override
    public long compterParContrat(long idContrat) {
        return paiementRepository.countByContratIdContrat(idContrat);
    }

    @Override
    public Paiement modifier(long id, Paiement paiement) {
        Paiement existant = obtenirParId(id);
        existant.setMontant(paiement.getMontant());
        existant.setDatePaiement(paiement.getDatePaiement());
        existant.setModePaiement(paiement.getModePaiement());
        return paiementRepository.save(existant);
    }

    @Override
    public void supprimer(long id) {
        obtenirParId(id);
        paiementRepository.deleteById(id);
    }

    @Override
    public Paiement affecterContrat(long idPaiement, long idContrat) {
        Paiement paiement = obtenirParId(idPaiement);
        Contrat contrat = contratRepository.findById(idContrat)
                .orElseThrow(() -> new EntityNotFoundException("Contrat introuvable avec l'identifiant " + idContrat));
        paiement.setContrat(contrat);
        if (!contrat.getPaiements().contains(paiement)) {
            contrat.getPaiements().add(paiement);
        }
        return paiementRepository.save(paiement);
    }

    @Override
    public Paiement desaffecterContrat(long idPaiement) {
        Paiement paiement = obtenirParId(idPaiement);
        Contrat contrat = paiement.getContrat();
        if (contrat != null) {
            contrat.getPaiements().remove(paiement);
        }
        paiement.setContrat(null);
        return paiementRepository.save(paiement);
    }
}
