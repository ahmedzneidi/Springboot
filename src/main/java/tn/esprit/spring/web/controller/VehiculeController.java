package tn.esprit.spring.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.spring.domain.CategorieVehicule;
import tn.esprit.spring.domain.StatutVehicule;
import tn.esprit.spring.service.IVehiculeService;
import tn.esprit.spring.web.dto.ApiError;
import tn.esprit.spring.web.dto.EquipementResponse;
import tn.esprit.spring.web.dto.VehiculeRequest;
import tn.esprit.spring.web.dto.VehiculeResponse;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/vehicules")
@Tag(name = "Véhicules", description = "Gestion de la flotte de véhicules")
public class VehiculeController {

    private final IVehiculeService vehiculeService;

    public VehiculeController(IVehiculeService vehiculeService) {
        this.vehiculeService = vehiculeService;
    }

    @PostMapping
    @Operation(summary = "Créer un véhicule")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Véhicule créé"),
            @ApiResponse(responseCode = "400", description = "Données invalides",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "409", description = "Immatriculation déjà utilisée",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<VehiculeResponse> creer(@Valid @RequestBody VehiculeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(VehiculeResponse.from(vehiculeService.creer(request.toEntity())));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Chercher un véhicule par identifiant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Véhicule trouvé"),
            @ApiResponse(responseCode = "404", description = "Véhicule introuvable",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public VehiculeResponse obtenirParId(
            @Parameter(description = "Identifiant du véhicule", example = "1")
            @PathVariable long id) {
        return VehiculeResponse.from(vehiculeService.obtenirParId(id));
    }

    @GetMapping("/listall")
    @Operation(summary = "Lister les véhicules")
    public List<VehiculeResponse> listerTous() {
        return vehiculeService.listerTous().stream().map(VehiculeResponse::from).toList();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un véhicule")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Véhicule modifié"),
            @ApiResponse(responseCode = "400", description = "Données invalides",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Véhicule introuvable",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "409", description = "Immatriculation déjà utilisée",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<VehiculeResponse> modifier(
            @Parameter(description = "Identifiant du véhicule", example = "1")
            @PathVariable long id,
            @Valid @RequestBody VehiculeRequest request) {
        return ResponseEntity.ok(VehiculeResponse.from(vehiculeService.modifier(id, request.toEntity())));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un véhicule")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Véhicule supprimé"),
            @ApiResponse(responseCode = "404", description = "Véhicule introuvable",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "409", description = "Suppression impossible",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<Void> supprimer(
            @Parameter(description = "Identifiant du véhicule", example = "1")
            @PathVariable long id) {
        vehiculeService.supprimer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/disponibles")
    @Operation(summary = "Lister les véhicules disponibles")
    public List<VehiculeResponse> listerDisponibles() {
        return vehiculeService.listerDisponibles().stream().map(VehiculeResponse::from).toList();
    }

    @GetMapping("/agence/{idAgence}")
    @Operation(summary = "Lister les véhicules d'une agence")
    public List<VehiculeResponse> listerParAgence(
            @Parameter(description = "Identifiant de l'agence", example = "1")
            @PathVariable long idAgence) {
        return vehiculeService.listerParAgence(idAgence).stream().map(VehiculeResponse::from).toList();
    }

    @PatchMapping("/{idVehicule}/agence/{idAgence}")
    @Operation(summary = "Affecter un véhicule à une agence")
    public VehiculeResponse affecterAgence(
            @Parameter(description = "Identifiant du véhicule", example = "1")
            @PathVariable long idVehicule,
            @Parameter(description = "Identifiant de l'agence", example = "1")
            @PathVariable long idAgence) {
        return VehiculeResponse.from(vehiculeService.affecterAgence(idVehicule, idAgence));
    }

    @DeleteMapping("/{idVehicule}/agence")
    @Operation(summary = "Désaffecter un véhicule de son agence")
    public VehiculeResponse desaffecterAgence(
            @Parameter(description = "Identifiant du véhicule", example = "1")
            @PathVariable long idVehicule) {
        return VehiculeResponse.from(vehiculeService.desaffecterAgence(idVehicule));
    }

    @PatchMapping("/{idVehicule}/equipements/{idEquipement}")
    @Operation(summary = "Affecter un équipement à un véhicule")
    public VehiculeResponse affecterEquipement(
            @Parameter(description = "Identifiant du véhicule", example = "1")
            @PathVariable long idVehicule,
            @Parameter(description = "Identifiant de l'équipement", example = "1")
            @PathVariable long idEquipement) {
        return VehiculeResponse.from(vehiculeService.affecterEquipement(idVehicule, idEquipement));
    }

    @DeleteMapping("/{idVehicule}/equipements/{idEquipement}")
    @Operation(summary = "Désaffecter un équipement d'un véhicule")
    public VehiculeResponse desaffecterEquipement(
            @Parameter(description = "Identifiant du véhicule", example = "1")
            @PathVariable long idVehicule,
            @Parameter(description = "Identifiant de l'équipement", example = "1")
            @PathVariable long idEquipement) {
        return VehiculeResponse.from(vehiculeService.desaffecterEquipement(idVehicule, idEquipement));
    }

    @GetMapping("/{id}/equipements")
    @Operation(summary = "Lister les équipements d'un véhicule")
    public List<EquipementResponse> listerEquipements(
            @Parameter(description = "Identifiant du véhicule", example = "1")
            @PathVariable long id) {
        return vehiculeService.listerEquipements(id).stream().map(EquipementResponse::from).toList();
    }

    @GetMapping("/immatriculation/{immatriculation}")
    @Operation(summary = "Chercher un véhicule par immatriculation")
    public VehiculeResponse rechercherParImmatriculation(
            @Parameter(description = "Immatriculation", example = "TU-1234")
            @PathVariable String immatriculation) {
        return VehiculeResponse.from(vehiculeService.rechercherParImmatriculation(immatriculation));
    }

    @GetMapping("/existe/{immatriculation}")
    @Operation(summary = "Vérifier l'existence d'une immatriculation")
    public boolean existeParImmatriculation(
            @Parameter(description = "Immatriculation", example = "TU-1234")
            @PathVariable String immatriculation) {
        return vehiculeService.existeParImmatriculation(immatriculation);
    }

    @GetMapping("/categorie/{categorie}/statut/{statut}")
    @Operation(summary = "Lister les véhicules par catégorie et statut")
    public List<VehiculeResponse> listerParCategorieEtStatut(
            @Parameter(description = "Catégorie", example = "CITADINE")
            @PathVariable CategorieVehicule categorie,
            @Parameter(description = "Statut", example = "DISPONIBLE")
            @PathVariable StatutVehicule statut) {
        return vehiculeService.listerParCategorieEtStatut(categorie, statut).stream()
                .map(VehiculeResponse::from).toList();
    }

    @GetMapping("/marque/{marque}")
    @Operation(summary = "Rechercher des véhicules par marque")
    public List<VehiculeResponse> rechercherParMarque(
            @Parameter(description = "Marque", example = "Renault")
            @PathVariable String marque) {
        return vehiculeService.rechercherParMarque(marque).stream().map(VehiculeResponse::from).toList();
    }

    @GetMapping("/tarif")
    @Operation(summary = "Lister les véhicules dans une plage de tarif")
    public List<VehiculeResponse> listerParTarif(
            @Parameter(description = "Tarif minimum", example = "50")
            @RequestParam BigDecimal min,
            @Parameter(description = "Tarif maximum", example = "200")
            @RequestParam BigDecimal max) {
        return vehiculeService.listerParTarif(min, max).stream().map(VehiculeResponse::from).toList();
    }

    @GetMapping("/ville/{ville}/disponibles")
    @Operation(summary = "Lister les véhicules disponibles dans une ville")
    public List<VehiculeResponse> listerParVilleEtStatut(
            @Parameter(description = "Ville", example = "Tunis")
            @PathVariable String ville) {
        return vehiculeService.listerParVilleEtStatut(ville, StatutVehicule.DISPONIBLE).stream()
                .map(VehiculeResponse::from).toList();
    }

    @GetMapping("/equipement/{libelle}")
    @Operation(summary = "Lister les véhicules possédant un équipement")
    public List<VehiculeResponse> listerParEquipement(
            @Parameter(description = "Libellé de l'équipement", example = "GPS")
            @PathVariable String libelle) {
        return vehiculeService.listerParEquipement(libelle).stream().map(VehiculeResponse::from).toList();
    }

    @GetMapping("/sans-equipement")
    @Operation(summary = "Lister les véhicules sans équipement")
    public List<VehiculeResponse> listerSansEquipement() {
        return vehiculeService.listerSansEquipement().stream().map(VehiculeResponse::from).toList();
    }

    @GetMapping("/statut/{statut}/count")
    @Operation(summary = "Compter les véhicules par statut")
    public long compterParStatut(
            @Parameter(description = "Statut", example = "DISPONIBLE")
            @PathVariable StatutVehicule statut) {
        return vehiculeService.compterParStatut(statut);
    }
}
