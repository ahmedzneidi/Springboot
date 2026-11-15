package tn.esprit.spring.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.spring.domain.Maintenance;
import tn.esprit.spring.service.MaintenanceService;
import tn.esprit.spring.web.dto.MaintenanceRequest;

import java.util.List;

@RestController
@RequestMapping("/maintenances")
@Tag(name = "Maintenances", description = "Gestion des opérations de maintenance")
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    public MaintenanceController(MaintenanceService maintenanceService) {
        this.maintenanceService = maintenanceService;
    }

    @PostMapping
    @Operation(summary = "Créer une maintenance pour un véhicule")
    public ResponseEntity<Maintenance> creer(@RequestBody MaintenanceRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(maintenanceService.creer(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Chercher une maintenance par identifiant")
    public Maintenance obtenirParId(@PathVariable long id) {
        return maintenanceService.obtenirParId(id);
    }

    @GetMapping("/listall")
    @Operation(summary = "Lister les maintenances")
    public List<Maintenance> listerTous() {
        return maintenanceService.listerTous();
    }

    @GetMapping("/vehicule/{idVehicule}")
    @Operation(summary = "Lister les maintenances d'un véhicule")
    public List<Maintenance> listerParVehicule(@PathVariable long idVehicule) {
        return maintenanceService.listerParVehicule(idVehicule);
    }

    @GetMapping("/encours")
    @Operation(summary = "Lister les maintenances en cours")
    public List<Maintenance> listerEnCours() {
        return maintenanceService.listerEnCours();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier une maintenance")
    public ResponseEntity<Maintenance> modifier(@PathVariable long id, @RequestBody Maintenance maintenance) {
        return ResponseEntity.ok(maintenanceService.modifier(id, maintenance));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une maintenance")
    public ResponseEntity<Void> supprimer(@PathVariable long id) {
        maintenanceService.supprimer(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/vehicule/{idVehicule}")
    @Operation(summary = "Affecter une maintenance à un véhicule")
    public Maintenance affecterVehicule(@PathVariable long id, @PathVariable long idVehicule) {
        return maintenanceService.affecterVehicule(id, idVehicule);
    }

    @DeleteMapping("/{id}/vehicule")
    @Operation(summary = "Désaffecter une maintenance de son véhicule")
    public Maintenance desaffecterVehicule(@PathVariable long id) {
        return maintenanceService.desaffecterVehicule(id);
    }
}
