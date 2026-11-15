package tn.esprit.spring.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.spring.domain.Agence;
import tn.esprit.spring.domain.Equipement;
import tn.esprit.spring.domain.StatutVehicule;
import tn.esprit.spring.domain.Vehicule;
import tn.esprit.spring.repository.AgenceRepository;
import tn.esprit.spring.repository.EquipementRepository;
import tn.esprit.spring.repository.VehiculeRepository;

import java.util.List;

@Service
@Transactional
public class VehiculeService {

    private final VehiculeRepository vehiculeRepository;
    private final AgenceRepository agenceRepository;
    private final EquipementRepository equipementRepository;

    public VehiculeService(VehiculeRepository vehiculeRepository,
                           AgenceRepository agenceRepository,
                           EquipementRepository equipementRepository) {
        this.vehiculeRepository = vehiculeRepository;
        this.agenceRepository = agenceRepository;
        this.equipementRepository = equipementRepository;
    }

    public Vehicule creer(Vehicule vehicule) {
        return vehiculeRepository.save(vehicule);
    }

    public Vehicule obtenirParId(long id) {
        return vehiculeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vehicule introuvable avec l'identifiant " + id));
    }

    public List<Vehicule> listerTous() {
        return vehiculeRepository.findAll();
    }

    public List<Vehicule> listerParAgence(long idAgence) {
        return vehiculeRepository.findByAgenceIdAgence(idAgence);
    }

    public List<Vehicule> listerDisponibles() {
        return vehiculeRepository.findByStatut(StatutVehicule.DISPONIBLE);
    }

    public Vehicule modifier(long id, Vehicule vehicule) {
        Vehicule existant = obtenirParId(id);
        existant.setImmatriculation(vehicule.getImmatriculation());
        existant.setMarque(vehicule.getMarque());
        existant.setModele(vehicule.getModele());
        existant.setCategorie(vehicule.getCategorie());
        existant.setTarifJournalier(vehicule.getTarifJournalier());
        existant.setStatut(vehicule.getStatut());
        return vehiculeRepository.save(existant);
    }

    public void supprimer(long id) {
        obtenirParId(id);
        vehiculeRepository.deleteById(id);
    }

    public Vehicule affecterAgence(long idVehicule, long idAgence) {
        Vehicule vehicule = obtenirParId(idVehicule);
        Agence agence = agenceRepository.findById(idAgence)
                .orElseThrow(() -> new EntityNotFoundException("Agence introuvable avec l'identifiant " + idAgence));
        vehicule.setAgence(agence);
        return vehiculeRepository.save(vehicule);
    }

    public Vehicule desaffecterAgence(long idVehicule) {
        Vehicule vehicule = obtenirParId(idVehicule);
        vehicule.setAgence(null);
        return vehiculeRepository.save(vehicule);
    }

    public Vehicule affecterEquipement(long idVehicule, long idEquipement) {
        Vehicule vehicule = obtenirParId(idVehicule);
        Equipement equipement = equipementRepository.findById(idEquipement)
                .orElseThrow(() -> new EntityNotFoundException("Equipement introuvable avec l'identifiant " + idEquipement));
        if (!vehicule.getEquipements().contains(equipement)) {
            vehicule.getEquipements().add(equipement);
        }
        return vehiculeRepository.save(vehicule);
    }

    public Vehicule desaffecterEquipement(long idVehicule, long idEquipement) {
        Vehicule vehicule = obtenirParId(idVehicule);
        vehicule.getEquipements().removeIf(equipement -> equipement.getIdEquipement().equals(idEquipement));
        return vehiculeRepository.save(vehicule);
    }
}
