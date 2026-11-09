package tn.esprit.autoloc.service;

import tn.esprit.autoloc.web.dto.EquipementRequestDTO;
import tn.esprit.autoloc.web.dto.EquipementResponseDTO;

import java.util.List;

public interface IEquipementService {

    List<EquipementResponseDTO> findAll();

    EquipementResponseDTO findById(Long id);

    EquipementResponseDTO create(EquipementRequestDTO dto);

    EquipementResponseDTO update(Long id, EquipementRequestDTO dto);

    void delete(Long id);
}