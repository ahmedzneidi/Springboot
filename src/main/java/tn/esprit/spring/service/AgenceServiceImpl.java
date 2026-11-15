package tn.esprit.spring.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.spring.domain.Agence;
import tn.esprit.spring.domain.Employe;
import tn.esprit.spring.domain.Vehicule;
import tn.esprit.spring.repository.AgenceRepository;
import tn.esprit.spring.repository.EmployeRepository;
import tn.esprit.spring.repository.VehiculeRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AgenceServiceImpl implements IAgenceService {

    private final AgenceRepository agenceRepository;
    private final EmployeRepository employeRepository;
    private final VehiculeRepository vehiculeRepository;

    public AgenceServiceImpl(AgenceRepository agenceRepository,
                         EmployeRepository employeRepository,
                         VehiculeRepository vehiculeRepository) {
        this.agenceRepository = agenceRepository;
        this.employeRepository = employeRepository;
        this.vehiculeRepository = vehiculeRepository;
    }

    public Agence creer(Agence agence) {
        return agenceRepository.save(agence);
    }

    public Agence obtenirParId(long id) {
        return agenceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Agence introuvable avec l'identifiant " + id));
    }

    public List<Agence> listerTous() {
        return agenceRepository.findAll();
    }

    public Optional<Agence> rechercherParNom(String nom) {
        return agenceRepository.findByNomIgnoreCase(nom);
    }

    public List<Agence> rechercherParVille(String ville) {
        return agenceRepository.findByVilleIgnoreCase(ville);
    }

    public long compterParVille(String ville) {
        return agenceRepository.countByVille(ville);
    }

    public List<Agence> rechercherParNomContenant(String nom) {
        return agenceRepository.findByNomContainingOrderByVilleAsc(nom);
    }

    public List<Agence> listerParVilleAvecTelephone(String ville) {
        return agenceRepository.findByVilleAndTelephoneIsNotNull(ville);
    }

    public boolean existeParNom(String nom) {
        return agenceRepository.existsByNomIgnoreCase(nom);
    }

    public Agence modifier(long id, Agence agence) {
        Agence existante = obtenirParId(id);
        existante.setNom(agence.getNom());
        existante.setVille(agence.getVille());
        existante.setAdresse(agence.getAdresse());
        existante.setTelephone(agence.getTelephone());
        return agenceRepository.save(existante);
    }

    public void supprimer(long id) {
        obtenirParId(id);
        agenceRepository.deleteById(id);
    }

    public Employe affecterEmploye(long idAgence, long idEmploye) {
        Agence agence = obtenirParId(idAgence);
        Employe employe = employeRepository.findById(idEmploye)
                .orElseThrow(() -> new EntityNotFoundException("Employé introuvable avec l'identifiant " + idEmploye));
        employe.setAgence(agence);
        return employeRepository.save(employe);
    }

    public Employe desaffecterEmploye(long idEmploye) {
        Employe employe = employeRepository.findById(idEmploye)
                .orElseThrow(() -> new EntityNotFoundException("Employé introuvable avec l'identifiant " + idEmploye));
        employe.setAgence(null);
        return employeRepository.save(employe);
    }

    public Vehicule affecterVehicule(long idAgence, long idVehicule) {
        Agence agence = obtenirParId(idAgence);
        Vehicule vehicule = vehiculeRepository.findById(idVehicule)
                .orElseThrow(() -> new EntityNotFoundException("Véhicule introuvable avec l'identifiant " + idVehicule));
        vehicule.setAgence(agence);
        return vehiculeRepository.save(vehicule);
    }

    public Vehicule desaffecterVehicule(long idVehicule) {
        Vehicule vehicule = vehiculeRepository.findById(idVehicule)
                .orElseThrow(() -> new EntityNotFoundException("Véhicule introuvable avec l'identifiant " + idVehicule));
        vehicule.setAgence(null);
        return vehiculeRepository.save(vehicule);
    }
}
