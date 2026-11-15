package tn.esprit.spring.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
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
import tn.esprit.spring.service.IMaintenanceService;
import tn.esprit.spring.web.dto.ApiError;
import tn.esprit.spring.web.dto.MaintenanceRequest;
import tn.esprit.spring.web.dto.MaintenanceResponse;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/maintenances")
@Tag(name = "Maintenances", description = "Gestion des opérations de maintenance")
public class MaintenanceController {

    private final IMaintenanceService maintenanceService;

    public MaintenanceController(IMaintenanceService maintenanceService) {
        this.maintenanceService = maintenanceService;
    }

    @PostMapping
    @Operation(summary = "Créer une maintenance pour un véhicule")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Maintenance créée"),
            @ApiResponse(responseCode = "400", description = "Données invalides",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Véhicule introuvable",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<MaintenanceResponse> creer(@Valid @RequestBody MaintenanceRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(MaintenanceResponse.from(maintenanceService.creer(request)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Chercher une maintenance par identifiant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Maintenance trouvée"),
            @ApiResponse(responseCode = "404", description = "Maintenance introuvable",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public MaintenanceResponse obtenirParId(
            @Parameter(description = "Identifiant de la maintenance", example = "1")
            @PathVariable long id) {
        return MaintenanceResponse.from(maintenanceService.obtenirParId(id));
    }

    @GetMapping("/listall")
    @Operation(summary = "Lister les maintenances")
    public List<MaintenanceResponse> listerTous() {
        return maintenanceService.listerTous().stream().map(MaintenanceResponse::from).toList();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier une maintenance")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Maintenance modifiée"),
            @ApiResponse(responseCode = "400", description = "Données invalides",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Maintenance ou véhicule introuvable",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<MaintenanceResponse> modifier(
            @Parameter(description = "Identifiant de la maintenance", example = "1")
            @PathVariable long id,
            @Valid @RequestBody MaintenanceRequest request) {
        return ResponseEntity.ok(MaintenanceResponse.from(maintenanceService.modifier(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une maintenance")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Maintenance supprimée"),
            @ApiResponse(responseCode = "404", description = "Maintenance introuvable",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "409", description = "Suppression impossible",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<Void> supprimer(
            @Parameter(description = "Identifiant de la maintenance", example = "1")
            @PathVariable long id) {
        maintenanceService.supprimer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/vehicule/{idVehicule}")
    @Operation(summary = "Lister les maintenances d'un véhicule")
    public List<MaintenanceResponse> listerParVehicule(
            @Parameter(description = "Identifiant du véhicule", example = "1")
            @PathVariable long idVehicule) {
        return maintenanceService.listerParVehicule(idVehicule).stream()
                .map(MaintenanceResponse::from).toList();
    }

    @GetMapping("/encours")
    @Operation(summary = "Lister les maintenances en cours")
    public List<MaintenanceResponse> listerEnCours() {
        return maintenanceService.listerEnCours().stream().map(MaintenanceResponse::from).toList();
    }

    @PatchMapping("/{id}/vehicule/{idVehicule}")
    @Operation(summary = "Affecter une maintenance à un véhicule")
    public MaintenanceResponse affecterVehicule(
            @Parameter(description = "Identifiant de la maintenance", example = "1")
            @PathVariable long id,
            @Parameter(description = "Identifiant du véhicule", example = "1")
            @PathVariable long idVehicule) {
        return MaintenanceResponse.from(maintenanceService.affecterVehicule(id, idVehicule));
    }

    @DeleteMapping("/{id}/vehicule")
    @Operation(summary = "Désaffecter une maintenance de son véhicule")
    public MaintenanceResponse desaffecterVehicule(
            @Parameter(description = "Identifiant de la maintenance", example = "1")
            @PathVariable long id) {
        return MaintenanceResponse.from(maintenanceService.desaffecterVehicule(id));
    }

    @GetMapping("/vehicule/immatriculation/{immatriculation}")
    @Operation(summary = "Lister les maintenances d'un véhicule par immatriculation")
    public List<MaintenanceResponse> listerParImmatriculation(
            @Parameter(description = "Immatriculation", example = "TU-1234")
            @PathVariable String immatriculation) {
        return maintenanceService.listerParImmatriculation(immatriculation).stream()
                .map(MaintenanceResponse::from).toList();
    }

    @GetMapping("/dates")
    @Operation(summary = "Lister les maintenances commencées entre deux dates")
    public List<MaintenanceResponse> listerParDateDebutEntre(
            @Parameter(description = "Date de début", example = "2026-01-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @Parameter(description = "Date de fin", example = "2026-12-31")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return maintenanceService.listerParDateDebutEntre(debut, fin).stream()
                .map(MaintenanceResponse::from).toList();
    }

    @GetMapping("/terminees")
    @Operation(summary = "Lister les maintenances terminées")
    public List<MaintenanceResponse> listerTerminees() {
        return maintenanceService.listerTerminees().stream().map(MaintenanceResponse::from).toList();
    }

    @GetMapping("/vehicule/{idVehicule}/count")
    @Operation(summary = "Compter les maintenances d'un véhicule")
    public long compterParVehicule(
            @Parameter(description = "Identifiant du véhicule", example = "1")
            @PathVariable long idVehicule) {
        return maintenanceService.compterParVehicule(idVehicule);
    }
}
