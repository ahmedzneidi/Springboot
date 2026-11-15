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
import tn.esprit.spring.domain.ModePaiement;
import tn.esprit.spring.service.IPaiementService;
import tn.esprit.spring.web.dto.ApiError;
import tn.esprit.spring.web.dto.PaiementRequest;
import tn.esprit.spring.web.dto.PaiementResponse;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/paiements")
@Tag(name = "Paiements", description = "Gestion des paiements")
public class PaiementController {

    private final IPaiementService paiementService;

    public PaiementController(IPaiementService paiementService) {
        this.paiementService = paiementService;
    }

    @PostMapping
    @Operation(summary = "Créer un paiement")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Paiement créé"),
            @ApiResponse(responseCode = "400", description = "Données invalides",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "409", description = "Conflit de données",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<PaiementResponse> creer(@Valid @RequestBody PaiementRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(PaiementResponse.from(paiementService.creer(request.toEntity())));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Chercher un paiement par identifiant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Paiement trouvé"),
            @ApiResponse(responseCode = "404", description = "Paiement introuvable",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public PaiementResponse obtenirParId(
            @Parameter(description = "Identifiant du paiement", example = "1")
            @PathVariable long id) {
        return PaiementResponse.from(paiementService.obtenirParId(id));
    }

    @GetMapping("/listall")
    @Operation(summary = "Lister les paiements")
    public List<PaiementResponse> listerTous() {
        return paiementService.listerTous().stream().map(PaiementResponse::from).toList();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un paiement")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Paiement modifié"),
            @ApiResponse(responseCode = "400", description = "Données invalides",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Paiement introuvable",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<PaiementResponse> modifier(
            @Parameter(description = "Identifiant du paiement", example = "1")
            @PathVariable long id,
            @Valid @RequestBody PaiementRequest request) {
        return ResponseEntity.ok(PaiementResponse.from(paiementService.modifier(id, request.toEntity())));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un paiement")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Paiement supprimé"),
            @ApiResponse(responseCode = "404", description = "Paiement introuvable",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "409", description = "Suppression impossible",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<Void> supprimer(
            @Parameter(description = "Identifiant du paiement", example = "1")
            @PathVariable long id) {
        paiementService.supprimer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/contrat/{idContrat}")
    @Operation(summary = "Lister les paiements d'un contrat")
    public List<PaiementResponse> listerParContrat(
            @Parameter(description = "Identifiant du contrat", example = "1")
            @PathVariable long idContrat) {
        return paiementService.listerParContrat(idContrat).stream().map(PaiementResponse::from).toList();
    }

    @GetMapping("/mode/{mode}")
    @Operation(summary = "Lister les paiements par mode")
    public List<PaiementResponse> listerParMode(
            @Parameter(description = "Mode de paiement", example = "CARTE")
            @PathVariable ModePaiement mode) {
        return paiementService.listerParMode(mode).stream().map(PaiementResponse::from).toList();
    }

    @GetMapping("/dates")
    @Operation(summary = "Lister les paiements effectués entre deux dates")
    public List<PaiementResponse> listerParDatePaiementEntre(
            @Parameter(description = "Date de début", example = "2026-01-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @Parameter(description = "Date de fin", example = "2026-12-31")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return paiementService.listerParDatePaiementEntre(debut, fin).stream()
                .map(PaiementResponse::from).toList();
    }

    @GetMapping("/dates/apres")
    @Operation(summary = "Lister les paiements effectués après une date")
    public List<PaiementResponse> listerParDatePaiementApres(
            @Parameter(description = "Date de référence", example = "2026-01-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return paiementService.listerParDatePaiementApres(date).stream()
                .map(PaiementResponse::from).toList();
    }

    @GetMapping("/contrat/{idContrat}/count")
    @Operation(summary = "Compter les paiements d'un contrat")
    public long compterParContrat(
            @Parameter(description = "Identifiant du contrat", example = "1")
            @PathVariable long idContrat) {
        return paiementService.compterParContrat(idContrat);
    }

    @PatchMapping("/{id}/contrat/{idContrat}")
    @Operation(summary = "Affecter un paiement à un contrat")
    public PaiementResponse affecterContrat(
            @Parameter(description = "Identifiant du paiement", example = "1")
            @PathVariable long id,
            @Parameter(description = "Identifiant du contrat", example = "1")
            @PathVariable long idContrat) {
        return PaiementResponse.from(paiementService.affecterContrat(id, idContrat));
    }

    @DeleteMapping("/{id}/contrat")
    @Operation(summary = "Désaffecter un paiement de son contrat")
    public PaiementResponse desaffecterContrat(
            @Parameter(description = "Identifiant du paiement", example = "1")
            @PathVariable long id) {
        return PaiementResponse.from(paiementService.desaffecterContrat(id));
    }
}
