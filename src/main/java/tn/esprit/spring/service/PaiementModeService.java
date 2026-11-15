package tn.esprit.spring.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import tn.esprit.spring.web.dto.PaiementModeResponse;

import java.math.BigDecimal;

@Service
public class PaiementModeService {

    private final IMoyenPaiementService paiementParDefaut;
    private final IMoyenPaiementService paiementEspeces;

    public PaiementModeService(
            IMoyenPaiementService paiementParDefaut,
            @Qualifier("especesPaiementService") IMoyenPaiementService paiementEspeces) {
        this.paiementParDefaut = paiementParDefaut;
        this.paiementEspeces = paiementEspeces;
    }

    public PaiementModeResponse payerParDefaut(BigDecimal montant) {
        return paiementParDefaut.payer(montant);
    }

    public PaiementModeResponse payerEnEspeces(BigDecimal montant) {
        return paiementEspeces.payer(montant);
    }
}
