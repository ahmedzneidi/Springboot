package tn.esprit.spring.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import tn.esprit.spring.domain.ModePaiement;
import tn.esprit.spring.domain.Paiement;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Paiement retourné par l'API")
public record PaiementResponse(
        @Schema(description = "Identifiant du paiement", example = "1")
        Long idPaiement,
        @Schema(description = "Montant", example = "100.50")
        BigDecimal montant,
        @Schema(description = "Date du paiement", example = "2026-11-15")
        LocalDate datePaiement,
        @Schema(description = "Mode de paiement", example = "CARTE")
        ModePaiement modePaiement,
        @Schema(description = "Identifiant du contrat associé", example = "1")
        Long idContrat) {

    public static PaiementResponse from(Paiement paiement) {
        return new PaiementResponse(
                paiement.getIdPaiement(),
                paiement.getMontant(),
                paiement.getDatePaiement(),
                paiement.getModePaiement(),
                paiement.getContrat() == null ? null : paiement.getContrat().getIdContrat());
    }
}
