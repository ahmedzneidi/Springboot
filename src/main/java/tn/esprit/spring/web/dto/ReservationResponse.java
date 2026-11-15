package tn.esprit.spring.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import tn.esprit.spring.domain.Reservation;
import tn.esprit.spring.domain.StatutReservation;

import java.time.LocalDate;

@Schema(description = "Réservation retournée par l'API")
public record ReservationResponse(
        @Schema(description = "Identifiant de la réservation", example = "1")
        Long idReservation,
        @Schema(description = "Date de début", example = "2026-11-20")
        LocalDate dateDebut,
        @Schema(description = "Date de fin", example = "2026-11-25")
        LocalDate dateFin,
        @Schema(description = "Statut", example = "EN_ATTENTE")
        StatutReservation statut,
        @Schema(description = "Identifiant du client", example = "1")
        Long idClient,
        @Schema(description = "Identifiant du véhicule", example = "1")
        Long idVehicule,
        @Schema(description = "Identifiant du contrat associé", example = "1")
        Long idContrat) {

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getIdReservation(),
                reservation.getDateDebut(),
                reservation.getDateFin(),
                reservation.getStatut(),
                reservation.getClient() == null ? null : reservation.getClient().getIdClient(),
                reservation.getVehicule() == null ? null : reservation.getVehicule().getIdVehicule(),
                reservation.getContrat() == null ? null : reservation.getContrat().getIdContrat());
    }
}
