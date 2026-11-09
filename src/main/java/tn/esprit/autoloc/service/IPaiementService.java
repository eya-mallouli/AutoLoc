package tn.esprit.autoloc.service;

import tn.esprit.autoloc.web.dto.PaiementRequestDTO;
import tn.esprit.autoloc.web.dto.PaiementResponseDTO;

import java.util.List;

public interface IPaiementService {

    List<PaiementResponseDTO> findAll();

    PaiementResponseDTO findById(Long id);

    PaiementResponseDTO create(PaiementRequestDTO dto);

    PaiementResponseDTO update(Long id, PaiementRequestDTO dto);

    void delete(Long id);
}