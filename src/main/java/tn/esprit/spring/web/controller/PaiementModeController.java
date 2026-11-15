package tn.esprit.spring.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.spring.service.PaiementModeService;
import tn.esprit.spring.web.dto.PaiementModeResponse;

import java.math.BigDecimal;

@RestController
@RequestMapping("/paiements/modes")
@Tag(name = "Modes de paiement", description = "Démonstration de la sélection de Beans avec @Primary et @Qualifier")
public class PaiementModeController {

    private final PaiementModeService paiementModeService;

    public PaiementModeController(PaiementModeService paiementModeService) {
        this.paiementModeService = paiementModeService;
    }

    @GetMapping("/defaut")
    @Operation(summary = "Payer avec le Bean @Primary")
    public PaiementModeResponse payerParDefaut(
            @Parameter(description = "Montant à payer", example = "100")
            @RequestParam BigDecimal montant) {
        return paiementModeService.payerParDefaut(montant);
    }

    @GetMapping("/especes")
    @Operation(summary = "Payer avec le Bean sélectionné par @Qualifier")
    public PaiementModeResponse payerEnEspeces(
            @Parameter(description = "Montant à payer", example = "100")
            @RequestParam BigDecimal montant) {
        return paiementModeService.payerEnEspeces(montant);
    }
}
