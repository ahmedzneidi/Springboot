package tn.esprit.spring.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import tn.esprit.spring.domain.Employe;
import tn.esprit.spring.domain.RoleEmploye;

@Schema(description = "Employé retourné par l'API")
public record EmployeResponse(
        @Schema(description = "Identifiant de l'employé", example = "1")
        Long idEmploye,
        @Schema(description = "Nom de l'employé", example = "Zneidi")
        String nom,
        @Schema(description = "Prénom de l'employé", example = "Ahmed")
        String prenom,
        @Schema(description = "Rôle de l'employé", example = "MANAGER")
        RoleEmploye role,
        @Schema(description = "Identifiant de l'agence rattachée", example = "1")
        Long idAgence) {

    public static EmployeResponse from(Employe employe) {
        return new EmployeResponse(
                employe.getIdEmploye(),
                employe.getNom(),
                employe.getPrenom(),
                employe.getRole(),
                employe.getAgence() == null ? null : employe.getAgence().getIdAgence());
    }
}
