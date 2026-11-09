package tn.esprit.autoloc.service;

import tn.esprit.autoloc.web.dto.AgenceRequestDTO;
import tn.esprit.autoloc.web.dto.AgenceResponseDTO;

import java.util.List;

public interface IAgenceService {

    List<AgenceResponseDTO> findAll();

    AgenceResponseDTO findById(Long id);

    AgenceResponseDTO create(AgenceRequestDTO dto);

    AgenceResponseDTO update(Long id, AgenceRequestDTO dto);

    void delete(Long id);
}