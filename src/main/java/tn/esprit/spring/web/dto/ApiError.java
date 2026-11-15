package tn.esprit.spring.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.Map;

@Schema(description = "Réponse d'erreur normalisée de l'API")
public record ApiError(
        @Schema(description = "Horodatage de l'erreur", example = "2026-11-15T20:22:06Z")
        Instant timestamp,
        @Schema(description = "Code HTTP", example = "400")
        int status,
        @Schema(description = "Libellé HTTP", example = "Bad Request")
        String error,
        @Schema(description = "Message lisible", example = "Validation échouée")
        String message,
        @Schema(description = "Chemin de la requête", example = "/clients")
        String path,
        @Schema(description = "Erreurs par champ")
        Map<String, String> fieldErrors) {
}
