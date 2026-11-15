package tn.esprit.spring.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import tn.esprit.spring.domain.Contrat;
import tn.esprit.spring.domain.Paiement;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Schema(description = "Contrat retourné par l'API")
public record ContratResponse(
        @Schema(description = "Identifiant du contrat", example = "1")
        Long idContrat,
        @Schema(description = "Date de signature", example = "2026-11-15")
        LocalDate dateSignature,
        @Schema(description = "Montant total", example = "600.00")
        BigDecimal montantTotal,
        @Schema(description = "Contrat valide", example = "true")
        boolean valide,
        @Schema(description = "Identifiant de la réservation", example = "1")
        Long idReservation,
        @Schema(description = "Identifiants des paiements associés")
        List<Long> idPaiements) {

    public static ContratResponse from(Contrat contrat) {
        List<Long> idPaiements = contrat.getPaiements() == null
                ? List.of()
                : contrat.getPaiements().stream().map(Paiement::getIdPaiement).toList();
        return new ContratResponse(
                contrat.getIdContrat(),
                contrat.getDateSignature(),
                contrat.getMontantTotal(),
                contrat.isValide(),
                contrat.getReservation() == null ? null : contrat.getReservation().getIdReservation(),
                idPaiements);
    }
}
