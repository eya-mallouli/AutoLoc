package tn.esprit.autoloc.service;

import tn.esprit.autoloc.web.dto.MaintenanceRequestDTO;
import tn.esprit.autoloc.web.dto.MaintenanceResponseDTO;

import java.util.List;

public interface IMaintenanceService {

    List<MaintenanceResponseDTO> findAll();

    MaintenanceResponseDTO findById(Long id);

    MaintenanceResponseDTO create(MaintenanceRequestDTO dto);

    MaintenanceResponseDTO update(Long id, MaintenanceRequestDTO dto);

    void delete(Long id);
}