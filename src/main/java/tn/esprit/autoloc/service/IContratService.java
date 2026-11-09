package tn.esprit.autoloc.service;

import tn.esprit.autoloc.web.dto.ContratRequestDTO;
import tn.esprit.autoloc.web.dto.ContratResponseDTO;

import java.util.List;

public interface IContratService {

    List<ContratResponseDTO> findAll();

    ContratResponseDTO findById(Long id);

    ContratResponseDTO create(ContratRequestDTO dto);

    ContratResponseDTO update(Long id, ContratRequestDTO dto);

    void delete(Long id);
}