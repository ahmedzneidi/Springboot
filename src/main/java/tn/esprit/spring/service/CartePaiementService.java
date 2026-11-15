package tn.esprit.spring.service;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import tn.esprit.spring.domain.ModePaiement;
import tn.esprit.spring.web.dto.PaiementModeResponse;

import java.math.BigDecimal;

@Service
@Primary
public class CartePaiementService implements IMoyenPaiementService {

    @Override
    public PaiementModeResponse payer(BigDecimal montant) {
        return new PaiementModeResponse(
                ModePaiement.CARTE,
                "Paiement par carte de " + montant);
    }
}
