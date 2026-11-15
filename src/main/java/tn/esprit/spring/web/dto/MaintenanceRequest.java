package tn.esprit.spring.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import tn.esprit.spring.domain.Maintenance;
import tn.esprit.spring.domain.Vehicule;

import java.time.LocalDate;

@Schema(description = "Données de création ou de modification d'une maintenance")
public record MaintenanceRequest(
        @NotNull(message = "La date de début est obligatoire.")
        @Schema(description = "Date de début de la maintenance", example = "2026-11-15")
        LocalDate dateDebut,

        @Schema(description = "Date de fin de la maintenance, null si en cours", example = "2026-11-18")
        LocalDate dateFin,

        @Size(max = 500, message = "La description ne doit pas dépasser 500 caractères.")
        @Schema(description = "Description des travaux", example = "Révision complète")
        String description,

        @NotNull(message = "L'identifiant du véhicule est obligatoire.")
        @Positive(message = "L'identifiant du véhicule doit être positif.")
        @Schema(description = "Identifiant du véhicule concerné", example = "1")
        Long idVehicule) {

    @JsonIgnore
    @AssertTrue(message = "La date de fin doit être postérieure ou égale à la date de début.")
    public boolean isPeriodeValide() {
        return dateDebut == null || dateFin == null || !dateFin.isBefore(dateDebut);
    }

    public Maintenance toEntity(Vehicule vehicule) {
        Maintenance maintenance = new Maintenance();
        maintenance.setDateDebut(dateDebut);
        maintenance.setDateFin(dateFin);
        maintenance.setDescription(description);
        maintenance.setVehicule(vehicule);
        return maintenance;
    }
}
