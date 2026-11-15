package tn.esprit.spring.service;

import tn.esprit.spring.domain.Employe;
import tn.esprit.spring.domain.RoleEmploye;

import java.util.List;

public interface IEmployeService {

    Employe creer(Employe employe);

    Employe obtenirParId(long id);

    List<Employe> listerTous();

    List<Employe> listerParAgence(long idAgence);

    List<Employe> listerParRole(RoleEmploye role);

    List<Employe> rechercherParNom(String valeur);

    Employe rechercherParNomEtPrenom(String nom, String prenom);

    boolean existeParNomEtPrenom(String nom, String prenom);

    long compterParAgence(long idAgence);

    Employe modifier(long id, Employe employe);

    void supprimer(long id);

    Employe affecterAgence(long idEmploye, long idAgence);

    Employe desaffecterAgence(long idEmploye);
}
