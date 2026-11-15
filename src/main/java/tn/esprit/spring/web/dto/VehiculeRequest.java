package tn.esprit.spring.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import tn.esprit.spring.domain.CategorieVehicule;
import tn.esprit.spring.domain.StatutVehicule;
import tn.esprit.spring.domain.Vehicule;

import java.math.BigDecimal;

@Schema(description = "Données de création ou de modification d'un véhicule")
public record VehiculeRequest(
        @NotBlank(message = "L'immatriculation est obligatoire.")
        @Size(min = 4, max = 20, message = "L'immatriculation doit contenir entre 4 et 20 caractères.")
        @Schema(description = "Immatriculation du véhicule", example = "TU-1234")
        String immatriculation,

        @NotBlank(message = "La marque est obligatoire.")
        @Size(max = 50, message = "La marque ne doit pas dépasser 50 caractères.")
        @Schema(description = "Marque du véhicule", example = "Renault")
        String marque,

        @NotBlank(message = "Le modèle est obligatoire.")
        @Size(max = 50, message = "Le modèle ne doit pas dépasser 50 caractères.")
        @Schema(description = "Modèle du véhicule", example = "Clio")
        String modele,

        @NotNull(message = "La catégorie est obligatoire.")
        @Schema(description = "Catégorie du véhicule", example = "CITADINE")
        CategorieVehicule categorie,

        @NotNull(message = "Le tarif journalier est obligatoire.")
        @Positive(message = "Le tarif journalier doit être strictement positif.")
        @Schema(description = "Tarif journalier", example = "120.00")
        BigDecimal tarifJournalier,

        @NotNull(message = "Le statut est obligatoire.")
        @Schema(description = "Statut du véhicule", example = "DISPONIBLE")
        StatutVehicule statut) {

    public Vehicule toEntity() {
        Vehicule vehicule = new Vehicule();
        vehicule.setImmatriculation(immatriculation);
        vehicule.setMarque(marque);
        vehicule.setModele(modele);
        vehicule.setCategorie(categorie);
        vehicule.setTarifJournalier(tarifJournalier);
        vehicule.setStatut(statut);
        return vehicule;
    }
}
