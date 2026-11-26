package tn.esprit.autoloc.service;

import tn.esprit.autoloc.web.dto.VehiculeRequestDTO;
import tn.esprit.autoloc.web.dto.VehiculeResponseDTO;

import java.util.List;

public interface IVehiculeService {

    List<VehiculeResponseDTO> findAll();

    VehiculeResponseDTO findById(Long id);

    VehiculeResponseDTO create(VehiculeRequestDTO dto);

    VehiculeResponseDTO update(Long id, VehiculeRequestDTO dto);

    void delete(Long id);

    VehiculeResponseDTO affecterVehiculeAEquipement(Long idVehicule, Long idEquipement);

    VehiculeResponseDTO affecterMaintenanceAvehicule(String description, String immatriculation);
}