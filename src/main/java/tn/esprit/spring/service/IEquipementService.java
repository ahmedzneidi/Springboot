package tn.esprit.spring.service;

import tn.esprit.spring.domain.Equipement;

import java.util.List;
import java.util.Optional;

public interface IEquipementService {

    Equipement creer(Equipement equipement);

    Equipement obtenirParId(long id);

    List<Equipement> listerTous();

    Optional<Equipement> rechercherParLibelle(String libelle);

    List<Equipement> rechercherParLibelleContenant(String libelle);

    boolean existeParLibelle(String libelle);

    List<Equipement> listerParVehicule(long idVehicule);

    Equipement modifier(long id, Equipement equipement);

    void supprimer(long id);
}
