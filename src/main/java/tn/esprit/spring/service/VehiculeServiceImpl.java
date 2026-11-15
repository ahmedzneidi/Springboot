package tn.esprit.spring.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.spring.domain.Agence;
import tn.esprit.spring.domain.CategorieVehicule;
import tn.esprit.spring.domain.Equipement;
import tn.esprit.spring.domain.StatutVehicule;
import tn.esprit.spring.domain.Vehicule;
import tn.esprit.spring.repository.AgenceRepository;
import tn.esprit.spring.repository.EquipementRepository;
import tn.esprit.spring.repository.VehiculeRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class VehiculeServiceImpl implements IVehiculeService {

    private final VehiculeRepository vehiculeRepository;
    private final AgenceRepository agenceRepository;
    private final EquipementRepository equipementRepository;

    public VehiculeServiceImpl(VehiculeRepository vehiculeRepository,
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

    public Vehicule rechercherParImmatriculation(String immatriculation) {
        return vehiculeRepository.findByImmatriculationIgnoreCase(immatriculation)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Vehicule introuvable avec l'immatriculation " + immatriculation));
    }

    public boolean existeParImmatriculation(String immatriculation) {
        return vehiculeRepository.existsByImmatriculationIgnoreCase(immatriculation);
    }

    public List<Vehicule> listerParCategorieEtStatut(CategorieVehicule categorie, StatutVehicule statut) {
        return vehiculeRepository.findByCategorieAndStatut(categorie, statut);
    }

    public List<Vehicule> rechercherParMarque(String marque) {
        return vehiculeRepository.findByMarqueContainingIgnoreCase(marque);
    }

    public List<Vehicule> listerParTarif(BigDecimal tarifMin, BigDecimal tarifMax) {
        return vehiculeRepository.findByTarifJournalierBetween(tarifMin, tarifMax);
    }

    public List<Vehicule> listerParVilleEtStatut(String ville, StatutVehicule statut) {
        return vehiculeRepository.findByAgenceVilleIgnoreCaseAndStatut(ville, statut);
    }

    public List<Vehicule> listerParEquipement(String libelle) {
        return vehiculeRepository.findByEquipementsLibelleIgnoreCase(libelle);
    }

    public List<Vehicule> listerSansEquipement() {
        return vehiculeRepository.findByEquipementsIsEmpty();
    }

    public long compterParStatut(StatutVehicule statut) {
        return vehiculeRepository.countByStatut(statut);
    }

    public List<Equipement> listerEquipements(long idVehicule) {
        return obtenirParId(idVehicule).getEquipements();
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
