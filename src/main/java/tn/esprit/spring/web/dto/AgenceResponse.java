package tn.esprit.spring.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import tn.esprit.spring.domain.Agence;

@Schema(description = "Agence retournée par l'API")
public record AgenceResponse(
        @Schema(description = "Identifiant de l'agence", example = "1")
        Long idAgence,
        @Schema(description = "Nom de l'agence", example = "AutoLoc Tunis")
        String nom,
        @Schema(description = "Ville de l'agence", example = "Tunis")
        String ville,
        @Schema(description = "Adresse de l'agence", example = "12 avenue Habib Bourguiba")
        String adresse,
        @Schema(description = "Téléphone de l'agence", example = "+216 71 000 000")
        String telephone) {

    public static AgenceResponse from(Agence agence) {
        return new AgenceResponse(
                agence.getIdAgence(),
                agence.getNom(),
                agence.getVille(),
                agence.getAdresse(),
                agence.getTelephone());
    }
}
