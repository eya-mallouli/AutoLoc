package tn.esprit.autoloc.web.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record MaintenanceRequestDTO(
        @NotNull(message = "La date de début est obligatoire")
        LocalDate dateDebut,

        LocalDate dateFin,

        @NotBlank(message = "La description est obligatoire")
        @Size(max = 255, message = "La description ne doit pas dépasser 255 caractères")
        String description
) {
}