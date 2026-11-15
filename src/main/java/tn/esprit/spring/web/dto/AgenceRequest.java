package tn.esprit.spring.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import tn.esprit.spring.domain.Agence;

@Schema(description = "Données de création ou de modification d'une agence")
public record AgenceRequest(
        @NotBlank(message = "Le nom est obligatoire.")
        @Size(max = 100, message = "Le nom ne doit pas dépasser 100 caractères.")
        @Schema(description = "Nom de l'agence", example = "AutoLoc Tunis")
        String nom,

        @NotBlank(message = "La ville est obligatoire.")
        @Size(max = 50, message = "La ville ne doit pas dépasser 50 caractères.")
        @Schema(description = "Ville de l'agence", example = "Tunis")
        String ville,

        @Size(max = 255, message = "L'adresse ne doit pas dépasser 255 caractères.")
        @Schema(description = "Adresse complète", example = "12 avenue Habib Bourguiba")
        String adresse,

        @Pattern(regexp = "^[0-9+ ]{8,20}$", message = "Le téléphone doit contenir entre 8 et 20 chiffres, espaces ou +.")
        @Schema(description = "Téléphone de l'agence", example = "+216 71 000 000")
        String telephone) {

    public Agence toEntity() {
        Agence agence = new Agence();
        agence.setNom(nom);
        agence.setVille(ville);
        agence.setAdresse(adresse);
        agence.setTelephone(telephone);
        return agence;
    }
}
