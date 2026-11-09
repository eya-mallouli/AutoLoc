package tn.esprit.autoloc.web.dto;

import tn.esprit.autoloc.domain.RoleEmploye;

public record EmployeResponseDTO(
        Long idEmploye,
        String nom,
        String prenom,
        RoleEmploye role
) {
}