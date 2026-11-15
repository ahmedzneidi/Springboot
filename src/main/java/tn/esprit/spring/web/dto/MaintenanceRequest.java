package tn.esprit.spring.web.dto;

import java.time.LocalDate;

public record MaintenanceRequest(
        LocalDate dateDebut,
        LocalDate dateFin,
        String description,
        Long idVehicule) {
}
