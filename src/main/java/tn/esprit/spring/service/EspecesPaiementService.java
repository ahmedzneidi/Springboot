package tn.esprit.spring.service;

import org.springframework.stereotype.Service;
import tn.esprit.spring.domain.ModePaiement;
import tn.esprit.spring.web.dto.PaiementModeResponse;

import java.math.BigDecimal;

@Service
public class EspecesPaiementService implements IMoyenPaiementService {

    @Override
    public PaiementModeResponse payer(BigDecimal montant) {
        return new PaiementModeResponse(
                ModePaiement.ESPECES,
                "Paiement en espèces de " + montant);
    }
}
