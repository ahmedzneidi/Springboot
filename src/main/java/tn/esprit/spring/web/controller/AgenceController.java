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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.spring.service.IAgenceService;
import tn.esprit.spring.web.dto.AgenceRequest;
import tn.esprit.spring.web.dto.AgenceResponse;
import tn.esprit.spring.web.dto.ApiError;
import tn.esprit.spring.web.dto.EmployeResponse;
import tn.esprit.spring.web.dto.VehiculeResponse;

import java.util.List;

@RestController
@RequestMapping("/agences")
@Tag(name = "Agences", description = "Gestion des agences de location")
public class AgenceController {

    private final IAgenceService agenceService;

    public AgenceController(IAgenceService agenceService) {
        this.agenceService = agenceService;
    }

    @PostMapping
    @Operation(summary = "Créer une agence", description = "Crée une agence et retourne la ressource créée.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Agence créée"),
            @ApiResponse(responseCode = "400", description = "Données invalides",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "409", description = "Conflit de données",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<AgenceResponse> creer(@Valid @RequestBody AgenceRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(AgenceResponse.from(agenceService.creer(request.toEntity())));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Chercher une agence par identifiant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Agence trouvée"),
            @ApiResponse(responseCode = "404", description = "Agence introuvable",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public AgenceResponse obtenirParId(
            @Parameter(description = "Identifiant de l'agence", example = "1")
            @PathVariable long id) {
        return AgenceResponse.from(agenceService.obtenirParId(id));
    }

    @GetMapping("/listall")
    @Operation(summary = "Lister les agences")
    public List<AgenceResponse> listerTous() {
        return agenceService.listerTous().stream().map(AgenceResponse::from).toList();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier une agence")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Agence modifiée"),
            @ApiResponse(responseCode = "400", description = "Données invalides",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Agence introuvable",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<AgenceResponse> modifier(
            @Parameter(description = "Identifiant de l'agence", example = "1")
            @PathVariable long id,
            @Valid @RequestBody AgenceRequest request) {
        return ResponseEntity.ok(AgenceResponse.from(agenceService.modifier(id, request.toEntity())));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une agence")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Agence supprimée"),
            @ApiResponse(responseCode = "404", description = "Agence introuvable",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "409", description = "Suppression impossible",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<Void> supprimer(
            @Parameter(description = "Identifiant de l'agence", example = "1")
            @PathVariable long id) {
        agenceService.supprimer(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/employes/{idEmploye}")
    @Operation(summary = "Affecter un employé à une agence")
    public EmployeResponse affecterEmploye(
            @Parameter(description = "Identifiant de l'agence", example = "1")
            @PathVariable long id,
            @Parameter(description = "Identifiant de l'employé", example = "1")
            @PathVariable long idEmploye) {
        return EmployeResponse.from(agenceService.affecterEmploye(id, idEmploye));
    }

    @DeleteMapping("/{id}/employes/{idEmploye}")
    @Operation(summary = "Désaffecter un employé de son agence")
    public ResponseEntity<Void> desaffecterEmploye(
            @Parameter(description = "Identifiant de l'agence", example = "1")
            @PathVariable long id,
            @Parameter(description = "Identifiant de l'employé", example = "1")
            @PathVariable long idEmploye) {
        agenceService.desaffecterEmploye(idEmploye);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/vehicules/{idVehicule}")
    @Operation(summary = "Affecter un véhicule à une agence")
    public VehiculeResponse affecterVehicule(
            @Parameter(description = "Identifiant de l'agence", example = "1")
            @PathVariable long id,
            @Parameter(description = "Identifiant du véhicule", example = "1")
            @PathVariable long idVehicule) {
        return VehiculeResponse.from(agenceService.affecterVehicule(id, idVehicule));
    }

    @DeleteMapping("/{id}/vehicules/{idVehicule}")
    @Operation(summary = "Désaffecter un véhicule de son agence")
    public ResponseEntity<Void> desaffecterVehicule(
            @Parameter(description = "Identifiant de l'agence", example = "1")
            @PathVariable long id,
            @Parameter(description = "Identifiant du véhicule", example = "1")
            @PathVariable long idVehicule) {
        agenceService.desaffecterVehicule(idVehicule);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/recherche/nom/{nom}")
    @Operation(summary = "Rechercher une agence par nom exact")
    public AgenceResponse rechercherParNom(
            @Parameter(description = "Nom de l'agence", example = "AutoLoc Tunis")
            @PathVariable String nom) {
        return AgenceResponse.from(agenceService.rechercherParNom(nom)
                .orElseThrow(() -> new EntityNotFoundException("Agence introuvable avec le nom " + nom)));
    }

    @GetMapping("/recherche")
    @Operation(summary = "Rechercher des agences par nom")
    public List<AgenceResponse> rechercherParNomContenant(
            @Parameter(description = "Fragment du nom", example = "Auto")
            @RequestParam String q) {
        return agenceService.rechercherParNomContenant(q).stream().map(AgenceResponse::from).toList();
    }

    @GetMapping("/ville/{ville}")
    @Operation(summary = "Lister les agences d'une ville")
    public List<AgenceResponse> listerParVille(
            @Parameter(description = "Ville recherchée", example = "Tunis")
            @PathVariable String ville) {
        return agenceService.rechercherParVille(ville).stream().map(AgenceResponse::from).toList();
    }

    @GetMapping("/ville/{ville}/telephone")
    @Operation(summary = "Lister les agences d'une ville ayant un téléphone")
    public List<AgenceResponse> listerParVilleAvecTelephone(
            @Parameter(description = "Ville recherchée", example = "Tunis")
            @PathVariable String ville) {
        return agenceService.listerParVilleAvecTelephone(ville).stream().map(AgenceResponse::from).toList();
    }

    @GetMapping("/ville/{ville}/count")
    @Operation(summary = "Compter les agences d'une ville")
    public long compterParVille(
            @Parameter(description = "Ville recherchée", example = "Tunis")
            @PathVariable String ville) {
        return agenceService.compterParVille(ville);
    }

    @GetMapping("/existe/{nom}")
    @Operation(summary = "Vérifier l'existence d'une agence par nom")
    public boolean existeParNom(
            @Parameter(description = "Nom de l'agence", example = "AutoLoc Tunis")
            @PathVariable String nom) {
        return agenceService.existeParNom(nom);
    }
}
