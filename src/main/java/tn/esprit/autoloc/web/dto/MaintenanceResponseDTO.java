package tn.esprit.autoloc.web.dto;

import java.time.LocalDate;

public record MaintenanceResponseDTO(
        Long idMaintenance,
        LocalDate dateDebut,
        LocalDate dateFin,
        String description
) {
}