package tn.esprit.spring.service;

import tn.esprit.spring.domain.Agence;
import tn.esprit.spring.domain.Employe;
import tn.esprit.spring.domain.Vehicule;

import java.util.List;
import java.util.Optional;

public interface IAgenceService {

    Agence creer(Agence agence);

    Agence obtenirParId(long id);

    List<Agence> listerTous();

    Optional<Agence> rechercherParNom(String nom);

    List<Agence> rechercherParVille(String ville);

    long compterParVille(String ville);

    List<Agence> rechercherParNomContenant(String nom);

    List<Agence> listerParVilleAvecTelephone(String ville);

    boolean existeParNom(String nom);

    Agence modifier(long id, Agence agence);

    void supprimer(long id);

    Employe affecterEmploye(long idAgence, long idEmploye);

    Employe desaffecterEmploye(long idEmploye);

    Vehicule affecterVehicule(long idAgence, long idVehicule);

    Vehicule desaffecterVehicule(long idVehicule);
}
