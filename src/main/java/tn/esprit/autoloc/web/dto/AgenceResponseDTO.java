package tn.esprit.autoloc.web.dto;

public record AgenceResponseDTO(
        Long idAgence,
        String nom,
        String ville,
        String adresse,
        String telephone
) {
}