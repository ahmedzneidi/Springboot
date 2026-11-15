package tn.esprit.spring.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.spring.domain.Maintenance;
import tn.esprit.spring.domain.Vehicule;
import tn.esprit.spring.repository.MaintenanceRepository;
import tn.esprit.spring.repository.VehiculeRepository;
import tn.esprit.spring.web.dto.MaintenanceRequest;

import java.util.List;

@Service
@Transactional
public class MaintenanceService {

    private final MaintenanceRepository maintenanceRepository;
    private final VehiculeRepository vehiculeRepository;

    public MaintenanceService(MaintenanceRepository maintenanceRepository,
                              VehiculeRepository vehiculeRepository) {
        this.maintenanceRepository = maintenanceRepository;
        this.vehiculeRepository = vehiculeRepository;
    }

    public Maintenance creer(MaintenanceRequest request) {
        if (request.idVehicule() == null) {
            throw new IllegalArgumentException("L'identifiant du véhicule est obligatoire.");
        }
        Vehicule vehicule = vehiculeRepository.findById(request.idVehicule())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Véhicule introuvable avec l'identifiant " + request.idVehicule()));
        Maintenance maintenance = new Maintenance();
        maintenance.setDateDebut(request.dateDebut());
        maintenance.setDateFin(request.dateFin());
        maintenance.setDescription(request.description());
        maintenance.setVehicule(vehicule);
        return maintenanceRepository.save(maintenance);
    }

    public Maintenance obtenirParId(long id) {
        return maintenanceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Maintenance introuvable avec l'identifiant " + id));
    }

    public List<Maintenance> listerTous() {
        return maintenanceRepository.findAll();
    }

    public List<Maintenance> listerParVehicule(long idVehicule) {
        return maintenanceRepository.findByVehiculeIdVehicule(idVehicule);
    }

    public List<Maintenance> listerEnCours() {
        return maintenanceRepository.findByDateFinIsNull();
    }

    public Maintenance modifier(long id, Maintenance maintenance) {
        Maintenance existante = obtenirParId(id);
        existante.setDateDebut(maintenance.getDateDebut());
        existante.setDateFin(maintenance.getDateFin());
        existante.setDescription(maintenance.getDescription());
        return maintenanceRepository.save(existante);
    }

    public void supprimer(long id) {
        obtenirParId(id);
        maintenanceRepository.deleteById(id);
    }

    public Maintenance affecterVehicule(long idMaintenance, long idVehicule) {
        Maintenance maintenance = obtenirParId(idMaintenance);
        Vehicule vehicule = vehiculeRepository.findById(idVehicule)
                .orElseThrow(() -> new EntityNotFoundException("Véhicule introuvable avec l'identifiant " + idVehicule));
        maintenance.setVehicule(vehicule);
        return maintenanceRepository.save(maintenance);
    }

    public Maintenance desaffecterVehicule(long idMaintenance) {
        Maintenance maintenance = obtenirParId(idMaintenance);
        maintenance.setVehicule(null);
        return maintenanceRepository.save(maintenance);
    }
}
