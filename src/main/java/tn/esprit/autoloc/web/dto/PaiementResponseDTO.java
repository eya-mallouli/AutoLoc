package tn.esprit.autoloc.web.dto;

import tn.esprit.autoloc.domain.ModePaiement;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaiementResponseDTO(
        Long idPaiement,
        BigDecimal montant,
        LocalDateTime datePaiement,
        ModePaiement modePaiement
) {
}