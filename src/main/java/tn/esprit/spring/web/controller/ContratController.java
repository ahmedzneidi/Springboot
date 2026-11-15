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
import tn.esprit.spring.service.IContratService;
import tn.esprit.spring.web.dto.ApiError;
import tn.esprit.spring.web.dto.ContratRequest;
import tn.esprit.spring.web.dto.ContratResponse;
import tn.esprit.spring.web.dto.PaiementResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/contrats")
@Tag(name = "Contrats", description = "Gestion des contrats de location")
public class ContratController {

    private final IContratService contratService;

    public ContratController(IContratService contratService) {
        this.contratService = contratService;
    }

    @PostMapping
    @Operation(summary = "Créer un contrat")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Contrat créé"),
            @ApiResponse(responseCode = "400", description = "Données invalides",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "409", description = "Conflit de données",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<ContratResponse> creer(@Valid @RequestBody ContratRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ContratResponse.from(contratService.creer(request.toEntity())));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Chercher un contrat par identifiant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contrat trouvé"),
            @ApiResponse(responseCode = "404", description = "Contrat introuvable",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ContratResponse obtenirParId(
            @Parameter(description = "Identifiant du contrat", example = "1")
            @PathVariable long id) {
        return ContratResponse.from(contratService.obtenirParId(id));
    }

    @GetMapping("/listall")
    @Operation(summary = "Lister les contrats")
    public List<ContratResponse> listerTous() {
        return contratService.listerTous().stream().map(ContratResponse::from).toList();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un contrat")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contrat modifié"),
            @ApiResponse(responseCode = "400", description = "Données invalides",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Contrat introuvable",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<ContratResponse> modifier(
            @Parameter(description = "Identifiant du contrat", example = "1")
            @PathVariable long id,
            @Valid @RequestBody ContratRequest request) {
        return ResponseEntity.ok(ContratResponse.from(contratService.modifier(id, request.toEntity())));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un contrat")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Contrat supprimé"),
            @ApiResponse(responseCode = "404", description = "Contrat introuvable",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "409", description = "Suppression impossible",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<Void> supprimer(
            @Parameter(description = "Identifiant du contrat", example = "1")
            @PathVariable long id) {
        contratService.supprimer(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/reservation/{idReservation}")
    @Operation(summary = "Affecter une réservation à un contrat")
    public ContratResponse affecterReservation(
            @Parameter(description = "Identifiant du contrat", example = "1")
            @PathVariable long id,
            @Parameter(description = "Identifiant de la réservation", example = "1")
            @PathVariable long idReservation) {
        return ContratResponse.from(contratService.affecterReservation(id, idReservation));
    }

    @DeleteMapping("/{id}/reservation")
    @Operation(summary = "Désaffecter la réservation d'un contrat")
    public ContratResponse desaffecterReservation(
            @Parameter(description = "Identifiant du contrat", example = "1")
            @PathVariable long id) {
        return ContratResponse.from(contratService.desaffecterReservation(id));
    }

    @PatchMapping("/{id}/paiements/{idPaiement}")
    @Operation(summary = "Affecter un paiement à un contrat")
    public PaiementResponse affecterPaiement(
            @Parameter(description = "Identifiant du contrat", example = "1")
            @PathVariable long id,
            @Parameter(description = "Identifiant du paiement", example = "1")
            @PathVariable long idPaiement) {
        return PaiementResponse.from(contratService.affecterPaiement(id, idPaiement));
    }

    @DeleteMapping("/{id}/paiements/{idPaiement}")
    @Operation(summary = "Désaffecter un paiement de son contrat")
    public PaiementResponse desaffecterPaiement(
            @Parameter(description = "Identifiant du contrat", example = "1")
            @PathVariable long id,
            @Parameter(description = "Identifiant du paiement", example = "1")
            @PathVariable long idPaiement) {
        return PaiementResponse.from(contratService.desaffecterPaiement(idPaiement));
    }

    @GetMapping("/valides")
    @Operation(summary = "Lister les contrats valides")
    public List<ContratResponse> listerValides() {
        return contratService.listerValides().stream().map(ContratResponse::from).toList();
    }

    @GetMapping("/invalides")
    @Operation(summary = "Lister les contrats invalides")
    public List<ContratResponse> listerInvalides() {
        return contratService.listerInvalides().stream().map(ContratResponse::from).toList();
    }

    @GetMapping("/reservation/{idReservation}")
    @Operation(summary = "Chercher le contrat associé à une réservation")
    public ContratResponse rechercherParReservation(
            @Parameter(description = "Identifiant de la réservation", example = "1")
            @PathVariable long idReservation) {
        return ContratResponse.from(contratService.rechercherParReservation(idReservation)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Contrat introuvable pour la réservation " + idReservation)));
    }

    @GetMapping("/signature")
    @Operation(summary = "Lister les contrats signés entre deux dates")
    public List<ContratResponse> listerParDateSignatureEntre(
            @Parameter(description = "Date de début", example = "2026-01-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @Parameter(description = "Date de fin", example = "2026-12-31")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return contratService.listerParDateSignatureEntre(debut, fin).stream()
                .map(ContratResponse::from).toList();
    }

    @GetMapping("/montant-min/{montant}")
    @Operation(summary = "Lister les contrats dont le montant dépasse un seuil")
    public List<ContratResponse> listerParMontantMinimum(
            @Parameter(description = "Montant minimum", example = "500")
            @PathVariable BigDecimal montant) {
        return contratService.listerParMontantMinimum(montant).stream()
                .map(ContratResponse::from).toList();
    }

    @GetMapping("/valides/count")
    @Operation(summary = "Compter les contrats valides")
    public long compterValides() {
        return contratService.compterValides();
    }

    @GetMapping("/signes-apres")
    @Operation(summary = "Compter les contrats signés après une date")
    public long compterSignesApres(
            @Parameter(description = "Date de référence", example = "2026-01-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return contratService.compterSignesApres(date);
    }
}
