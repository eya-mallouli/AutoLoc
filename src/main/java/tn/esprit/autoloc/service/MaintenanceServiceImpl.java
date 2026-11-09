package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Maintenance;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IMaintenanceRepository;
import tn.esprit.autoloc.web.dto.MaintenanceRequestDTO;
import tn.esprit.autoloc.web.dto.MaintenanceResponseDTO;
import tn.esprit.autoloc.web.mapper.MaintenanceMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MaintenanceServiceImpl implements IMaintenanceService {

    private final IMaintenanceRepository repository;
    private final MaintenanceMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<MaintenanceResponseDTO> findAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public MaintenanceResponseDTO findById(Long id) {
        return mapper.toResponse(getOrThrow(id));
    }

    @Override
    public MaintenanceResponseDTO create(MaintenanceRequestDTO dto) {
        return mapper.toResponse(repository.save(mapper.toEntity(dto)));
    }

    @Override
    public MaintenanceResponseDTO update(Long id, MaintenanceRequestDTO dto) {
        Maintenance maintenance = getOrThrow(id);
        mapper.updateEntity(maintenance, dto);
        return mapper.toResponse(repository.save(maintenance));
    }

    @Override
    public void delete(Long id) {
        repository.delete(getOrThrow(id));
    }

    private Maintenance getOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance introuvable avec l'id " + id));
    }
}