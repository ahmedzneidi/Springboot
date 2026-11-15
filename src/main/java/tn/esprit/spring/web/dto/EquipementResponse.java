package tn.esprit.spring.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import tn.esprit.spring.domain.Equipement;

@Schema(description = "Équipement retourné par l'API")
public record EquipementResponse(
        @Schema(description = "Identifiant de l'équipement", example = "1")
        Long idEquipement,
        @Schema(description = "Libellé de l'équipement", example = "GPS")
        String libelle) {

    public static EquipementResponse from(Equipement equipement) {
        return new EquipementResponse(equipement.getIdEquipement(), equipement.getLibelle());
    }
}
