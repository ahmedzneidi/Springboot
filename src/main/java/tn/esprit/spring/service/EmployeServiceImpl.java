package tn.esprit.spring.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.spring.domain.Agence;
import tn.esprit.spring.domain.Employe;
import tn.esprit.spring.domain.RoleEmploye;
import tn.esprit.spring.repository.AgenceRepository;
import tn.esprit.spring.repository.EmployeRepository;

import java.util.List;

@Service
@Transactional
public class EmployeServiceImpl implements IEmployeService {

    private final EmployeRepository employeRepository;
    private final AgenceRepository agenceRepository;

    public EmployeServiceImpl(EmployeRepository employeRepository,
                          AgenceRepository agenceRepository) {
        this.employeRepository = employeRepository;
        this.agenceRepository = agenceRepository;
    }

    public Employe creer(Employe employe) {
        return employeRepository.save(employe);
    }

    public Employe obtenirParId(long id) {
        return employeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employé introuvable avec l'identifiant " + id));
    }

    public List<Employe> listerTous() {
        return employeRepository.findAll();
    }

    public List<Employe> listerParAgence(long idAgence) {
        return employeRepository.findByAgenceIdAgence(idAgence);
    }

    public List<Employe> listerParRole(RoleEmploye role) {
        return employeRepository.findByRole(role);
    }

    public List<Employe> rechercherParNom(String valeur) {
        return employeRepository.findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(valeur, valeur);
    }

    public Employe rechercherParNomEtPrenom(String nom, String prenom) {
        return employeRepository.findByNomIgnoreCaseAndPrenomIgnoreCase(nom, prenom)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Employé introuvable avec le nom " + nom + " et le prénom " + prenom));
    }

    public boolean existeParNomEtPrenom(String nom, String prenom) {
        return employeRepository.existsByNomIgnoreCaseAndPrenomIgnoreCase(nom, prenom);
    }

    public long compterParAgence(long idAgence) {
        return employeRepository.countByAgenceIdAgence(idAgence);
    }

    public Employe modifier(long id, Employe employe) {
        Employe employeExistant = obtenirParId(id);
        employeExistant.setNom(employe.getNom());
        employeExistant.setPrenom(employe.getPrenom());
        employeExistant.setRole(employe.getRole());
        return employeRepository.save(employeExistant);
    }

    public void supprimer(long id) {
        obtenirParId(id);
        employeRepository.deleteById(id);
    }

    public Employe affecterAgence(long idEmploye, long idAgence) {
        Employe employe = obtenirParId(idEmploye);
        Agence agence = agenceRepository.findById(idAgence)
                .orElseThrow(() -> new EntityNotFoundException("Agence introuvable avec l'identifiant " + idAgence));
        employe.setAgence(agence);
        return employeRepository.save(employe);
    }

    public Employe desaffecterAgence(long idEmploye) {
        Employe employe = obtenirParId(idEmploye);
        employe.setAgence(null);
        return employeRepository.save(employe);
    }
}
