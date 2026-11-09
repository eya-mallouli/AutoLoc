package tn.esprit.autoloc.web.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.web.dto.AgenceRequestDTO;
import tn.esprit.autoloc.web.dto.AgenceResponseDTO;

@Component
public class AgenceMapper {

    public Agence toEntity(AgenceRequestDTO dto) {
        Agence agence = new Agence();
        updateEntity(agence, dto);
        return agence;
    }

    public void updateEntity(Agence agence, AgenceRequestDTO dto) {
        agence.setNom(dto.nom());
        agence.setVille(dto.ville());
        agence.setAdresse(dto.adresse());
        agence.setTelephone(dto.telephone());
    }

    public AgenceResponseDTO toResponse(Agence agence) {
        return new AgenceResponseDTO(
                agence.getIdAgence(),
                agence.getNom(),
                agence.getVille(),
                agence.getAdresse(),
                agence.getTelephone());
    }
}