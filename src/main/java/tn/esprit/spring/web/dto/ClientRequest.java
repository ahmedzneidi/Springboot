package tn.esprit.spring.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import tn.esprit.spring.domain.Client;

import java.time.LocalDate;

@Schema(description = "Données de création ou de modification d'un client")
public record ClientRequest(
        @NotBlank(message = "Le nom est obligatoire.")
        @Size(max = 100, message = "Le nom ne doit pas dépasser 100 caractères.")
        @Schema(description = "Nom du client", example = "Zneidi")
        String nom,

        @NotBlank(message = "Le prénom est obligatoire.")
        @Size(max = 100, message = "Le prénom ne doit pas dépasser 100 caractères.")
        @Schema(description = "Prénom du client", example = "Ahmed")
        String prenom,

        @NotBlank(message = "L'email est obligatoire.")
        @Email(message = "L'email doit être valide.")
        @Size(max = 150, message = "L'email ne doit pas dépasser 150 caractères.")
        @Schema(description = "Adresse email", example = "ahmed.zneidi@example.com")
        String email,

        @Pattern(regexp = "^[0-9+ ]{8,20}$", message = "Le téléphone doit contenir entre 8 et 20 chiffres, espaces ou +.")
        @Schema(description = "Téléphone du client", example = "+216 20 000 000")
        String telephone,

        @NotBlank(message = "Le numéro de permis est obligatoire.")
        @Size(max = 50, message = "Le numéro de permis ne doit pas dépasser 50 caractères.")
        @Schema(description = "Numéro de permis de conduire", example = "PERMIS-2026-001")
        String numPermis,

        @NotNull(message = "La date d'inscription est obligatoire.")
        @PastOrPresent(message = "La date d'inscription ne peut pas être future.")
        @Schema(description = "Date d'inscription", example = "2026-11-15")
        LocalDate dateInscription) {

    public Client toEntity() {
        Client client = new Client();
        client.setNom(nom);
        client.setPrenom(prenom);
        client.setEmail(email);
        client.setTelephone(telephone);
        client.setNumPermis(numPermis);
        client.setDateInscription(dateInscription);
        return client;
    }
}
