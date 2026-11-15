package tn.esprit.spring.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.spring.domain.Equipement;
import tn.esprit.spring.repository.EquipementRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EquipementService {

    private final EquipementRepository equipementRepository;

    public EquipementService(EquipementRepository equipementRepository) {
        this.equipementRepository = equipementRepository;
    }

    public Equipement creer(Equipement equipement) {
        return equipementRepository.save(equipement);
    }

    public Equipement obtenirParId(long id) {
        return equipementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Equipement introuvable avec l'identifiant " + id));
    }

    public List<Equipement> listerTous() {
        return equipementRepository.findAll();
    }

    public Optional<Equipement> rechercherParLibelle(String libelle) {
        return equipementRepository.findByLibelleIgnoreCase(libelle);
    }

    public List<Equipement> listerParVehicule(long idVehicule) {
        return equipementRepository.findByVehiculesIdVehicule(idVehicule);
    }

    public Equipement modifier(long id, Equipement equipement) {
        Equipement existant = obtenirParId(id);
        existant.setLibelle(equipement.getLibelle());
        return equipementRepository.save(existant);
    }

    public void supprimer(long id) {
        obtenirParId(id);
        equipementRepository.deleteById(id);
    }
}
