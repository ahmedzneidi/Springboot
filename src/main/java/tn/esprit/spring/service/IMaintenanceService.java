package tn.esprit.spring.service;

import tn.esprit.spring.domain.Maintenance;
import tn.esprit.spring.web.dto.MaintenanceRequest;

import java.time.LocalDate;
import java.util.List;

public interface IMaintenanceService {

    Maintenance creer(MaintenanceRequest request);

    Maintenance obtenirParId(long id);

    List<Maintenance> listerTous();

    List<Maintenance> listerParVehicule(long idVehicule);

    List<Maintenance> listerEnCours();

    List<Maintenance> listerParImmatriculation(String immatriculation);

    List<Maintenance> listerParDateDebutEntre(LocalDate dateDebut, LocalDate dateFin);

    List<Maintenance> listerTerminees();

    long compterParVehicule(long idVehicule);

    Maintenance modifier(long id, Maintenance maintenance);

    Maintenance modifier(long id, MaintenanceRequest request);

    void supprimer(long id);

    Maintenance affecterVehicule(long idMaintenance, long idVehicule);

    Maintenance desaffecterVehicule(long idMaintenance);
}
