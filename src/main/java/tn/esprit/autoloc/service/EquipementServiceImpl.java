package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IEquipementRepository;
import tn.esprit.autoloc.web.dto.EquipementRequestDTO;
import tn.esprit.autoloc.web.dto.EquipementResponseDTO;
import tn.esprit.autoloc.web.mapper.EquipementMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EquipementServiceImpl implements IEquipementService {

    private final IEquipementRepository repository;
    private final EquipementMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<EquipementResponseDTO> findAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public EquipementResponseDTO findById(Long id) {
        return mapper.toResponse(getOrThrow(id));
    }

    @Override
    public EquipementResponseDTO create(EquipementRequestDTO dto) {
        return mapper.toResponse(repository.save(mapper.toEntity(dto)));
    }

    @Override
    public EquipementResponseDTO update(Long id, EquipementRequestDTO dto) {
        Equipement equipement = getOrThrow(id);
        mapper.updateEntity(equipement, dto);
        return mapper.toResponse(repository.save(equipement));
    }

    @Override
    public void delete(Long id) {
        repository.delete(getOrThrow(id));
    }

    private Equipement getOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipement introuvable avec l'id " + id));
    }
}