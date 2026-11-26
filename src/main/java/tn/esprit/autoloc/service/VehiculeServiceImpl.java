package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.domain.Maintenance;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IEquipementRepository;
import tn.esprit.autoloc.repository.IMaintenanceRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;
import tn.esprit.autoloc.web.dto.VehiculeRequestDTO;
import tn.esprit.autoloc.web.dto.VehiculeResponseDTO;
import tn.esprit.autoloc.web.mapper.VehiculeMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class VehiculeServiceImpl implements IVehiculeService {

    private final IVehiculeRepository repository;
    private final IEquipementRepository equipementRepository;
    private final IMaintenanceRepository maintenanceRepository;
    private final VehiculeMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<VehiculeResponseDTO> findAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public VehiculeResponseDTO findById(Long id) {
        return mapper.toResponse(getOrThrow(id));
    }

    @Override
    public VehiculeResponseDTO create(VehiculeRequestDTO dto) {
        return mapper.toResponse(repository.save(mapper.toEntity(dto)));
    }

    @Override
    public VehiculeResponseDTO update(Long id, VehiculeRequestDTO dto) {
        Vehicule vehicule = getOrThrow(id);
        mapper.updateEntity(vehicule, dto);
        return mapper.toResponse(repository.save(vehicule));
    }

    @Override
    public void delete(Long id) {
        repository.delete(getOrThrow(id));
    }

    @Override
    public VehiculeResponseDTO affecterVehiculeAEquipement(Long idVehicule, Long idEquipement) {
        Vehicule vehicule = getOrThrow(idVehicule);
        Equipement equipement = equipementRepository.findById(idEquipement)
                .orElseThrow(() -> new ResourceNotFoundException("Equipement introuvable avec l'id " + idEquipement));
        if (!vehicule.getEquipements().contains(equipement)) {
            vehicule.getEquipements().add(equipement);
        }
        if (!equipement.getVehicules().contains(vehicule)) {
            equipement.getVehicules().add(vehicule);
        }
        return mapper.toResponse(repository.save(vehicule));
    }

    @Override
    public VehiculeResponseDTO affecterMaintenanceAvehicule(String description, String immatriculation) {
        Maintenance maintenance = maintenanceRepository.findByDescription(description)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Maintenance introuvable avec la description " + description));
        Vehicule vehicule = repository.findByImmatriculation(immatriculation)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Véhicule introuvable avec l'immatriculation " + immatriculation));
        maintenance.setVehicule(vehicule);
        if (!vehicule.getMaintenances().contains(maintenance)) {
            vehicule.getMaintenances().add(maintenance);
        }
        maintenanceRepository.save(maintenance);
        return mapper.toResponse(repository.save(vehicule));
    }

    private Vehicule getOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Véhicule introuvable avec l'id " + id));
    }
}