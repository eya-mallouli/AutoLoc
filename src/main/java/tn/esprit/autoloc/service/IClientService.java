package tn.esprit.autoloc.service;

import tn.esprit.autoloc.web.dto.ClientRequestDTO;
import tn.esprit.autoloc.web.dto.ClientResponseDTO;

import java.util.List;

public interface IClientService {

    List<ClientResponseDTO> findAll();

    ClientResponseDTO findById(Long id);

    ClientResponseDTO create(ClientRequestDTO dto);

    ClientResponseDTO update(Long id, ClientRequestDTO dto);

    void delete(Long id);
}