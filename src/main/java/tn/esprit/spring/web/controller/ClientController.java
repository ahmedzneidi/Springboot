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
import tn.esprit.spring.service.IClientService;
import tn.esprit.spring.web.dto.ApiError;
import tn.esprit.spring.web.dto.ClientRequest;
import tn.esprit.spring.web.dto.ClientResponse;
import tn.esprit.spring.web.dto.ReservationResponse;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/clients")
@Tag(name = "Clients", description = "Gestion des clients")
public class ClientController {

    private final IClientService clientService;

    public ClientController(IClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    @Operation(summary = "Créer un client")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Client créé"),
            @ApiResponse(responseCode = "400", description = "Données invalides",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "409", description = "Conflit de données",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<ClientResponse> creer(@Valid @RequestBody ClientRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ClientResponse.from(clientService.creer(request.toEntity())));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Chercher un client par identifiant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Client trouvé"),
            @ApiResponse(responseCode = "404", description = "Client introuvable",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ClientResponse obtenirParId(
            @Parameter(description = "Identifiant du client", example = "1")
            @PathVariable long id) {
        return ClientResponse.from(clientService.obtenirParId(id));
    }

    @GetMapping("/listall")
    @Operation(summary = "Lister les clients")
    public List<ClientResponse> listerTous() {
        return clientService.listerTous().stream().map(ClientResponse::from).toList();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un client")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Client modifié"),
            @ApiResponse(responseCode = "400", description = "Données invalides",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Client introuvable",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<ClientResponse> modifier(
            @Parameter(description = "Identifiant du client", example = "1")
            @PathVariable long id,
            @Valid @RequestBody ClientRequest request) {
        return ResponseEntity.ok(ClientResponse.from(clientService.modifier(id, request.toEntity())));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un client")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Client supprimé"),
            @ApiResponse(responseCode = "404", description = "Client introuvable",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "409", description = "Suppression impossible",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<Void> supprimer(
            @Parameter(description = "Identifiant du client", example = "1")
            @PathVariable long id) {
        clientService.supprimer(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/reservations/{idReservation}")
    @Operation(summary = "Affecter une réservation à un client")
    public ReservationResponse affecterReservation(
            @Parameter(description = "Identifiant du client", example = "1")
            @PathVariable long id,
            @Parameter(description = "Identifiant de la réservation", example = "1")
            @PathVariable long idReservation) {
        return ReservationResponse.from(clientService.affecterReservation(id, idReservation));
    }

    @DeleteMapping("/{id}/reservations/{idReservation}")
    @Operation(summary = "Désaffecter une réservation de son client")
    public ResponseEntity<Void> desaffecterReservation(
            @Parameter(description = "Identifiant du client", example = "1")
            @PathVariable long id,
            @Parameter(description = "Identifiant de la réservation", example = "1")
            @PathVariable long idReservation) {
        clientService.desaffecterReservation(idReservation);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/recherche/email/{email}")
    @Operation(summary = "Rechercher un client par email")
    public ClientResponse rechercherParEmail(
            @Parameter(description = "Email du client", example = "ahmed.zneidi@example.com")
            @PathVariable String email) {
        return ClientResponse.from(clientService.rechercherParEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Client introuvable avec l'email " + email)));
    }

    @GetMapping("/recherche")
    @Operation(summary = "Rechercher des clients par nom ou prénom")
    public List<ClientResponse> rechercherParNom(
            @Parameter(description = "Nom ou prénom recherché", example = "Ahmed")
            @RequestParam String q) {
        return clientService.rechercherParNom(q).stream().map(ClientResponse::from).toList();
    }

    @GetMapping("/inscriptions")
    @Operation(summary = "Lister les clients inscrits entre deux dates")
    public List<ClientResponse> listerParDateInscriptionEntre(
            @Parameter(description = "Date de début", example = "2026-01-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @Parameter(description = "Date de fin", example = "2026-12-31")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return clientService.listerParDateInscriptionEntre(debut, fin).stream()
                .map(ClientResponse::from).toList();
    }

    @GetMapping("/inscriptions/apres")
    @Operation(summary = "Lister les clients inscrits après une date")
    public List<ClientResponse> listerParDateInscriptionApres(
            @Parameter(description = "Date de référence", example = "2026-01-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return clientService.listerParDateInscriptionApres(date).stream()
                .map(ClientResponse::from).toList();
    }

    @GetMapping("/inscriptions/apres/count")
    @Operation(summary = "Compter les clients inscrits après une date")
    public long compterParDateInscriptionApres(
            @Parameter(description = "Date de référence", example = "2026-01-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return clientService.compterParDateInscriptionApres(date);
    }

    @GetMapping("/existe/permis/{numPermis}")
    @Operation(summary = "Vérifier l'existence d'un numéro de permis")
    public boolean existeParNumPermis(
            @Parameter(description = "Numéro de permis", example = "PERMIS-2026-001")
            @PathVariable String numPermis) {
        return clientService.existeParNumPermis(numPermis);
    }
}
