package tn.esprit.spring.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import tn.esprit.spring.domain.Client;

import java.time.LocalDate;

@Schema(description = "Client retourné par l'API")
public record ClientResponse(
        @Schema(description = "Identifiant du client", example = "1")
        Long idClient,
        @Schema(description = "Nom du client", example = "Zneidi")
        String nom,
        @Schema(description = "Prénom du client", example = "Ahmed")
        String prenom,
        @Schema(description = "Email du client", example = "ahmed.zneidi@example.com")
        String email,
        @Schema(description = "Téléphone du client", example = "+216 20 000 000")
        String telephone,
        @Schema(description = "Numéro de permis", example = "PERMIS-2026-001")
        String numPermis,
        @Schema(description = "Date d'inscription", example = "2026-11-15")
        LocalDate dateInscription) {

    public static ClientResponse from(Client client) {
        return new ClientResponse(
                client.getIdClient(),
                client.getNom(),
                client.getPrenom(),
                client.getEmail(),
                client.getTelephone(),
                client.getNumPermis(),
                client.getDateInscription());
    }
}
