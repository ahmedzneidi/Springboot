package tn.esprit.spring.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import tn.esprit.spring.domain.Employe;
import tn.esprit.spring.domain.RoleEmploye;

@Schema(description = "Données de création ou de modification d'un employé")
public record EmployeRequest(
        @NotBlank(message = "Le nom est obligatoire.")
        @Size(max = 100, message = "Le nom ne doit pas dépasser 100 caractères.")
        @Schema(description = "Nom de l'employé", example = "Zneidi")
        String nom,

        @NotBlank(message = "Le prénom est obligatoire.")
        @Size(max = 100, message = "Le prénom ne doit pas dépasser 100 caractères.")
        @Schema(description = "Prénom de l'employé", example = "Ahmed")
        String prenom,

        @NotNull(message = "Le rôle est obligatoire.")
        @Schema(description = "Rôle de l'employé", example = "MANAGER")
        RoleEmploye role) {

    public Employe toEntity() {
        Employe employe = new Employe();
        employe.setNom(nom);
        employe.setPrenom(prenom);
        employe.setRole(role);
        return employe;
    }
}
