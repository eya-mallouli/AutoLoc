package tn.esprit.autoloc.web.dto;

import jakarta.validation.constraints.*;
import tn.esprit.autoloc.domain.ModePaiement;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaiementRequestDTO(
        @NotNull(message = "Le montant est obligatoire")
        @Positive(message = "Le montant doit être strictement positif")
        BigDecimal montant,

        @NotNull(message = "La date de paiement est obligatoire")
        LocalDateTime datePaiement,

        @NotNull(message = "Le mode de paiement est obligatoire")
        ModePaiement modePaiement
) {
}