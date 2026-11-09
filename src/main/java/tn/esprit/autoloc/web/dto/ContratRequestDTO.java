package tn.esprit.autoloc.web.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ContratRequestDTO(
        @NotNull(message = "La date de signature est obligatoire")
        LocalDate dateSignature,

        @NotNull(message = "Le montant total est obligatoire")
        @PositiveOrZero(message = "Le montant total ne peut pas être négatif")
        BigDecimal montantTotal,

        @NotNull(message = "Le champ valide est obligatoire")
        Boolean valide
) {
}