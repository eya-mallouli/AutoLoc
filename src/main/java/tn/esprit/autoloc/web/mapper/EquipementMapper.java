package tn.esprit.autoloc.web.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.web.dto.EquipementRequestDTO;
import tn.esprit.autoloc.web.dto.EquipementResponseDTO;

@Component
public class EquipementMapper {

    public Equipement toEntity(EquipementRequestDTO dto) {
        Equipement equipement = new Equipement();
        updateEntity(equipement, dto);
        return equipement;
    }

    public void updateEntity(Equipement equipement, EquipementRequestDTO dto) {
        equipement.setLibelle(dto.libelle());
    }

    public EquipementResponseDTO toResponse(Equipement equipement) {
        return new EquipementResponseDTO(
                equipement.getIdEquipement(),
                equipement.getLibelle());
    }
}