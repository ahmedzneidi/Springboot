package tn.esprit.spring.service;

import tn.esprit.spring.domain.CategorieVehicule;
import tn.esprit.spring.domain.Equipement;
import tn.esprit.spring.domain.StatutVehicule;
import tn.esprit.spring.domain.Vehicule;

import java.math.BigDecimal;
import java.util.List;

public interface IVehiculeService {

    Vehicule creer(Vehicule vehicule);

    Vehicule obtenirParId(long id);

    List<Vehicule> listerTous();

    List<Vehicule> listerParAgence(long idAgence);

    List<Vehicule> listerDisponibles();

    Vehicule rechercherParImmatriculation(String immatriculation);

    boolean existeParImmatriculation(String immatriculation);

    List<Vehicule> listerParCategorieEtStatut(CategorieVehicule categorie, StatutVehicule statut);

    List<Vehicule> rechercherParMarque(String marque);

    List<Vehicule> listerParTarif(BigDecimal tarifMin, BigDecimal tarifMax);

    List<Vehicule> listerParVilleEtStatut(String ville, StatutVehicule statut);

    List<Vehicule> listerParEquipement(String libelle);

    List<Vehicule> listerSansEquipement();

    long compterParStatut(StatutVehicule statut);

    List<Equipement> listerEquipements(long idVehicule);

    Vehicule modifier(long id, Vehicule vehicule);

    void supprimer(long id);

    Vehicule affecterAgence(long idVehicule, long idAgence);

    Vehicule desaffecterAgence(long idVehicule);

    Vehicule affecterEquipement(long idVehicule, long idEquipement);

    Vehicule desaffecterEquipement(long idVehicule, long idEquipement);
}
