package tn.esprit.spring.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import tn.esprit.spring.domain.Equipement;

@Schema(description = "Données de création ou de modification d'un équipement")
public record EquipementRequest(
        @NotBlank(message = "Le libellé est obligatoire.")
        @Size(max = 100, message = "Le libellé ne doit pas dépasser 100 caractères.")
        @Schema(description = "Libellé de l'équipement", example = "GPS")
        String libelle) {

    public Equipement toEntity() {
        Equipement equipement = new Equipement();
        equipement.setLibelle(libelle);
        return equipement;
    }
}
