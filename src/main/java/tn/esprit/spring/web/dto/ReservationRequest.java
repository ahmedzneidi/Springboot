package tn.esprit.spring.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import tn.esprit.spring.domain.Reservation;
import tn.esprit.spring.domain.StatutReservation;

import java.time.LocalDate;

@Schema(description = "Données de création ou de modification d'une réservation")
public record ReservationRequest(
        @NotNull(message = "La date de début est obligatoire.")
        @Schema(description = "Date de début de la réservation", example = "2026-11-20")
        LocalDate dateDebut,

        @NotNull(message = "La date de fin est obligatoire.")
        @Schema(description = "Date de fin de la réservation", example = "2026-11-25")
        LocalDate dateFin,

        @NotNull(message = "Le statut est obligatoire.")
        @Schema(description = "Statut de la réservation", example = "EN_ATTENTE")
        StatutReservation statut) {

    @JsonIgnore
    @AssertTrue(message = "La date de fin doit être postérieure ou égale à la date de début.")
    public boolean isPeriodeValide() {
        return dateDebut == null || dateFin == null || !dateFin.isBefore(dateDebut);
    }

    public Reservation toEntity() {
        Reservation reservation = new Reservation();
        reservation.setDateDebut(dateDebut);
        reservation.setDateFin(dateFin);
        reservation.setStatut(statut);
        return reservation;
    }
}
