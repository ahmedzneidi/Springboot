package tn.esprit.spring.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import tn.esprit.spring.domain.ModePaiement;
import tn.esprit.spring.domain.Paiement;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Données de création ou de modification d'un paiement")
public record PaiementRequest(
        @NotNull(message = "Le montant est obligatoire.")
        @Positive(message = "Le montant doit être strictement positif.")
        @Schema(description = "Montant du paiement", example = "100.50")
        BigDecimal montant,

        @NotNull(message = "La date de paiement est obligatoire.")
        @Schema(description = "Date du paiement", example = "2026-11-15")
        LocalDate datePaiement,

        @NotNull(message = "Le mode de paiement est obligatoire.")
        @Schema(description = "Mode de paiement", example = "CARTE")
        ModePaiement modePaiement) {

    public Paiement toEntity() {
        Paiement paiement = new Paiement();
        paiement.setMontant(montant);
        paiement.setDatePaiement(datePaiement);
        paiement.setModePaiement(modePaiement);
        return paiement;
    }
}
