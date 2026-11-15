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
import tn.esprit.spring.domain.StatutReservation;
import tn.esprit.spring.service.IReservationService;
import tn.esprit.spring.web.dto.ApiError;
import tn.esprit.spring.web.dto.ReservationRequest;
import tn.esprit.spring.web.dto.ReservationResponse;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reservations")
@Tag(name = "Réservations", description = "Gestion des réservations de véhicules")
public class ReservationController {

    private final IReservationService reservationService;

    public ReservationController(IReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    @Operation(summary = "Créer une réservation")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Réservation créée"),
            @ApiResponse(responseCode = "400", description = "Données invalides",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "409", description = "Conflit de données",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<ReservationResponse> creer(@Valid @RequestBody ReservationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ReservationResponse.from(reservationService.creer(request.toEntity())));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Chercher une réservation par identifiant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Réservation trouvée"),
            @ApiResponse(responseCode = "404", description = "Réservation introuvable",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ReservationResponse obtenirParId(
            @Parameter(description = "Identifiant de la réservation", example = "1")
            @PathVariable long id) {
        return ReservationResponse.from(reservationService.obtenirParId(id));
    }

    @GetMapping("/listall")
    @Operation(summary = "Lister les réservations")
    public List<ReservationResponse> listerTous() {
        return reservationService.listerTous().stream().map(ReservationResponse::from).toList();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier une réservation")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Réservation modifiée"),
            @ApiResponse(responseCode = "400", description = "Données invalides",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Réservation introuvable",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<ReservationResponse> modifier(
            @Parameter(description = "Identifiant de la réservation", example = "1")
            @PathVariable long id,
            @Valid @RequestBody ReservationRequest request) {
        return ResponseEntity.ok(ReservationResponse.from(reservationService.modifier(id, request.toEntity())));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une réservation")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Réservation supprimée"),
            @ApiResponse(responseCode = "404", description = "Réservation introuvable",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "409", description = "Suppression impossible",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<Void> supprimer(
            @Parameter(description = "Identifiant de la réservation", example = "1")
            @PathVariable long id) {
        reservationService.supprimer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/client/{idClient}")
    @Operation(summary = "Lister les réservations d'un client")
    public List<ReservationResponse> listerParClient(
            @Parameter(description = "Identifiant du client", example = "1")
            @PathVariable long idClient) {
        return reservationService.listerParClient(idClient).stream().map(ReservationResponse::from).toList();
    }

    @GetMapping("/vehicule/{idVehicule}")
    @Operation(summary = "Lister les réservations d'un véhicule")
    public List<ReservationResponse> listerParVehicule(
            @Parameter(description = "Identifiant du véhicule", example = "1")
            @PathVariable long idVehicule) {
        return reservationService.listerParVehicule(idVehicule).stream()
                .map(ReservationResponse::from).toList();
    }

    @GetMapping("/statut/{statut}")
    @Operation(summary = "Lister les réservations par statut")
    public List<ReservationResponse> listerParStatut(
            @Parameter(description = "Statut recherché", example = "EN_ATTENTE")
            @PathVariable StatutReservation statut) {
        return reservationService.listerParStatut(statut).stream().map(ReservationResponse::from).toList();
    }

    @PatchMapping("/{id}/client/{idClient}")
    @Operation(summary = "Affecter une réservation à un client")
    public ReservationResponse affecterClient(
            @Parameter(description = "Identifiant de la réservation", example = "1")
            @PathVariable long id,
            @Parameter(description = "Identifiant du client", example = "1")
            @PathVariable long idClient) {
        return ReservationResponse.from(reservationService.affecterClient(id, idClient));
    }

    @DeleteMapping("/{id}/client")
    @Operation(summary = "Désaffecter la réservation de son client")
    public ReservationResponse desaffecterClient(
            @Parameter(description = "Identifiant de la réservation", example = "1")
            @PathVariable long id) {
        return ReservationResponse.from(reservationService.desaffecterClient(id));
    }

    @PatchMapping("/{id}/vehicule/{idVehicule}")
    @Operation(summary = "Affecter une réservation à un véhicule")
    public ReservationResponse affecterVehicule(
            @Parameter(description = "Identifiant de la réservation", example = "1")
            @PathVariable long id,
            @Parameter(description = "Identifiant du véhicule", example = "1")
            @PathVariable long idVehicule) {
        return ReservationResponse.from(reservationService.affecterVehicule(id, idVehicule));
    }

    @DeleteMapping("/{id}/vehicule")
    @Operation(summary = "Désaffecter la réservation de son véhicule")
    public ReservationResponse desaffecterVehicule(
            @Parameter(description = "Identifiant de la réservation", example = "1")
            @PathVariable long id) {
        return ReservationResponse.from(reservationService.desaffecterVehicule(id));
    }

    @GetMapping("/client/{idClient}/statut/{statut}")
    @Operation(summary = "Lister les réservations d'un client par statut")
    public List<ReservationResponse> listerParClientEtStatut(
            @Parameter(description = "Identifiant du client", example = "1")
            @PathVariable long idClient,
            @Parameter(description = "Statut recherché", example = "CONFIRMEE")
            @PathVariable StatutReservation statut) {
        return reservationService.listerParClientEtStatut(idClient, statut).stream()
                .map(ReservationResponse::from).toList();
    }

    @GetMapping("/dates")
    @Operation(summary = "Lister les réservations dont la date de début est comprise entre deux dates")
    public List<ReservationResponse> listerParDateDebutEntre(
            @Parameter(description = "Date de début", example = "2026-01-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @Parameter(description = "Date de fin", example = "2026-12-31")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return reservationService.listerParDateDebutEntre(debut, fin).stream()
                .map(ReservationResponse::from).toList();
    }

    @GetMapping("/terminees")
    @Operation(summary = "Lister les réservations terminées avant une date")
    public List<ReservationResponse> listerTermineesAvant(
            @Parameter(description = "Date de référence", example = "2026-11-15")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate avant) {
        return reservationService.listerTermineesAvant(avant).stream()
                .map(ReservationResponse::from).toList();
    }

    @GetMapping("/vehicule/{idVehicule}/active")
    @Operation(summary = "Vérifier si un véhicule a une réservation confirmée")
    public boolean existeActivePourVehicule(
            @Parameter(description = "Identifiant du véhicule", example = "1")
            @PathVariable long idVehicule) {
        return reservationService.existeActivePourVehicule(idVehicule);
    }

    @GetMapping("/contrat/{idContrat}")
    @Operation(summary = "Chercher la réservation associée à un contrat")
    public ReservationResponse rechercherParContrat(
            @Parameter(description = "Identifiant du contrat", example = "1")
            @PathVariable long idContrat) {
        return ReservationResponse.from(reservationService.rechercherParContrat(idContrat));
    }

    @GetMapping("/statut/{statut}/count")
    @Operation(summary = "Compter les réservations par statut")
    public long compterParStatut(
            @Parameter(description = "Statut recherché", example = "CONFIRMEE")
            @PathVariable StatutReservation statut) {
        return reservationService.compterParStatut(statut);
    }
}
