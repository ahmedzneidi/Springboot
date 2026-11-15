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
import tn.esprit.spring.domain.Vehicule;
import tn.esprit.spring.service.VehiculeService;

import java.util.List;

@RestController
@RequestMapping("/vehicules")
@Tag(name = "Véhicules", description = "Gestion de la flotte de véhicules")
public class VehiculeController {

    private final VehiculeService vehiculeService;

    public VehiculeController(VehiculeService vehiculeService) {
        this.vehiculeService = vehiculeService;
    }

    @PostMapping
    @Operation(summary = "Créer un véhicule")
    public ResponseEntity<Vehicule> creer(@RequestBody Vehicule vehicule) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vehiculeService.creer(vehicule));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Chercher un véhicule par identifiant")
    public Vehicule obtenirParId(@PathVariable long id) {
        return vehiculeService.obtenirParId(id);
    }

    @GetMapping("/listall")
    @Operation(summary = "Lister les véhicules")
    public List<Vehicule> listerTous() {
        return vehiculeService.listerTous();
    }

    @GetMapping("/disponibles")
    @Operation(summary = "Lister les véhicules disponibles")
    public List<Vehicule> listerDisponibles() {
        return vehiculeService.listerDisponibles();
    }

    @GetMapping("/agence/{idAgence}")
    @Operation(summary = "Lister les véhicules d'une agence")
    public List<Vehicule> listerParAgence(@PathVariable long idAgence) {
        return vehiculeService.listerParAgence(idAgence);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un véhicule")
    public ResponseEntity<Vehicule> modifier(@PathVariable long id, @RequestBody Vehicule vehicule) {
        return ResponseEntity.ok(vehiculeService.modifier(id, vehicule));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un véhicule")
    public ResponseEntity<Void> supprimer(@PathVariable long id) {
        vehiculeService.supprimer(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{idVehicule}/agence/{idAgence}")
    @Operation(summary = "Affecter un véhicule à une agence")
    public Vehicule affecterAgence(@PathVariable long idVehicule, @PathVariable long idAgence) {
        return vehiculeService.affecterAgence(idVehicule, idAgence);
    }

    @DeleteMapping("/{idVehicule}/agence")
    @Operation(summary = "Désaffecter un véhicule de son agence")
    public Vehicule desaffecterAgence(@PathVariable long idVehicule) {
        return vehiculeService.desaffecterAgence(idVehicule);
    }

    @PatchMapping("/{idVehicule}/equipements/{idEquipement}")
    @Operation(summary = "Affecter un équipement à un véhicule")
    public Vehicule affecterEquipement(@PathVariable long idVehicule, @PathVariable long idEquipement) {
        return vehiculeService.affecterEquipement(idVehicule, idEquipement);
    }

    @DeleteMapping("/{idVehicule}/equipements/{idEquipement}")
    @Operation(summary = "Désaffecter un équipement d'un véhicule")
    public Vehicule desaffecterEquipement(@PathVariable long idVehicule, @PathVariable long idEquipement) {
        return vehiculeService.desaffecterEquipement(idVehicule, idEquipement);
    }
}
