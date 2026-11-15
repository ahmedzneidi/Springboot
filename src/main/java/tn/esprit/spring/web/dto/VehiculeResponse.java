package tn.esprit.spring.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import tn.esprit.spring.domain.CategorieVehicule;
import tn.esprit.spring.domain.StatutVehicule;
import tn.esprit.spring.domain.Vehicule;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "Véhicule retourné par l'API")
public record VehiculeResponse(
        @Schema(description = "Identifiant du véhicule", example = "1")
        Long idVehicule,
        @Schema(description = "Immatriculation", example = "TU-1234")
        String immatriculation,
        @Schema(description = "Marque", example = "Renault")
        String marque,
        @Schema(description = "Modèle", example = "Clio")
        String modele,
        @Schema(description = "Catégorie", example = "CITADINE")
        CategorieVehicule categorie,
        @Schema(description = "Tarif journalier", example = "120.00")
        BigDecimal tarifJournalier,
        @Schema(description = "Statut", example = "DISPONIBLE")
        StatutVehicule statut,
        @Schema(description = "Identifiant de l'agence rattachée", example = "1")
        Long idAgence,
        @Schema(description = "Équipements du véhicule")
        List<EquipementResponse> equipements) {

    public static VehiculeResponse from(Vehicule vehicule) {
        List<EquipementResponse> equipements = vehicule.getEquipements() == null
                ? List.of()
                : vehicule.getEquipements().stream().map(EquipementResponse::from).toList();
        return new VehiculeResponse(
                vehicule.getIdVehicule(),
                vehicule.getImmatriculation(),
                vehicule.getMarque(),
                vehicule.getModele(),
                vehicule.getCategorie(),
                vehicule.getTarifJournalier(),
                vehicule.getStatut(),
                vehicule.getAgence() == null ? null : vehicule.getAgence().getIdAgence(),
                equipements);
    }
}
