package tn.esprit.spring.service;

import tn.esprit.spring.web.dto.PaiementModeResponse;

import java.math.BigDecimal;

public interface IMoyenPaiementService {

    PaiementModeResponse payer(BigDecimal montant);
}
