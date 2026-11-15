package tn.esprit.spring.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import tn.esprit.spring.domain.Maintenance;

import java.time.LocalDate;

@Schema(description = "Maintenance retournée par l'API")
public record MaintenanceResponse(
        @Schema(description = "Identifiant de la maintenance", example = "1")
        Long idMaintenance,
        @Schema(description = "Date de début", example = "2026-11-15")
        LocalDate dateDebut,
        @Schema(description = "Date de fin", example = "2026-11-18")
        LocalDate dateFin,
        @Schema(description = "Description", example = "Révision complète")
        String description,
        @Schema(description = "Identifiant du véhicule", example = "1")
        Long idVehicule) {

    public static MaintenanceResponse from(Maintenance maintenance) {
        return new MaintenanceResponse(
                maintenance.getIdMaintenance(),
                maintenance.getDateDebut(),
                maintenance.getDateFin(),
                maintenance.getDescription(),
                maintenance.getVehicule() == null ? null : maintenance.getVehicule().getIdVehicule());
    }
}
