package tn.esprit.autoloc.web.dto;

import jakarta.validation.constraints.*;
import tn.esprit.autoloc.domain.StatutReservation;

import java.time.LocalDate;

public record ReservationRequestDTO(
        @NotNull(message = "La date de début est obligatoire")
        LocalDate dateDebut,

        @NotNull(message = "La date de fin est obligatoire")
        LocalDate dateFin,

        @NotNull(message = "Le statut est obligatoire")
        StatutReservation statut
) {
}