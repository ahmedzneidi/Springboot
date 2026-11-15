package tn.esprit.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.spring.domain.CategorieVehicule;
import tn.esprit.spring.domain.StatutVehicule;
import tn.esprit.spring.domain.Vehicule;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface VehiculeRepository extends JpaRepository<Vehicule, Long> {

    Optional<Vehicule> findByImmatriculationIgnoreCase(String immatriculation);

    boolean existsByImmatriculationIgnoreCase(String immatriculation);

    List<Vehicule> findByStatut(StatutVehicule statut);

    List<Vehicule> findByCategorieAndStatut(CategorieVehicule categorie, StatutVehicule statut);

    List<Vehicule> findByMarqueContainingIgnoreCase(String marque);

    List<Vehicule> findByTarifJournalierBetween(BigDecimal tarifMin, BigDecimal tarifMax);

    List<Vehicule> findByAgenceIdAgence(Long idAgence);

    List<Vehicule> findByAgenceVilleIgnoreCaseAndStatut(String ville, StatutVehicule statut);

    List<Vehicule> findByEquipementsLibelleIgnoreCase(String libelle);

    List<Vehicule> findByEquipementsIsEmpty();

    long countByStatut(StatutVehicule statut);
}
