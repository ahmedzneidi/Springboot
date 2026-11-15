package tn.esprit.spring.web.controller;

import io.swagger.v3.oas.annotations.Hidden;
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
import tn.esprit.spring.domain.RoleEmploye;
import tn.esprit.spring.service.IEmployeService;
import tn.esprit.spring.web.dto.ApiError;
import tn.esprit.spring.web.dto.EmployeRequest;
import tn.esprit.spring.web.dto.EmployeResponse;

import java.util.List;

@RestController
@RequestMapping("/employes")
@Tag(name = "Employés", description = "Gestion des employés des agences")
public class EmployeController {

    private final IEmployeService employeService;

    public EmployeController(IEmployeService employeService) {
        this.employeService = employeService;
    }

    @Hidden
    @GetMapping("/test")
    @Operation(summary = "Tester le contrôleur")
    public String testerController() {
        return "Le contrôleur Employe fonctionne.";
    }

    @PostMapping
    @Operation(summary = "Créer un employé")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Employé créé"),
            @ApiResponse(responseCode = "400", description = "Données invalides",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "409", description = "Conflit de données",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<EmployeResponse> creer(@Valid @RequestBody EmployeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(EmployeResponse.from(employeService.creer(request.toEntity())));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Chercher un employé par identifiant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employé trouvé"),
            @ApiResponse(responseCode = "404", description = "Employé introuvable",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public EmployeResponse obtenirParId(
            @Parameter(description = "Identifiant de l'employé", example = "1")
            @PathVariable long id) {
        return EmployeResponse.from(employeService.obtenirParId(id));
    }

    @GetMapping("/listall")
    @Operation(summary = "Lister les employés")
    public List<EmployeResponse> listerTous() {
        return employeService.listerTous().stream().map(EmployeResponse::from).toList();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un employé")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employé modifié"),
            @ApiResponse(responseCode = "400", description = "Données invalides",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Employé introuvable",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<EmployeResponse> modifier(
            @Parameter(description = "Identifiant de l'employé", example = "1")
            @PathVariable long id,
            @Valid @RequestBody EmployeRequest request) {
        return ResponseEntity.ok(EmployeResponse.from(employeService.modifier(id, request.toEntity())));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un employé")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Employé supprimé"),
            @ApiResponse(responseCode = "404", description = "Employé introuvable",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "409", description = "Suppression impossible",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<Void> supprimer(
            @Parameter(description = "Identifiant de l'employé", example = "1")
            @PathVariable long id) {
        employeService.supprimer(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{idEmploye}/agence/{idAgence}")
    @Operation(summary = "Affecter un employé à une agence")
    public EmployeResponse affecterAgence(
            @Parameter(description = "Identifiant de l'employé", example = "1")
            @PathVariable long idEmploye,
            @Parameter(description = "Identifiant de l'agence", example = "1")
            @PathVariable long idAgence) {
        return EmployeResponse.from(employeService.affecterAgence(idEmploye, idAgence));
    }

    @DeleteMapping("/{idEmploye}/agence")
    @Operation(summary = "Désaffecter un employé de son agence")
    public EmployeResponse desaffecterAgence(
            @Parameter(description = "Identifiant de l'employé", example = "1")
            @PathVariable long idEmploye) {
        return EmployeResponse.from(employeService.desaffecterAgence(idEmploye));
    }

    @GetMapping("/agence/{idAgence}")
    @Operation(summary = "Lister les employés d'une agence")
    public List<EmployeResponse> listerParAgence(
            @Parameter(description = "Identifiant de l'agence", example = "1")
            @PathVariable long idAgence) {
        return employeService.listerParAgence(idAgence).stream().map(EmployeResponse::from).toList();
    }

    @GetMapping("/agence/{idAgence}/count")
    @Operation(summary = "Compter les employés d'une agence")
    public long compterParAgence(
            @Parameter(description = "Identifiant de l'agence", example = "1")
            @PathVariable long idAgence) {
        return employeService.compterParAgence(idAgence);
    }

    @GetMapping("/role/{role}")
    @Operation(summary = "Lister les employés par rôle")
    public List<EmployeResponse> listerParRole(
            @Parameter(description = "Rôle recherché", example = "MANAGER")
            @PathVariable RoleEmploye role) {
        return employeService.listerParRole(role).stream().map(EmployeResponse::from).toList();
    }

    @GetMapping("/recherche")
    @Operation(summary = "Rechercher des employés par nom ou prénom")
    public List<EmployeResponse> rechercherParNom(
            @Parameter(description = "Nom ou prénom recherché", example = "Ahmed")
            @RequestParam String q) {
        return employeService.rechercherParNom(q).stream().map(EmployeResponse::from).toList();
    }

    @GetMapping("/recherche/exact")
    @Operation(summary = "Rechercher un employé par nom et prénom exacts")
    public EmployeResponse rechercherParNomEtPrenom(
            @Parameter(description = "Nom exact", example = "Zneidi")
            @RequestParam String nom,
            @Parameter(description = "Prénom exact", example = "Ahmed")
            @RequestParam String prenom) {
        return EmployeResponse.from(employeService.rechercherParNomEtPrenom(nom, prenom));
    }

    @GetMapping("/existe")
    @Operation(summary = "Vérifier l'existence d'un employé par nom et prénom")
    public boolean existeParNomEtPrenom(
            @Parameter(description = "Nom exact", example = "Zneidi")
            @RequestParam String nom,
            @Parameter(description = "Prénom exact", example = "Ahmed")
            @RequestParam String prenom) {
        return employeService.existeParNomEtPrenom(nom, prenom);
    }
}
