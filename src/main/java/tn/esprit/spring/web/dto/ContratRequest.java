package tn.esprit.spring.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import tn.esprit.spring.domain.Contrat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Données de création ou de modification d'un contrat")
public record ContratRequest(
        @NotNull(message = "La date de signature est obligatoire.")
        @PastOrPresent(message = "La date de signature ne peut pas être future.")
        @Schema(description = "Date de signature du contrat", example = "2026-11-15")
        LocalDate dateSignature,

        @NotNull(message = "Le montant total est obligatoire.")
        @PositiveOrZero(message = "Le montant total doit être positif ou nul.")
        @Schema(description = "Montant total du contrat", example = "600.00")
        BigDecimal montantTotal,

        @NotNull(message = "Le statut de validité est obligatoire.")
        @Schema(description = "Indique si le contrat est valide", example = "true")
        Boolean valide) {

    public Contrat toEntity() {
        Contrat contrat = new Contrat();
        contrat.setDateSignature(dateSignature);
        contrat.setMontantTotal(montantTotal);
        contrat.setValide(Boolean.TRUE.equals(valide));
        return contrat;
    }
}
