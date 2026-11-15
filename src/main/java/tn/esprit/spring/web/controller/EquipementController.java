package tn.esprit.spring.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.spring.service.IEquipementService;
import tn.esprit.spring.web.dto.ApiError;
import tn.esprit.spring.web.dto.EquipementRequest;
import tn.esprit.spring.web.dto.EquipementResponse;

import java.util.List;

@RestController
@RequestMapping("/equipements")
@Tag(name = "Équipements", description = "Gestion des équipements")
public class EquipementController {

    private final IEquipementService equipementService;

    public EquipementController(IEquipementService equipementService) {
        this.equipementService = equipementService;
    }

    @PostMapping
    @Operation(summary = "Créer un équipement")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Équipement créé"),
            @ApiResponse(responseCode = "400", description = "Données invalides",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "409", description = "Conflit de données",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<EquipementResponse> creer(@Valid @RequestBody EquipementRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(EquipementResponse.from(equipementService.creer(request.toEntity())));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Chercher un équipement par identifiant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Équipement trouvé"),
            @ApiResponse(responseCode = "404", description = "Équipement introuvable",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public EquipementResponse obtenirParId(
            @Parameter(description = "Identifiant de l'équipement", example = "1")
            @PathVariable long id) {
        return EquipementResponse.from(equipementService.obtenirParId(id));
    }

    @GetMapping("/listall")
    @Operation(summary = "Lister les équipements")
    public List<EquipementResponse> listerTous() {
        return equipementService.listerTous().stream().map(EquipementResponse::from).toList();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un équipement")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Équipement modifié"),
            @ApiResponse(responseCode = "400", description = "Données invalides",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Équipement introuvable",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<EquipementResponse> modifier(
            @Parameter(description = "Identifiant de l'équipement", example = "1")
            @PathVariable long id,
            @Valid @RequestBody EquipementRequest request) {
        return ResponseEntity.ok(EquipementResponse.from(equipementService.modifier(id, request.toEntity())));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un équipement")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Équipement supprimé"),
            @ApiResponse(responseCode = "404", description = "Équipement introuvable",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "409", description = "Suppression impossible",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<Void> supprimer(
            @Parameter(description = "Identifiant de l'équipement", example = "1")
            @PathVariable long id) {
        equipementService.supprimer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/vehicule/{idVehicule}")
    @Operation(summary = "Lister les équipements d'un véhicule")
    public List<EquipementResponse> listerParVehicule(
            @Parameter(description = "Identifiant du véhicule", example = "1")
            @PathVariable long idVehicule) {
        return equipementService.listerParVehicule(idVehicule).stream()
                .map(EquipementResponse::from).toList();
    }

    @GetMapping("/recherche/libelle/{libelle}")
    @Operation(summary = "Rechercher un équipement par libellé exact")
    public EquipementResponse rechercherParLibelle(
            @Parameter(description = "Libellé exact", example = "GPS")
            @PathVariable String libelle) {
        return EquipementResponse.from(equipementService.rechercherParLibelle(libelle)
                .orElseThrow(() -> new EntityNotFoundException("Equipement introuvable avec le libellé " + libelle)));
    }

    @GetMapping("/recherche")
    @Operation(summary = "Rechercher des équipements par libellé")
    public List<EquipementResponse> rechercherParLibelleContenant(
            @Parameter(description = "Fragment du libellé", example = "GP")
            @RequestParam String q) {
        return equipementService.rechercherParLibelleContenant(q).stream()
                .map(EquipementResponse::from).toList();
    }

    @GetMapping("/existe/{libelle}")
    @Operation(summary = "Vérifier l'existence d'un équipement par libellé")
    public boolean existeParLibelle(
            @Parameter(description = "Libellé exact", example = "GPS")
            @PathVariable String libelle) {
        return equipementService.existeParLibelle(libelle);
    }
}
