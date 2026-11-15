package tn.esprit.spring.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import tn.esprit.spring.domain.ModePaiement;

@Schema(description = "Résultat d'un paiement simulé")
public record PaiementModeResponse(
        @Schema(description = "Mode de paiement utilisé", example = "CARTE")
        ModePaiement mode,
        @Schema(description = "Message retourné par le service de paiement", example = "Paiement par carte de 100")
        String message) {
}
