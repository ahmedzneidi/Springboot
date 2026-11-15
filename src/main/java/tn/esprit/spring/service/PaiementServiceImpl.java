package tn.esprit.spring.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.spring.domain.Contrat;
import tn.esprit.spring.domain.ModePaiement;
import tn.esprit.spring.domain.Paiement;
import tn.esprit.spring.repository.ContratRepository;
import tn.esprit.spring.repository.PaiementRepository;

import java.util.List;

@Service
@Transactional
public class PaiementService {

    private final PaiementRepository paiementRepository;
    private final ContratRepository contratRepository;

    public PaiementService(PaiementRepository paiementRepository,
                           ContratRepository contratRepository) {
        this.paiementRepository = paiementRepository;
        this.contratRepository = contratRepository;
    }

    public Paiement creer(Paiement paiement) {
        return paiementRepository.save(paiement);
    }

    public Paiement obtenirParId(long id) {
        return paiementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Paiement introuvable avec l'identifiant " + id));
    }

    public List<Paiement> listerTous() {
        return paiementRepository.findAll();
    }

    public List<Paiement> listerParContrat(long idContrat) {
        return paiementRepository.findByContratIdContrat(idContrat);
    }

    public List<Paiement> listerParMode(ModePaiement modePaiement) {
        return paiementRepository.findByModePaiement(modePaiement);
    }

    public Paiement modifier(long id, Paiement paiement) {
        Paiement existant = obtenirParId(id);
        existant.setMontant(paiement.getMontant());
        existant.setDatePaiement(paiement.getDatePaiement());
        existant.setModePaiement(paiement.getModePaiement());
        return paiementRepository.save(existant);
    }

    public void supprimer(long id) {
        obtenirParId(id);
        paiementRepository.deleteById(id);
    }

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
