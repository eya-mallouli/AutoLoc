package tn.esprit.autoloc.web.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record ClientRequestDTO(
        @NotBlank(message = "Le nom est obligatoire")
        @Size(max = 50, message = "Le nom ne doit pas dépasser 50 caractères")
        String nom,

        @NotBlank(message = "Le prénom est obligatoire")
        @Size(max = 50, message = "Le prénom ne doit pas dépasser 50 caractères")
        String prenom,

        @NotBlank(message = "L'email est obligatoire")
        @Email(message = "Format d'email invalide")
        @Size(max = 100, message = "L'email ne doit pas dépasser 100 caractères")
        String email,

        @NotBlank(message = "Le téléphone est obligatoire")
        @Pattern(regexp = "^[0-9+ ]{8,15}$", message = "Numéro de téléphone invalide")
        String telephone,

        @NotBlank(message = "Le numéro de permis est obligatoire")
        @Size(max = 30, message = "Le numéro de permis ne doit pas dépasser 30 caractères")
        String numPermis,

        @NotNull(message = "La date d'inscription est obligatoire")
        LocalDate dateInscription
) {
}